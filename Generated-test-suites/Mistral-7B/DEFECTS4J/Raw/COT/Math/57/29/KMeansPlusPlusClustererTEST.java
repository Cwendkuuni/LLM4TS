import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private KMeansPlusPlusClusterer<T> clusteringAlgorithm;
    private T point1;
    private T point2;
    private T point3;
    private T point4;
    private T point5;
    private T point6;
    private List<T> points;

    public KMeansPlusPlusClustererTest(T point1, T point2, T point3, T point4, T point5, T point6) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.point5 = point5;
        this.point6 = point6;
        this.points = Arrays.asList(point1, point2, point3, point4, point5, point6);
        this.clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Parameterized.Parameters(name = "{index}: given {0}, {1}, {2}, {3}, {4}, {5}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new PointWithDistance(new Point(1.0), new Point(1.0), 0.0),
                new PointWithDistance(new Point(1.0), new Point(2.0), 1.0),
                new PointWithDistance(new Point(2.0), new Point(1.0), 1.0),
                new PointWithDistance(new Point(2.0), new Point(2.0), 0.0),
                new PointWithDistance(new Point(3.0), new Point(2.0), 1.5),
                new PointWithDistance(new Point(2.0), new Point(3.0), 1.5)
        });
    }

    @Test
    public void testChooseInitialCenters() {
        List<Cluster<T>> initialCenters = clusteringAlgorithm.chooseInitialCenters(points, 2, new Random());
        assertEquals(2, initialCenters.size());
        for (Cluster<T> cluster : initialCenters) {
            assertNotNull(cluster.getCenter());
        }
    }

    @Test
    public void testCluster() {
        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
        for (T point : points) {
            Cluster<T> cluster = getNearestCluster(clusters, point);
            assertEquals(point, cluster.getCenter());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusters() {
        List<T> emptyPoints = Collections.emptyList();
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        try {
            List<Cluster<T>> clusters = clusteringAlgorithm.cluster(emptyPoints, 2, 10);
            fail("Should have thrown a ConvergenceException");
        } catch (ConvergenceException ce) {
            // expected
        }
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, 2, 10);
        Cluster<T> cluster1 = getNearestCluster(clusters, point1);
        assertEquals(point1, cluster1.getCenter());
        Cluster<T> cluster2 = getNearestCluster(clusters, point2);
        assertEquals(point2, cluster2.getCenter());
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<T>> clusters = Arrays.asList(
                new Cluster<T>(new Point(1.0)),
                new Cluster<T>(new Point(2.0), Arrays.asList(point1, point2, point3)),
                new Cluster<T>(new Point(3.0)),
                new Cluster<T>(new Point(4.0), Arrays.asList(point4, point5, point6))
        );
        T point = clusteringAlgorithm.getPointFromLargestNumberCluster(clusters);
        assertEquals(point4, point);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<T>> clusters = Arrays.asList(
                new Cluster<T>(new Point(1.0), Arrays.asList(point1, point2, point3)),
                new Cluster<T>(new Point(2.0), Arrays.asList(point4, point5, point6)),
                new Cluster<T>(new Point(3.0))
        );
        Variance variance = new Variance();
        variance.increment(point1.distanceFrom(clusters.get(0).getCenter()));
        variance.increment(point2.distanceFrom(clusters.get(0).getCenter()));
        variance.increment(point3.distanceFrom(clusters.get(0).getCenter()));
        double varianceResult = variance.getResult();
        T point = clusteringAlgorithm.getPointFromLargestVarianceCluster(clusters);
        assertEquals(point5, point);
        assertTrue(varianceResult >clusteringAlgorithm.getPointFromLargestVarianceCluster(clusters).distanceFrom(clusters.get(1).getCenter()));
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<T>> clusters = Arrays.asList(
                new Cluster<T>(new Point(1.0), Arrays.asList(point1, point2, point3)),
                new Cluster<T>(new Point(2.0), Arrays.asList(point4, point5, point6))
        );
        T point = clusteringAlgorithm.getFarthestPoint(clusters);
        assertEquals(point6, point);
    }

    private Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    private static class PointWithDistance {
        private final T point;
        private final double distance;

        PointWithDistance(T point, T referencePoint) {
            this.point = point;
            this.distance = point.distanceFrom(referencePoint);
        }

        PointWithDistance(T point, double distance) {
            this.point = point;
            this.distance = distance;
        }

        T getPoint() {
            return point;
        }

        double getDistance() {
            return distance;
        }
    }

    private static class Point implements Clusterable<Point> {
        private double value;

        Point(double value) {
            this.value = value;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            if (other instanceof Point) {
                Point otherPoint = (Point) other;
                return Math.abs(this.value - otherPoint.value);
            }
            throw new UnsupportedOperationException();
        }
    }

}