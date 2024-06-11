import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<TestPoint> clusterer;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
    }

    @Test
    public void testCluster() {
        List<TestPoint> points = new ArrayList<TestPoint>();
        points.add(new TestPoint(1.0, 2.0));
        points.add(new TestPoint(2.0, 3.0));
        points.add(new TestPoint(3.0, 4.0));
        points.add(new TestPoint(4.0, 5.0));
        points.add(new TestPoint(5.0, 6.0));
        points.add(new TestPoint(6.0, 7.0));
        
        int k = 2;
        int maxIterations = 100;
        
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, k, maxIterations);

        assertEquals(k, clusters.size());
        
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterInKMeans() {
        List<TestPoint> points = new ArrayList<TestPoint>();
        points.add(new TestPoint(1.0, 2.0));
        points.add(new TestPoint(2.0, 3.0));
        points.add(new TestPoint(3.0, 4.0));
        points.add(new TestPoint(4.0, 5.0));
        points.add(new TestPoint(5.0, 6.0));
        points.add(new TestPoint(6.0, 7.0));
        
        int k = 10;
        int maxIterations = 100;
        
        clusterer.cluster(points, k, maxIterations);
    }

    @Test
    public void testClusterMultipleIterations() {
        List<TestPoint> points = new ArrayList<TestPoint>();
        points.add(new TestPoint(1.0, 2.0));
        points.add(new TestPoint(2.0, 3.0));
        points.add(new TestPoint(3.0, 4.0));
        points.add(new TestPoint(4.0, 5.0));
        points.add(new TestPoint(5.0, 6.0));
        points.add(new TestPoint(6.0, 7.0));

        int k = 2;
        int maxIterations = 10;

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, k, maxIterations);

        for (int i = 1; i < maxIterations; i++) {
            List<Cluster<TestPoint>> newClusters = clusterer.cluster(points, k, maxIterations);

            boolean changed = false;
            for (int j = 0; j < clusters.size(); j++) {
                if (!clusters.get(j).equals(newClusters.get(j))) {
                    changed = true;
                    break;
                }
            }

            if (!changed) {
                break;
            }

            clusters = newClusters;
        }

        assertEquals(k, clusters.size());

        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    private static class TestPoint implements Clusterable<TestPoint> {

        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(TestPoint other) {
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

    }
}