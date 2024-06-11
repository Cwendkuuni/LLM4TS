import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Create a list of points
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        
        // Test clustering with k=2 and maxIterations=100
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        // Assert clustering result
        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterEmptyPoints() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Create an empty list of points
        List<Point> points = new ArrayList<>();
        
        // Test clustering with k=2 and maxIterations=100 for empty points
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        // Assert no clusters are created for empty points
        assertNotNull(clusters);
        assertTrue(clusters.isEmpty());
    }

    @Test
    public void testClusterMaxIterations() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Create a list of points
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        
        // Set maxIterations to 0 to force exit early
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 0);

        // Assert clusters are returned even with maxIterations set to 0
        assertNotNull(clusters);
        assertFalse(clusters.isEmpty());
    }

}