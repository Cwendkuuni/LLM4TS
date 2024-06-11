import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.descriptive.moment.Variance;
import org.apache.commons.math3.util.random.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-6;

    private double[][] points;
    private int k;
    private KMeansPlusPlusClusterer<MyPoint> clustering;

    public KMeansPlusPlusClustererTest(double[][] points, int k) {
        this.points = points;
        this.k = k;
    }

    @Parameterized.Parameters(name = "{index}: points = {0}, k = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[][]{{2.0, 3.0}, {1.0, 1.0}, {5.0, 5.0}, {3.0, 3.0}, {1.0, 3.0}}, 3},
                {new double[][]{{0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {4.0, 4.0}}, 5},
                {new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}}, 6}
        });
    }

    @Test
    public void testCluster() {
        clustering = new KMeansPlusPlusClusterer<>(RandomUtils.create(42));
        List<Cluster<MyPoint>> result = clustering.cluster(getPoints(), k, 10);
        assertEquals(k, result.size());
        for (Cluster<MyPoint> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClusterEmptyPoints() {
        clustering = new KMeansPlusPlusClusterer<>(RandomUtils.create(42));
        clustering.cluster(new ArrayList<>(), k, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClusterNegativeK() {
        clustering = new KMeansPlusPlusClusterer<>(RandomUtils.create(42));
        clustering.cluster(getPoints(), -1, 10);
    }

    @Test
    public void testChooseInitialCenters() {
        Cluster<MyPoint> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(getPoints(), k, RandomUtils.create(42));
        assertEquals(k, initialCenters.size());
        for (Cluster<MyPoint> cluster : initialCenters) {
            assertNotNull(cluster.getCenter());
            assertTrue(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<>(new MyPoint(i, i)));
        }
        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, getPoints());
        for (Cluster<MyPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<>(new MyPoint(i, i)));
        }
        MyPoint point = new MyPoint(1.5, 2.0);
        Cluster<MyPoint> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
        assertEquals(getClusterIndex(point), nearestCluster.getIndex());
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<>(new MyPoint(i, i)));
        }
        MyPoint farthestPoint = KMeansPlusPlusClusterer.getFarthestPoint(clusters);
        assertNotNull(farthestPoint);
        for (Cluster<MyPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                MyPoint clusterPoint = cluster.getPoints().get(0);
                assertEquals(getClusterIndex(clusterPoint), getClusterIndex(farthestPoint));
                assertTrue(Precision.compareDoubles(clusterPoint.distanceFrom(cluster.getCenter()), farthestPoint.distanceFrom(cluster.getCenter()), EPSILON) > 0);
            }
        }
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<>(new MyPoint(i, i)));
        }
        MyPoint pointFromLargestNumberCluster = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);
        assertNotNull(pointFromLargestNumberCluster);
        for (Cluster<MyPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                int size = cluster.getPoints().size();
                assertEquals(getClusterIndex(pointFromLargestNumberCluster), getClusterIndex(cluster.getPoints().get(size - 1)));
            }
        }
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<>(new MyPoint(i, i)));
        }
        MyPoint pointFromLargestVarianceCluster = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);
        assertNotNull(pointFromLargestVarianceCluster);
        for (Cluster<MyPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                Variance variance = new Variance();
                for (MyPoint point : cluster.getPoints()) {
                    variance.increment(point.distanceFrom(cluster.getCenter()));
                }
                double varianceValue = variance.getResult();
                double largestVariance = Double.MAX_VALUE;
                int largestVarianceIndex = -1;
                for (int i = 0; i < clusters.size(); i++) {
                    if (clusters.get(i).getPoints().size() > 0) {
                        Variance clusterVariance = new Variance();
                        for (MyPoint point : clusters.get(i).getPoints()) {
                            clusterVariance.increment(point.distanceFrom(clusters.get(i).getCenter()));
                        }
                        double currentVariance = clusterVariance.getResult();
                        if (currentVariance < largestVariance) {
                            largestVariance = currentVariance;
                            largestVarianceIndex = i;
                        }
                    }
                }
                assertTrue(Precision.compareDoubles(varianceValue, largestVariance, EPSILON) > 0);
                assertEquals(getClusterIndex(pointFromLargestVarianceCluster), largestVarianceIndex);
            }
        }
    }

    private List<MyPoint> getPoints() {
        List<MyPoint> points = new ArrayList<>();
        for (double[] point : points) {
            points.add(new MyPoint(point[0], point[1]));
        }
        return points;
    }

    private int getClusterIndex(MyPoint point) {
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).getCenter().equals(point)) {
                return i;
            }
        }
        return -1;
    }

    private static class MyPoint implements Clusterable<MyPoint> {

        private final double x;
        private final double y;

        public MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<MyPoint> other) {
            if (other instanceof MyPoint) {
                MyPoint point = (MyPoint) other;
                return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Cluster<MyPoint> getCenter() {
            return new Cluster<MyPoint>(this);
        }

    }

}