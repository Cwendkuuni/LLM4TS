package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.factory.DistributionFactory;
import org.junit.Test;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    @Test
    public void testCluster() {
        Random random = new Random();

        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5),
                new TestPoint(3.0, 3.0),
                new TestPoint(3.5, 3.5)
        );

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);

        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertEquals(cluster.getPoints().size(), 1);
        }

        List<Cluster<TestPoint>> expectedClusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0)),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0)),
                new Cluster<TestPoint>(new TestPoint(3.0, 3.0))
        );

        assertEquals(expectedClusters, clusters);
    }

    @Test
    public void testEmptyClusterStrategy_LargestVariance() {
        Random random = new Random();

        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5)
        );

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);

        List<Cluster<TestPoint>> expectedClusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0)),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0), Arrays.asList(new TestPoint(2.5, 2.5))),
                new Cluster<TestPoint>( Collections.emptyList() )
        );

        assertEquals(expectedClusters, clusters);
    }

    @Test
    public void testEmptyClusterStrategy_LargestPointsNumber() {
        Random random = new Random();

        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(1.6, 1.6),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5)
        );

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);

        List<Cluster<TestPoint>> expectedClusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0), Arrays.asList(new TestPoint(1.5, 1.5), new TestPoint(1.6, 1.6))),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0)),
                new Cluster<TestPoint>(Collections.emptyList())
        );

        assertEquals(expectedClusters, clusters);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategy_Error() {
        Random random = new Random();

        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);

        List<TestPoint> points = Collections.emptyList();

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new Random();

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(2.0, 2.0),
                new TestPoint(3.0, 3.0)
        );

        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.5, 1.5)),
                new Cluster<TestPoint>(new TestPoint(2.5, 2.5)),
                new Cluster<TestPoint>(new TestPoint(3.5, 3.5))
        );

        TestPoint testPoint = new TestPoint(2.1, 2.1);

        Cluster<TestPoint> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, testPoint);

        assertEquals(clusters.get(1), nearestCluster);
    }

    @Test
    public void testGetFarthestPoint() {
        Random random = new Random();

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5),
                new TestPoint(3.0, 3.0),
                new TestPoint(3.5, 3.5)
        );

        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0)),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0), Arrays.asList(new TestPoint(2.5, 2.5))),
                new Cluster<TestPoint>(Arrays.asList(new TestPoint(3.0, 3.0), new TestPoint(3.5, 3.5)))
        );

        TestPoint farthestPoint = KMeansPlusPlusClusterer.getFarthestPoint(clusters);

        assertEquals(clusters.get(2).getPoints().get(0), farthestPoint);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Random random = new Random();

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(1.6, 1.6),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5)
        );

        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0), Arrays.asList(new TestPoint(1.5, 1.5), new TestPoint(1.6, 1.6))),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0)),
                new Cluster<TestPoint>(Arrays.asList(new TestPoint(2.5, 2.5)))
        );

        TestPoint pointFromLargestNumberCluster = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);

        assertEquals(clusters.get(2).getPoints().get(0), pointFromLargestNumberCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Random random = new Random();

        List<TestPoint> points = Arrays.asList(
                new TestPoint(1.0, 1.0),
                new TestPoint(1.5, 1.5),
                new TestPoint(1.6, 1.6),
                new TestPoint(2.0, 2.0),
                new TestPoint(2.5, 2.5)
        );

        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0), Arrays.asList(new TestPoint(1.5, 1.5), new TestPoint(1.6, 1.6))),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0)),
                new Cluster<TestPoint>(Arrays.asList(new TestPoint(2.5, 2.5)))
        );

        TestPoint pointFromLargestVarianceCluster = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);

        assertEquals(clusters.get(1).getPoints().get(0), pointFromLargestVarianceCluster);
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<TestPoint> other) {
            TestPoint otherPoint = (TestPoint) other;
            NormalDistribution distribution = DistributionFactory.getNormalDistribution(0.0, 1.0);
            double dx = Math.abs(x - otherPoint.x);
            double dy = Math.abs(y - otherPoint.y);
            double distance = Math.sqrt(dx * dx + dy * dy);
            return distance + distribution.inverseCumulativeProbability(0.01);
        }

        @Override
        public Cluster<TestPoint> getCluster() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setCluster(Cluster<TestPoint> cluster) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Cluster<TestPoint> getCenter() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setCenter(Cluster<TestPoint> center) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Clusterable<TestPoint>> getPoints() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPoints(List<Clusterable<TestPoint>> points) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return Double.hashCode(x) + Double.hashCode(y);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestPoint)) {
                return false;
            }
            TestPoint point = (TestPoint) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public String toString() {
            return "TestPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}