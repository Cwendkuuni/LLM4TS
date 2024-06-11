package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.Clusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.point.Point2D;

public class KMeansPlusPlusClustererTest {

    /**
     * Test KMeansPlusPlusClusterer with synthetic data.
     */
    @Test
    public void testSyntheticData() {
        // Given
        final int n = 100;
        final int k = 3;
        final double epsilon = 0.01;
        final double minDistance = 0.1;
        final double maxDistance = 10.0;
        final NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
        final Random random = new Random();
        final List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final double x = distribution.sample();
            final double y = distribution.sample();
            points.add(new Point2D(x, y));
        }

        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Cluster<Point2D>> clusters = clusterer.cluster(points, k, 100);

        // When

        // Check that the number of clusters is correct
        assertEquals(k, clusters.size());

        // Check that each cluster is not empty
        for (final Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check that the clusters are well separated
        for (int i = 0; i < clusters.size(); i++) {
            final Cluster<Point2D> clusterI = clusters.get(i);
            for (int j = i + 1; j < clusters.size(); j++) {
                final Cluster<Point2D> clusterJ = clusters.get(j);
                final double distance = clusterI.getCenter().distanceFrom(clusterJ.getCenter());
                assertTrue(distance > minDistance);
            }
        }

        // Check that the clusters have a reasonable size
        final double sizeSum = 0.0;
        for (final Cluster<Point2D> cluster : clusters) {
            final double size = cluster.getPoints().size();
            assertTrue(size > 0 && size <= n / k);
            sizeSum += size;
        }
        assertTrue(sizeSum > 0 && sizeSum <= n);

        // Check that the centroid of each cluster is a good representative of the cluster
        for (int i = 0; i < clusters.size(); i++) {
            final Cluster<Point2D> cluster = clusters.get(i);
            final Variance variance = new Variance();
            for (final Clusterable<Point2D> point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            final double varianceValue = Precision.round(variance.getResult(), 3);
            final double maxExpectedVariance = Math.max(maxDistance * maxDistance, epsilon);
            assertTrue(varianceValue <= maxExpectedVariance);
        }

        // Check that updating the centers does not change the cluster assignment
        final List<Cluster<Point2D>> newClusters = new ArrayList<>();
        for (final Cluster<Point2D> cluster : clusters) {
            final Point2D newCenter = new Point2D(cluster.getCenter().getX() + epsilon, cluster.getCenter().getY() + epsilon);
            newClusters.add(new Cluster<Point2D>(newCenter));
        }
        final List<Cluster<Point2D>> newClustersAssigned = new ArrayList<>();
        assignPointsToClusters(newClusters, points);
        for (int i = 0; i < newClusters.size(); i++) {
            final Cluster<Point2D> newCluster = newClusters.get(i);
            final Cluster<Point2D> originalCluster = clusters.get(i);
            assertEquals(originalCluster, newCluster);
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with random data.
     */
    @Test
    public void testRandomData() {
        // Given
        final int n = 1000;
        final int k = 10;
        final double epsilon = 0.01;
        final double minDistance = 0.1;
        final double maxDistance = 10.0;
        final Random random = new Random();
        final List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final double x = RandomUtils.nextGaussian(0.0, 1.0);
            final double y = RandomUtils.nextGaussian(0.0, 1.0);
            points.add(new Point2D(x, y));
        }

        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Cluster<Point2D>> clusters = clusterer.cluster(points, k, 100);

        // When

        // Check that the number of clusters is correct
        assertEquals(k, clusters.size());

        // Check that each cluster is not empty
        for (final Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check that the clusters are well separated
        for (int i = 0; i < clusters.size(); i++) {
            final Cluster<Point2D> clusterI = clusters.get(i);
            for (int j = i + 1; j < clusters.size(); j++) {
                final Cluster<Point2D> clusterJ = clusters.get(j);
                final double distance = clusterI.getCenter().distanceFrom(clusterJ.getCenter());
                assertTrue(distance > minDistance);
            }
        }

        // Check that the clusters have a reasonable size
        final double sizeSum = 0.0;
        for (final Cluster<Point2D> cluster : clusters) {
            final double size = cluster.getPoints().size();
            assertTrue(size > 0 && size <= n / k);
            sizeSum += size;
        }
        assertTrue(sizeSum > 0 && sizeSum <= n);

        // Check that the centroid of each cluster is a good representative of the cluster
        for (int i = 0; i < clusters.size(); i++) {
            final Cluster<Point2D> cluster = clusters.get(i);
            final Variance variance = new Variance();
            for (final Clusterable<Point2D> point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            final double varianceValue = Precision.round(variance.getResult(), 3);
            final double maxExpectedVariance = Math.max(maxDistance * maxDistance, epsilon);
            assertTrue(varianceValue <= maxExpectedVariance);
        }

        // Check that updating the centers does not change the cluster assignment
        final List<Cluster<Point2D>> newClusters = new ArrayList<>();
        for (final Cluster<Point2D> cluster : clusters) {
            final Point2D newCenter = new Point2D(cluster.getCenter().getX() + epsilon, cluster.getCenter().getY() + epsilon);
            newClusters.add(new Cluster<Point2D>(newCenter));
        }
        final List<Cluster<Point2D>> newClustersAssigned = new ArrayList<>();
        assignPointsToClusters(newClusters, points);
        for (int i = 0; i < newClusters.size(); i++) {
            final Cluster<Point2D> newCluster = newClusters.get(i);
            final Cluster<Point2D> originalCluster = clusters.get(i);
            assertEquals(originalCluster, newCluster);
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with a very small epsilon value.
     */
    @Test(expected = ConvergenceException.class)
    public void testSmallEpsilon() {
        // Given
        final int n = 100;
        final int k = 3;
        final double epsilon = 0.0;
        final double minDistance = 0.1;
        final double maxDistance = 10.0;
        final NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
        final Random random = new Random();
        final List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final double x = distribution.sample();
            final double y = distribution.sample();
            points.add(new Point2D(x, y));
        }

        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        try {
            clusterer.cluster(points, k, 100);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with a very large number of clusters.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testLargeNumberOfClusters() {
        // Given
        final int n = 100;
        final int k = Integer.MAX_VALUE;
        final double epsilon = 0.1;
        final double minDistance = 0.1;
        final double maxDistance = 10.0;
        final NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
        final Random random = new Random();
        final List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final double x = distribution.sample();
            final double y = distribution.sample();
            points.add(new Point2D(x, y));
        }

        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        try {
            clusterer.cluster(points, k, 100);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        final Random random = null;
        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        try {
            clusterer.cluster(Collections.emptyList(), 10, 100);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with a negative number of iterations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIterations() {
        final Random random = new Random();
        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        try {
            clusterer.cluster(Collections.emptyList(), 10, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test KMeansPlusPlusClusterer with a negative number of clusters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeClusters() {
        final Random random = new Random();
        final Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        try {
            clusterer.cluster(Collections.emptyList(), -1, 100);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Assigns each point to the nearest cluster.
     *
     * @param <T> type of the points to cluster
     * @param clusters the {@link Cluster}s to assign the points to
     * @param points the points to assign to the given {@link Cluster}s
     */
    private static <T extends Clusterable<T>> void assignPointsToClusters(final Collection<Cluster<T>> clusters, final Collection<T> points) {
        for (final T p : points) {
            Cluster<T> cluster = getNearestCluster(clusters, p);
            cluster.addPoint(p);
        }
    }

    /**
     * Returns the nearest {@link Cluster} to the given point.
     *
     * @param <T> type of the points to cluster
     * @param clusters the {@link Cluster}s to search
     * @param point the point to find the nearest {@link Cluster} for
     * @return the nearest {@link Cluster} to the given point
     */
    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(final Collection<Cluster<T>> clusters, final T point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<T> minCluster = null;
        for (final Cluster<T> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }
}