import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-10;
    private static final Random RANDOM = new Random();

    // Test Data
    private List<Cluster<Point>> testData;

    @Before
    public void setUp() {
        testData = new ArrayList<Cluster<Point>>();
        for (int i = 0; i < 10; i++) {
            Cluster<Point> cluster = new Cluster<Point>();
            for (int j = 0; j < 10; j++) {
                Point point = new Point(i, j);
                cluster.addPoint(point);
            }
            testData.add(cluster);
        }
    }

    @Test
    public void testChooseInitialCenters() {
        int k = 5;
        List<Cluster<Point>> initialCenters = KMeansPlusPlusClusterer
                .chooseInitialCenters(testData, k, RANDOM);
        assertEquals(k, initialCenters.size());

        // check that all initial centers are contained in the test data
        for (Cluster<Point> cluster : initialCenters) {
            assertTrue(testData.contains(cluster));
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        int k = 5;
        List<Cluster<Point>> initialCenters = KMeansPlusPlusClusterer
                .chooseInitialCenters(testData, k, RANDOM);

        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < 10; i++) {
            Point point = new Point(i, 10);
            points.add(point);
        }

        KMeansPlusPlusClusterer.assignPointsToClusters(initialCenters, points);

        // check that all points are assigned to a cluster
        for (Point point : points) {
            assertTrue(isAssignedToCluster(initialCenters, point));
        }
    }

    private boolean isAssignedToCluster(List<Cluster<Point>> clusters,
                                        Point point) {
        for (Cluster<Point> cluster : clusters) {
            if (cluster.getPoints().contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testGetNearestCluster() {
        Cluster<Point> cluster1 = new Cluster<Point>();
        Cluster<Point> cluster2 = new Cluster<Point>();
        Collection<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point point = new Point(1, 1);
        cluster1.addPoint(point);

        Cluster<Point> nearestCluster = KMeansPlusPlusClusterer
                .getNearestCluster(clusters, point);

        assertEquals(cluster1, nearestCluster);
    }

    @Test
    public void testCluster() {
        int k = 5;
        int maxIterations = 100;

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(
                RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point>> clusters = clusterer.cluster(testData, k,
                maxIterations);

        assertEquals(k, clusters.size());

        // check that all points are assigned to a cluster
        for (Cluster<Point> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyCluster() {
        int k = 5;
        int maxIterations = 10;

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(
                RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Point>> clusters = clusterer.cluster(testData, k,
                maxIterations);

        fail("ConvergenceException should have been thrown");
    }

    private static class Point implements Clusterable<Point> {

        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(Point other) {
            double dx = other.getX() - x;
            double dy = other.getY() - y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            double centerX = sumX / points.size();
            double centerY = sumY / points.size();
            return new Point(centerX, centerY);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Point other = (Point) obj;
            return Math.abs(x - other.getX()) < EPSILON
                    && Math.abs(y - other.getY()) < EPSILON;
        }

        public int hashCode() {
            int result;
            long temp;
            temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
            result = (int) (temp ^ (temp >>> 32));
            temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

}