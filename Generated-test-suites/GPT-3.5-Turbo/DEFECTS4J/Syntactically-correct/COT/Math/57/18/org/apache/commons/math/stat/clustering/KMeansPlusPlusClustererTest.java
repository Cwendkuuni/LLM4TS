import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster_basicFunctionality() {
        // Create test data
        List<ClusterablePoint> points = new ArrayList<>();
        points.add(new ClusterablePoint(1, 2));
        points.add(new ClusterablePoint(3, 5));
        points.add(new ClusterablePoint(6, 9));
        
        // Perform clustering
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 10);
        
        // Verify the number of clusters
        assertEquals(2, clusters.size());
        
        // Verify that each point is assigned to a cluster
        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }
    
    @Test
    public void testCluster_emptyPoints() {
        // Create test data
        List<ClusterablePoint> points = new ArrayList<>();
        
        // Perform clustering
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 10);
        
        // Verify the number of clusters
        assertEquals(2, clusters.size());
        
        // Verify that each cluster is empty
        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertTrue(cluster.getPoints().isEmpty());
        }
    }
    
    @Test
    public void testCluster_maxIterationsReached() {
        // Create test data
        List<ClusterablePoint> points = new ArrayList<>();
        points.add(new ClusterablePoint(1, 2));
        points.add(new ClusterablePoint(3, 5));
        points.add(new ClusterablePoint(6, 9));
        
        // Perform clustering with max iterations set to 1
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 1);
        
        // Verify the number of clusters
        assertEquals(2, clusters.size());
        
        // Verify that each point is assigned to a cluster
        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testCluster_emptyStrategyError() {
        // Create test data
        List<ClusterablePoint> points = new ArrayList<>();
        points.add(new ClusterablePoint(1, 2));
        points.add(new ClusterablePoint(3, 5));
        points.add(new ClusterablePoint(6, 9));
        
        // Perform clustering with empty strategy set to ERROR
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        clusterer.cluster(points, 2, 10);
    }
}