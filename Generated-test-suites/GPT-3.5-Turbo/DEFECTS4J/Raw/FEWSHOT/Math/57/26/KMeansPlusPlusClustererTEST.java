
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<ClusterablePoint> clusterer;
    private Collection<ClusterablePoint> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<ClusterablePoint>(new Random());
        points = new ArrayList<ClusterablePoint>();
        points.add(new ClusterablePoint(1, 2));
        points.add(new ClusterablePoint(3, 4));
        points.add(new ClusterablePoint(5, 6));
        points.add(new ClusterablePoint(7, 8));
        points.add(new ClusterablePoint(9, 10));
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, k, maxIterations);
        
        // Check number of clusters created
        assertEquals(k, clusters.size());
        
        // Check if all points are assigned to a cluster
        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertNotNull(cluster.getPoints());
            assertEquals(true, cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        int k = 2;
        int maxIterations = -1;
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, k, maxIterations);
        
        // Check number of clusters created
        assertEquals(k, clusters.size());
        
        // Check if all points are assigned to a cluster
        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertNotNull(cluster.getPoints());
            assertEquals(true, cluster.getPoints().size() > 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(new ArrayList<ClusterablePoint>(), k, maxIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusters() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, k, maxIterations);
        
        // Remove all points from each cluster
        for (Cluster<ClusterablePoint> cluster : clusters) {
            cluster.getPoints().clear();
        }
        
        // Cluster again, an exception should be thrown
        clusters = clusterer.cluster(points, k, maxIterations);
    }

    private static class ClusterablePoint implements Clusterable<ClusterablePoint> {
        private double x;
        private double y;

        public ClusterablePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(ClusterablePoint other) {
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
}
