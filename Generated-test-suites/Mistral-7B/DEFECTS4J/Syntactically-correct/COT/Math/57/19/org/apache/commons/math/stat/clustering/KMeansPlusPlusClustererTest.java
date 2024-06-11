import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.apache.commons.math3.util.pair.OpenIntIntPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private final int maxIterations;
    private final int numClusters;
    private final double[][] points;
    private final int k;

    public KMeansPlusPlusClustererTest(int maxIterations, int numClusters, double[][] points, int k) {
        this.maxIterations = maxIterations;
        this.numClusters = numClusters;
        this.points = points;
        this.k = k;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {5, 3, new double[][]{
                        {0, 0},
                        {1, 1},
                        {2, 2},
                        {3, 3},
                        {4, 4},
                        {5, 5}}, 3},
                {10, 2, new double[][]{
                        {0.1, 0.1},
                        {0.2, 0.2},
                        {0.3, 0.3},
                        {0.4, 0.4},
                        {0.5, 0.5},
                        {0.6, 0.6},
                        {0.7, 0.7},
                        {0.8, 0.8},
                        {0.9, 0.9},
                        {1.0, 1.0}}, 2},
                {-1, 2, new double[][]{
                        {0, 0},
                        {1, 1},
                        {2, 2},
                        {3, 3},
                        {4, 4},
                        {5, 5}}, 2},
                {5, 5, new double[][]{
                        {0, 0},
                        {1, 1},
                        {2, 2},
                        {3, 3},
                        {4, 4},
                        {5, 5}}, 5}
        });
    }

    @Test
    public void testCluster() {
        Collection<Cluster<TestPoint>> result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(Arrays.asList(getTestPoints()), k, maxIterations);
        assertEquals(numClusters, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertEquals(k > 0 ? (double) Math.floor(1.0 * cluster.getPoints().size() / k) : 1, cluster.getPoints().size() / (double) numClusters, 0.01);
        }
    }

    private static List<TestPoint> getTestPoints() {
        List<TestPoint> points = new ArrayList<>();
        for (double[] p : points) {
            points.add(new TestPoint(p[0], p[1]));
        }
        return points;
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<TestPoint>(new TestPoint(0.0, 0.0)));
        clusters.remove(0);
        new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR).cluster(Arrays.asList(getTestPoints()), k, maxIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoMaxIterations() {
        Collection<Cluster<TestPoint>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(Arrays.asList(getTestPoints()), k, -1);
        assertNull(clusters);
    }

    @Test
    public void testGetNearestCluster() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        double[][] points = new double[][]{
                {1, 1},
                {2, 2},
                {3, 3},
                {4, 4},
                {5, 5}
        };
        TestPoint center1 = new TestPoint(2, 2);
        TestPoint center2 = new TestPoint(3, 3);
        clusters.add(new Cluster<TestPoint>(center1));
        clusters.add(new Cluster<TestPoint>(center2));

        TestPoint testPoint = new TestPoint(2.5, 2.5);
        Cluster<TestPoint> nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, testPoint);
        assertEquals(center1, nearestCluster.getCenter());

        testPoint = new TestPoint(3.5, 3.5);
        nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, testPoint);
        assertEquals(center2, nearestCluster.getCenter());

        testPoint = new TestPoint(1.5, 1.5);
        nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, testPoint);
        assertEquals(center1, nearestCluster.getCenter());

        testPoint = new TestPoint(4.5, 4.5);
        nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, testPoint);
        assertEquals(center2, nearestCluster.getCenter());
    }

    private static Cluster<TestPoint> getNearestCluster(Collection<Cluster<TestPoint>> clusters, TestPoint point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<TestPoint> minCluster = null;
        for (Cluster<TestPoint> c : clusters) {
            final double distance = point.distanceFrom(c.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = c;
            }
        }
        return minCluster;
    }

    @Test
    public void testChooseInitialCenters() {
        Collection<Cluster<TestPoint>> result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).chooseInitialCenters(Arrays.asList(getTestPoints()), k, new Random());
        assertEquals(k, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertEquals(1, cluster.getPoints().size());
            assertNotNull(cluster.getCenter());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        List<TestPoint> points = Arrays.asList(new TestPoint(1, 1), new TestPoint(2, 2), new TestPoint(3, 3), new TestPoint(4, 4), new TestPoint(5, 5));
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(i + 0.5, i + 0.5)));
        }
        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);
        for (Cluster<TestPoint> cluster : clusters) {
            for (TestPoint point : points) {
                if (cluster.getPoints().contains(point)) {
                    assertEquals(cluster.getCenter(), point, 0.001);
                }
            }
        }
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(i, i)));
        }
        TestPoint result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).getPointFromLargestVarianceCluster(clusters);
        assertNull(result);
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                assertNull(cluster.getCenter());
                result = cluster.getPoints().remove(new Random().nextInt(cluster.getPoints().size()));
                break;
            }
        }
        assertNotNull(result);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(i, i)));
        }
        TestPoint result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER).getPointFromLargestNumberCluster(clusters);
        assertNull(result);
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                result = cluster.getPoints().remove(new Random().nextInt(cluster.getPoints().size()));
                break;
            }
        }
        assertNotNull(result);
    }

    @Test
    public void testGetFarthestPoint() {
        Collection<Cluster<TestPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(i, i)));
        }
        TestPoint result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT).getFarthestPoint(clusters);
        assertNull(result);
        for (Cluster<TestPoint> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                result = cluster.getPoints().get(0);
                break;
            }
        }
        assertNotNull(result);

        Collection<Cluster<TestPoint>> newClusters = new ArrayList<>();
        for (Cluster<TestPoint> cluster : clusters) {
            newClusters.add(new Cluster<TestPoint>(new TestPoint(cluster.getCenter().getX() + 1, cluster.getCenter().getY() + 1)));
        }
        result = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT).getFarthestPoint(newClusters);
        assertNotNull(result);

        double[] center = new double[]{1.0, 1.0};
        OpenIntIntPair nearestPointIndex = new OpenIntIntPair(-1, -1);
        for (int i = 0; i < points.length; i++) {
            double distance = points[i][0] - center[0];
            distance *= distance;
            distance += (points[i][1] - center[1]) * (points[i][1] - center[1]);
            if (distance > Precision.sq(result.getDistanceFrom(center), 0.001)) {
                nearestPointIndex = new OpenIntIntPair(i, 0);
            }
        }
        assertEquals(nearestPointIndex.getIntPart1(), result.getIndex());
    }

    private static TestPoint getRandomPoint() {
        return new TestPoint(RandomUtils.nextDouble(10, 0), RandomUtils.nextDouble(10, 0));
    }

    static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double[] toArray() {
            return new double[]{x, y};
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            double dx = this.x - ((TestPoint) other).x;
            double dy = this.y - ((TestPoint) other).y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return "{x=" + x + ", y=" + y + '}';
        }

        @Override
        public int getIndex() {
            return 0;
        }
    }

}