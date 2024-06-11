package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    @Test
    public void testCluster() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = generateRandomPoints(10, 2);
        int k = 3;
        int maxIterations = 10;

        // When
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, k, maxIterations);

        // Then
        assertEquals(k, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = Collections.singletonList(new TestPoint(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        int k = 2;
        int maxIterations = 1;

        // When
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, k, maxIterations);

        // Then
        assertEquals(2, clusters.size());
        Cluster<TestPoint> cluster1 = clusters.get(0);
        Cluster<TestPoint> cluster2 = clusters.get(1);
        assertTrue(cluster1.getPoints().isEmpty());
        assertTrue(cluster2.getPoints().contains(points.get(0)));
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<TestPoint> points = Collections.emptyList();
        int k = 2;
        int maxIterations = 1;

        // When
        clusterer.cluster(points, k, maxIterations);

        // Then
        // ConvergenceException should be thrown
    }

    @Test
    public void testGetNearestCluster() {
        // Given
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.0, 0.0)),
                new Cluster<TestPoint>(new TestPoint(1.0, 1.0)),
                new Cluster<TestPoint>(new TestPoint(2.0, 2.0)));
        TestPoint point = new TestPoint(1.5, 1.5);

        // When
        Cluster<TestPoint> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        // Then
        assertEquals(clusters.get(1), nearestCluster);
    }

    @Test
    public void testChooseInitialCenters() {
        // Given
        List<TestPoint> points = generateRandomPoints(10, 2);
        int k = 3;
        Random random = new Random();

        // When
        List<Cluster<TestPoint>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, k, random);

        // Then
        assertEquals(k, initialCenters.size());
        for (Cluster<TestPoint> initialCenter : initialCenters) {
            assertTrue(initialCenter.getPoints().isEmpty());
        }
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        // Given
        List<Cluster<TestPoint>> clusters = generateRandomClusters(3, 10, 2, new EuclideanDistance());

        // When
        T pointFromLargestVarianceCluster = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);

        // Then
        Cluster<TestPoint> largestVarianceCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                largestVarianceCluster = cluster;
                break;
            }
        }
        assertEquals(largestVarianceCluster.getPoints().get(0), pointFromLargestVarianceCluster);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        // Given
        List<Cluster<TestPoint>> clusters = generateRandomClusters(3, 10, 2, new EuclideanDistance());

        // When
        T pointFromLargestNumberCluster = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);

        // Then
        Cluster<TestPoint> largestNumberCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                largestNumberCluster = cluster;
                break;
            }
        }
        assertEquals(largestNumberCluster.getPoints().get(new Random().nextInt(largestNumberCluster.getPoints().size())), pointFromLargestNumberCluster);
    }

    @Test
    public void testGetFarthestPoint() {
        // Given
        List<TestPoint> points = generateRandomPoints(10, 2);
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(i, i)));
        }

        // When
        TestPoint farthestPoint = KMeansPlusPlusClusterer.getFarthestPoint(clusters);

        // Then
        TestPoint expectedFarthestPoint = null;
        for (Cluster<TestPoint> cluster : clusters) {
            List<TestPoint> clusterPoints = cluster.getPoints();
            if (clusterPoints.size() > 0) {
                expectedFarthestPoint = clusterPoints.get(clusterPoints.size() - 1);
                break;
            }
        }
        assertEquals(expectedFarthestPoint, farthestPoint);
    }

    private List<TestPoint> generateRandomPoints(int n, int d) {
        Random random = new Random();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            points.add(new TestPoint(random.nextDouble(), random.nextDouble()));
        }
        return points;
    }

    private List<Cluster<TestPoint>> generateRandomClusters(int k, int n, int d, DistanceMeasure distanceMeasure) {
        Random random = new Random();
        List<TestPoint> points = generateRandomPoints(n, d);
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<TestPoint>(new TestPoint(random.nextDouble(), random.nextDouble())));
        }
        for (int i = 0; i < n; i++) {
            Cluster<TestPoint> nearestCluster = null;
            double minDistance = Double.MAX_VALUE;
            for (Cluster<TestPoint> cluster : clusters) {
                double distance = distanceMeasure.computeDistance(points.get(i), cluster.getCenter());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCluster = cluster;
                }
            }
            nearestCluster.addPoint(points.get(i));
        }
        return clusters;
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
            return Math.sqrt(Math.pow(x - otherPoint.x, 2.0) + Math.pow(y - otherPoint.y, 2.0));
        }

        @Override
        public Cluster<TestPoint> getCenter() {
            return new Cluster<TestPoint>(new TestPoint(x, y));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
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