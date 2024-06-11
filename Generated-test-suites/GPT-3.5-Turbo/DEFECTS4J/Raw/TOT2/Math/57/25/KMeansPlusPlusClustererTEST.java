import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create mock points for testing
        Clusterable<Point> point1 = mock(Point.class);
        Clusterable<Point> point2 = mock(Point.class);
        Clusterable<Point> point3 = mock(Point.class);

        when(point1.distanceFrom(any())).thenReturn(1.0);
        when(point2.distanceFrom(any())).thenReturn(2.0);
        when(point3.distanceFrom(any())).thenReturn(3.0);

        // Create a list of mock points
        List<Clusterable<Point>> points = new ArrayList<>(Arrays.asList(point1, point2, point3));

        // Create a KMeansPlusPlusClusterer instance for testing
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Test clustering with k=2 and maxIterations=10
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testChooseInitialCenters() {
        // Create mock points for testing
        Clusterable<Point> point1 = mock(Point.class);
        when(point1.distanceFrom(any())).thenReturn(1.0);

        Clusterable<Point> point2 = mock(Point.class);
        when(point2.distanceFrom(any())).thenReturn(2.0);

        Clusterable<Point> point3 = mock(Point.class);
        when(point3.distanceFrom(any())).thenReturn(3.0);

        List<Clusterable<Point>> points = new ArrayList<>(Arrays.asList(point1, point2, point3));

        // Create a KMeansPlusPlusClusterer instance for testing
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Test the chooseInitialCenters method
        List<Cluster<Point>> initialCenters = clusterer.chooseInitialCenters(points, 2, new Random());

        assertNotNull(initialCenters);
        assertEquals(2, initialCenters.size());
    }

    // Additional test cases for other methods in the class can be added here

}