
import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {
    
    @Test
    public void testCluster() {
        // Test case using a simple 2D dataset
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 12));
        
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        
        // Check that the number of clusters is correct
        assertEquals(2, clusters.size());
        
        // Check that each point is assigned to a cluster
        for (Point point : points) {
            boolean assigned = false;
            for (Cluster<Point> cluster : clusters) {
                if (cluster.getPoints().contains(point)) {
                    assigned = true;
                    break;
                }
            }
            assertTrue(assigned);
        }
    }
    
    @Test
    public void testClusterWithEmptyPoints() {
        // Test case using an empty dataset
        List<Point> points = new ArrayList<>();
        
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        
        // Check that no clusters were generated
        assertEquals(0, clusters.size());
    }
    
    @Test
    public void testClusterWithNegativeMaxIterations() {
        // Test case with a negative maxIterations value
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 12));
        
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, -1);
        
        // Check that the number of clusters is correct
        assertEquals(2, clusters.size());
        
        // Check that each point is assigned to a cluster
        for (Point point : points) {
            boolean assigned = false;
            for (Cluster<Point> cluster : clusters) {
                if (cluster.getPoints().contains(point)) {
                    assigned = true;
                    break;
                }
            }
            assertTrue(assigned);
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterStrategyError() {
        // Test case with empty cluster strategy set to ERROR
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 12));
        
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), 
            KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        
        // This assertion should not be reached since an exception should be thrown
        assertEquals(2, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestVarianceClusterWithEmptyClusters() {
        // Test case with empty clusters list
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        clusterer.getPointFromLargestVarianceCluster(new ArrayList<Cluster<Point>>());
        
        // This assertion should not be reached since an exception should be thrown
        fail("Exception should have been thrown");
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestNumberClusterWithEmptyClusters() {
        // Test case with empty clusters list
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        clusterer.getPointFromLargestNumberCluster(new ArrayList<Cluster<Point>>());
        
        // This assertion should not be reached since an exception should be thrown
        fail("Exception should have been thrown");
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetFarthestPointWithEmptyClusters() {
        // Test case with empty clusters list
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        clusterer.getFarthestPoint(new ArrayList<Cluster<Point>>());
        
        // This assertion should not be reached since an exception should be thrown
        fail("Exception should have been thrown");
    }
    
    @Test
    public void testGetNearestCluster() {
        // Test case for getNearestCluster method
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(0, 0));
        Cluster<Point> cluster2 = new Cluster<>(new Point(1, 1));
        cluster1.addPoint(new Point(0.5, 0.5));
        cluster2.addPoint(new Point(1.5, 1.5));
        clusters.add(cluster1);
        clusters.add(cluster2);
        
        Point point1 = new Point(0.1, 0.1);
        Cluster<Point> nearest1 = KMeansPlusPlusClusterer.getNearestCluster(clusters, point1);
        assertEquals(cluster1, nearest1);
        
        Point point2 = new Point(1.9, 1.9);
        Cluster<Point> nearest2 = KMeansPlusPlusClusterer.getNearestCluster(clusters, point2);
        assertEquals(cluster2, nearest2);
    }
    
}
