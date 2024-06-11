package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 * Test class for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    /** Random generator for test cases. */
    private static final Random RANDOM = new Random();

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}.
     */
    @org.junit.Test
    public void testCluster() {
        final int pointsNumber = 20;
        final int dimensions = 2;
        final int k = 3;
        final int maxIterations = 10;

        // Prepare test data
        final List<Clusterable<Double>> testData = new ArrayList<>();
        for (int i = 0; i < pointsNumber; i++) {
            final double x = new UniformRealDistribution(i * 0.1, i * 0.2).sample();
            final double y = new UniformRealDistribution((i + 5) * 0.1, (i + 5) * 0.2).sample();
            testData.add(new Point(x, y));
        }

        // Run the test
        final List<Cluster<Clusterable<Double>>> result = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE)
                .cluster(testData, k, maxIterations);

        // Check results
        assertNotNull(result);
        assertEquals(k, result.size());
        for (final Cluster<Clusterable<Double>> cluster : result) {
            assertNotNull(cluster);
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)}.
     */
    @org.junit.Test
    public void testGetNearestCluster() {
        final int pointsNumber = 5;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Clusterable<Double>> testData = new ArrayList<>();
        for (int i = 0; i < pointsNumber; i++) {
            final double x = new UniformRealDistribution(i * 0.1, i * 0.2).sample();
            final double y = new UniformRealDistribution((i + 1) * 0.1, (i + 1) * 0.2).sample();
            testData.add(new Point(x, y));
        }

        final List<Cluster<Clusterable<Double>>> result = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE)
                .cluster(testData, k, 1);

        // Prepare test point
        final double xTest = new UniformRealDistribution(3.5 * 0.1, 3.5 * 0.2).sample();
        final double yTest = new UniformRealDistribution(3.6 * 0.1, 3.6 * 0.2).sample();
        final Point testPoint = new Point(xTest, yTest);

        // Run the test
        final Cluster<Clusterable<Double>> nearestCluster = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE)
                .getNearestCluster(result, testPoint);

        // Check results
        assertNotNull(nearestCluster);
        assertEquals(result.size(), nearestCluster.getIndex());
        final double expectedDistance = testPoint.distanceFrom(new Point(nearestCluster.getCenter()));
        final double actualDistance = testPoint.distanceFrom(result.get(nearestCluster.getIndex()).getCenter());
        assertEquals(expectedDistance, actualDistance, 0.001);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#chooseInitialCenters(Collection, int, Random)}.
     */
    @org.junit.Test
    public void testChooseInitialCenters() {
        final int pointsNumber = 5;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Clusterable<Double>> testData = new ArrayList<>();
        for (int i = 0; i < pointsNumber; i++) {
            final double x = new UniformRealDistribution(i * 0.1, i * 0.2).sample();
            final double y = new UniformRealDistribution((i + 1) * 0.1, (i + 1) * 0.2).sample();
            testData.add(new Point(x, y));
        }

        // Run the test
        final List<Cluster<Clusterable<Double>>> initialCenters = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE)
                .chooseInitialCenters(testData, k, RANDOM);

        // Check results
        assertNotNull(initialCenters);
        assertEquals(k, initialCenters.size());
        for (final Cluster<Clusterable<Double>> cluster : initialCenters) {
            assertNotNull(cluster);
            assertTrue(cluster.getPoints().isEmpty());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#assignPointsToClusters(Collection, Collection)}.
     */
    @org.junit.Test
    public void testAssignPointsToClusters() {
        final int pointsNumber = 5;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Clusterable<Double>> testData = new ArrayList<>();
        for (int i = 0; i < pointsNumber; i++) {
            final double x = new UniformRealDistribution(i * 0.1, i * 0.2).sample();
            final double y = new UniformRealDistribution((i + 1) * 0.1, (i + 1) * 0.2).sample();
            testData.add(new Point(x, y));
        }

        final List<Cluster<Clusterable<Double>>> initialCenters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            initialCenters.add(new Cluster<Clusterable<Double>>(new Point(i * 0.1, i * 0.2)));
        }

        // Run the test
        new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE)
                .assignPointsToClusters(initialCenters, testData);

        // Check results
        for (final Cluster<Clusterable<Double>> cluster : initialCenters) {
            assertFalse(cluster.getPoints().isEmpty());
        }

        for (final Clusterable<Double> point : testData) {
            boolean found = false;
            for (final Cluster<Clusterable<Double>> cluster : initialCenters) {
                if (cluster.getPoints().contains(point)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestVarianceCluster(Collection)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestVarianceCluster_emptyCluster() {
        final int pointsNumber = 0;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Cluster<Clusterable<Double>>> testData = new ArrayList<>();

        // Run the test
        new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE).getPointFromLargestVarianceCluster(testData);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestNumberCluster(Collection)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestNumberCluster_emptyCluster() {
        final int pointsNumber = 0;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Cluster<Clusterable<Double>>> testData = new ArrayList<>();

        // Run the test
        new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER).getPointFromLargestNumberCluster(testData);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getFarthestPoint(Collection)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testGetFarthestPoint_emptyCluster() {
        final int pointsNumber = 0;
        final int dimensions = 2;
        final int k = 3;

        // Prepare test data
        final List<Cluster<Clusterable<Double>>> testData = new ArrayList<>();

        // Run the test
        new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT).getFarthestPoint(testData);
    }

    /**
     * Test class for {@link Point}.
     */
    private static class Point implements Clusterable<Point> {

        private final double x;
        private final double y;

        /**
         * Constructor.
         *
         * @param x x-coordinate
         * @param y y-coordinate
         */
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<Point> other) {
            if (other instanceof Point) {
                final Point otherPoint = (Point) other;
                return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }

    }

}