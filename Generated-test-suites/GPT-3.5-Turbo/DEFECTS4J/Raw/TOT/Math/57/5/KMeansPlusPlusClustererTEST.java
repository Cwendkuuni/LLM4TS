import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster_RegularInput() {
        // Regular input test case
        // Create some sample points
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(2, 2);
        Point p4 = new Point(3, 3);
        
        // Create a collection of points
        List<Point> points = Arrays.asList(p1, p2, p3, p4);
        
        // Create a clusterer with 2 clusters
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        
        // Cluster the points
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        
        // Expecting 2 clusters
        assertEquals(2, clusters.size());
        
        // Each cluster should contain exactly 2 points
        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(2, clusters.get(1).getPoints().size());
    }
    
    @Test
    public void testCluster_EdgeInput() {
        // Edge input test case
        // Create some sample points
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);
        Point p3 = new Point(1, 0);
        Point p4 = new Point(1, 1);
        
        // Create a collection of points
        List<Point> points = Arrays.asList(p1, p2, p3, p4);
        
        // Create a clusterer with 10 clusters
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        
        // Cluster the points
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, -1);
        
        // Expecting 4 clusters (each point is its own cluster)
        assertEquals(4, clusters.size());
        
        // Each cluster should contain exactly 1 point
        for (Cluster<Point> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testCluster_EmptyInput() {
        // Error scenario test case
        // Create an empty collection of points
        List<Point> points = Collections.emptyList();
        
        // Create a clusterer with 5 clusters
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        
        // Cluster the points
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        
        // Expecting an exception to be thrown (empty cluster)
    }
}