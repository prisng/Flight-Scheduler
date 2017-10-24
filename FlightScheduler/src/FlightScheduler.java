import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class FlightScheduler {
	
	public static void main(String[] args) throws IOException {

		File timetable = new File(args[0]);

		HashMap<String, Integer> airportToInteger = new HashMap<String, Integer>();
		//EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(10); //airportToInteger.size()
		TreeSet<Integer> departureTimeHolder = new TreeSet<Integer>();
		HashSet<String> ewdSize = new HashSet<String>();
		HashSet<String> holder = new HashSet<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(timetable));) {
			String text;
			while ((text = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(text.replaceAll(",", ""));
					String origin = st.nextToken();
					String destination = st.nextToken();
					System.out.println(origin + destination);
					ewdSize.add(origin);
					ewdSize.add(destination);
			}
		}
		catch (FileNotFoundException x) {
			System.out.println("Please enter a valid timetable text file.");
			x.printStackTrace();
		}
		
		EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(ewdSize.size() + 1);
		
		try (BufferedReader br = new BufferedReader(new FileReader(timetable));) {
			String text;
			int i = 1;
			int v = 0;
			int w = 0;
			while ((text = br.readLine()) != null) {
				// System.out.println(text.replaceAll(",", "")); // Test: Print out input values
				StringTokenizer st = new StringTokenizer(text.replaceAll(",", ""));
				while (st.hasMoreTokens()) {
					String origin = st.nextToken();
					String destination = st.nextToken();
					int departureTime = Integer.parseInt(st.nextToken());
					departureTimeHolder.add(departureTime);
					int arrivalTime = Integer.parseInt(st.nextToken());
					int weight = arrivalTime - departureTime;
					if (holder.add(origin)) {
						airportToInteger.put(origin, i);
						i++;
					}
					if (holder.add(destination)) {
						airportToInteger.put(destination, i);
						i++;
					}
					v = airportToInteger.get(origin);
					w = airportToInteger.get(destination);
					DirectedEdge de = new DirectedEdge(v, w, departureTime, weight);
					ewd.addEdge(de);
					// Test: Print out all the directed edges with their weights
					//System.out.println(de.toString());
				}
			}
		} catch (FileNotFoundException x) {
			System.out.println("Please enter a valid timetable text file.");
			x.printStackTrace();
		}
	
		// Test: Iterating through the hash map of airport vertices, printing each integer assigned to each airport
		System.out.println("Hashmap of vertices: ");
		Set<String> keySet = airportToInteger.keySet();
		for (String i : keySet) {
			Integer a = airportToInteger.get(i);
			System.out.println(i + " " + a);
		}
		System.out.println("-------------------------");
		
		
		// Test: Print out departure times
		/*
		Integer[] toArray = departureTimeHolder.toArray(new Integer[departureTimeHolder.size()]);
		for (int i = 0; i < toArray.length; i++) {
			System.out.println(toArray[i]);
		}
		*/
	
		
		int s = 0;
		int d = 0;
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter an origin airport: ");
		String originAirport = scan.next();
		System.out.println("Enter an destination airport: ");
		String destinationAirport = scan.next();
		System.out.println("Enter a start time: ");
		int startTime = scan.nextInt();
		scan.close();
		
		if (airportToInteger.containsKey(originAirport) && airportToInteger.containsKey(destinationAirport)) {
			s = airportToInteger.get(originAirport);
			d = airportToInteger.get(destinationAirport);
		}
		else {
			System.out.println("Please enter valid airports.");
		}
		
		
		
		// Test: Print out graph before removing flights with starting times after start time
		System.out.println(ewd.toString());

		// Prints out the directed edges coming out of origin vertex
		for (DirectedEdge e : ewd.adj(s)) {
			System.out.println(e.toString());
			if (e.departureTime() < startTime) {
				ewd.deleteEdge(e);
			}
		}
		
		// Test: Print out graph after removing flights with starting times after start time
		//System.out.println(ewd.toString());
		

		DijkstraSP sp = new DijkstraSP(ewd, s);
		
		// Shortest path from source vertex to destination vertex
		if (sp.hasPathTo(d)) {
			System.out.printf("%d to %d (%.2f)  ", s, d, sp.distTo(d));
            for (DirectedEdge e : sp.pathTo(d)) {
                System.out.print(e + "   ");
            }
		}
		else {
			System.out.printf("%d to %d         no path\n", s, d);
		}
		
	}
}
