
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-6;
    private KMeansPlusPlusClusterer<ClusterablePoint> clusterer;
    private Collection<ClusterablePoint> points;
    private int numClusters;
    private int maxIterations;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random());
        points = new ArrayList<>();
        points.add(new ClusterablePoint(1.0, 2.0));
        points.add(new ClusterablePoint(3.0, 4.0));
        points.add(new ClusterablePoint(5.0, 6.0));
        numClusters = 2;
        maxIterations = 10;
    }

    @Test
    public void testCluster() {
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, numClusters, maxIterations);
        assertEquals(numClusters, clusters.size());

        for (Cluster<ClusterablePoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (ClusterablePoint point : cluster.getPoints()) {
                assertTrue(cluster.getCenter().distanceFrom(point) <= EPSILON);
            }
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        clusterer.cluster(new ArrayList<>(), numClusters, maxIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterInKMeans() {
        clusterer.cluster(Arrays.asList(new ClusterablePoint(1.0,2.0)),
                numClusters, maxIterations);
    }

    private static class ClusterablePoint implements Clusterable<ClusterablePoint> {

        private final double x;
        private final double y;

        public ClusterablePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(ClusterablePoint other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public ClusterablePoint centroidOf(Collection<ClusterablePoint> points) {
            double centroidX = 0.0;
            double centroidY = 0.0;
            int count = 0;
            for (ClusterablePoint point : points) {
                centroidX += point.x;
                centroidY += point.y;
                count++;
            }
            return new ClusterablePoint(centroidX / count, centroidY / count);
        }
    }
}
