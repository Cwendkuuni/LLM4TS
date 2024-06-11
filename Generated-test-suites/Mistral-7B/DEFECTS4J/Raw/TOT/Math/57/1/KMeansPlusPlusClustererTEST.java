package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Point2D;

/**
 * Tests the {@link KMeansPlusPlusClusterer} class.
 */
public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 20;
    private static final int NUM_CLUSTERS = 3;
    private List<Point2D> points;
    private KMeansPlusPlusClusterer<Point2D> clustering;

    @Before
    public void setUp() {
        points = new ArrayList<Point2D>(NUM_POINTS);
        final NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new Point2D(distribution.sample(), distribution.sample()));
        }
        clustering = new KMeansPlusPlusClusterer<Point2D>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    /**
     * Test clustering when given a valid collection of points.
     */
    @Test
    public void testSimpleClustering() {
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS, 100);
        assertNotNull(result);
        assertEquals(NUM_CLUSTERS, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    /**
     * Test clustering when given an empty collection of points.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyCollection() {
        final List<Cluster<Point2D>> result = clustering.cluster(Collections.<Point2D>emptyList(), NUM_CLUSTERS, 100);
    }

    /**
     * Test clustering when given a collection of points that cannot be clustered.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        final List<Point2D> noConvergencePoints = new ArrayList<Point2D>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            noConvergencePoints.add(new Point2D(i, i));
        }
        final List<Cluster<Point2D>> result = clustering.cluster(noConvergencePoints, NUM_CLUSTERS, 1);
    }

    /**
     * Test clustering when given a collection of points with a single cluster.
     */
    @Test
    public void testSingleCluster() {
        final List<Point2D> singleClusterPoints = new ArrayList<Point2D>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            singleClusterPoints.add(new Point2D(i, i));
        }
        final List<Cluster<Point2D>> result = clustering.cluster(singleClusterPoints, NUM_CLUSTERS, 100);
        assertNotNull(result);
        assertEquals(1, result.size());
        final Cluster<Point2D> singleCluster = result.get(0);
        assertNotNull(singleCluster.getCenter());
        assertEquals(NUM_POINTS, singleCluster.getPoints().size());
    }

    /**
     * Test clustering when given a collection of points with a single empty cluster.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingleEmptyCluster() {
        final Point2D emptyPoint = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        final List<Point2D> emptyClusterPoints = new ArrayList<Point2D>(NUM_POINTS + 1);
        for (int i = 0; i < NUM_POINTS; i++) {
            emptyClusterPoints.add(new Point2D(i, i));
        }
        emptyClusterPoints.add(emptyPoint);
        final List<Cluster<Point2D>> result = clustering.cluster(emptyClusterPoints, NUM_CLUSTERS, 100);
    }

    /**
     * Test clustering when given a collection of points with a cluster that contains no points.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() {
        final List<Point2D> emptyClusterPoints = new ArrayList<Point2D>(NUM_POINTS + 1);
        for (int i = 0; i < NUM_POINTS; i++) {
            emptyClusterPoints.add(new Point2D(i, i));
        }
        final List<Cluster<Point2D>> result = clustering.cluster(emptyClusterPoints, NUM_CLUSTERS, 100);
        final Cluster<Point2D> emptyCluster = result.get(0);
        assertNotNull(emptyCluster);
        assertNull(emptyCluster.getCenter());
        assertTrue(emptyCluster.getPoints().isEmpty());
    }

    /**
     * Test clustering when given a collection of points with a large number of clusters.
     */
    @Test
    public void testManyClusters() {
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS * 2, 100);
        assertNotNull(result);
        assertTrue(result.size() >= NUM_CLUSTERS * 2);
    }

    /**
     * Test clustering when given a collection of points with a large number of iterations.
     */
    @Test
    public void testManyIterations() {
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS, 100000);
        assertNotNull(result);
        assertEquals(NUM_CLUSTERS, result.size());
    }

    /**
     * Test clustering when given a collection of points with a small number of iterations.
     */
    @Test
    public void testFewIterations() {
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS, 10);
        assertNotNull(result);
        assertEquals(NUM_CLUSTERS, result.size());
    }

    /**
     * Test that the initial centers are chosen randomly.
     */
    @Test
    public void testRandomInitialCenters() {
        final Random random = new Random();
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS, 10);
        final KMeansPlusPlusClusterer<Point2D> clustering2 = new KMeansPlusPlusClusterer<Point2D>(random, EmptyClusterStrategy.FARTHEST_POINT);
        final List<Cluster<Point2D>> result2 = clustering2.cluster(points, NUM_CLUSTERS, 10);

        for (int i = 0; i < NUM_CLUSTERS; i++) {
            final Cluster<Point2D> c1 = result.get(i);
            final Cluster<Point2D> c2 = result2.get(i);
            assertNotNull(c1);
            assertNotNull(c2);
            final double centerDistance = new EuclideanDistance().distance(c1.getCenter(), c2.getCenter());
            assertTrue(Precision.abs(centerDistance) > 0.0);
        }
    }

    /**
     * Test that the points are assigned to the nearest cluster.
     */
    @Test
    public void testNearestCluster() {
        final List<Cluster<Point2D>> result = clustering.cluster(points, NUM_CLUSTERS, 100);
        for (final Point2D point : points) {
            final Cluster<Point2D> cluster = getNearestCluster(result, point);
            assertTrue(cluster.getPoints().contains(point));
        }
    }

    /**
     * Get the nearest {@link Cluster} to the given point.
     *
     * @param clusters the {@link Cluster}s to search
     * @param point the point to find the nearest {@link Cluster} for
     * @return the nearest {@link Cluster} to the given point
     */
    private static Cluster<Point2D> getNearestCluster(final Collection<Cluster<Point2D>> clusters, final Point2D point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<Point2D> minCluster = null;
        for (Cluster<Point2D> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }

}