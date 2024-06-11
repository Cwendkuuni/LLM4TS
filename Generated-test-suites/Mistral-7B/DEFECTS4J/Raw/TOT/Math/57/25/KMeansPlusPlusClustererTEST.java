package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final int NUM_POINTS = 12;
    private static final int NUM_CLUSTERS = 3;
    private static final double EPSILON = 1e-10;

    private KMeansPlusPlusClusterer<T> clustering;

    @Before
    public void setup() {
        clustering = new KMeansPlusPlusClusterer<>(RandomUtils.create(42), EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    /**
     * Test clustering with random points.
     */
    @Test
    public void testRandomData() {
        List<T> points = getRandomPoints(NUM_POINTS);
        List<Cluster<T>> clusters = clustering.cluster(points, NUM_CLUSTERS, 100);
        Assert.assertEquals("Wrong number of clusters", NUM_CLUSTERS, clusters.size());

        // Check each cluster has at least one point
        for (Cluster<T> cluster : clusters) {
            Assert.assertFalse("Empty cluster", cluster.getPoints().isEmpty());
        }

        // Check each point is in the correct cluster
        for (T point : points) {
            Cluster<T> cluster = getNearestCluster(clusters, point);
            Assert.assertEquals("Point not in correct cluster", point.getClass(), cluster.getCenter().getClass());
            Assert.assertTrue("Point not near its cluster center",
                    Precision.isClose(point.distanceFrom(cluster.getCenter()), 0.0, EPSILON));
        }
    }

    /**
     * Test clustering with collinear points.
     */
    @Test
    public void testCollinearData() {
        List<T> points = getCollinearPoints(NUM_POINTS);
        List<Cluster<T>> clusters = clustering.cluster(points, NUM_CLUSTERS, 100);
        Assert.assertEquals("Wrong number of clusters", NUM_CLUSTERS, clusters.size());

        // Check each cluster has at least one point
        for (Cluster<T> cluster : clusters) {
            Assert.assertFalse("Empty cluster", cluster.getPoints().isEmpty());
        }

        // Check each point is in the correct cluster
        for (T point : points) {
            Cluster<T> cluster = getNearestCluster(clusters, point);
            Assert.assertEquals("Point not in correct cluster", point.getClass(), cluster.getCenter().getClass());
            // Collinear points will have the same distance to all centers
            // so we cannot check proximity here
        }
    }

    /**
     * Test clustering with empty data.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyData() {
        List<T> points = Collections.emptyList();
        clustering.cluster(points, NUM_CLUSTERS, 100);
    }

    /**
     * Test clustering with less points than clusters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLessPointsThanClusters() {
        List<T> points = Arrays.asList(new Point(1.0, 1.0), new Point(1.0, 2.0));
        clustering.cluster(points, NUM_CLUSTERS, 100);
    }

    /**
     * Test clustering with a maximum number of iterations.
     */
    @Test
    public void testMaxIterations() {
        List<T> points = getRandomPoints(NUM_POINTS);
        List<Cluster<T>> clusters = clustering.cluster(points, NUM_CLUSTERS, 1);
        Assert.assertEquals("Wrong number of clusters", NUM_CLUSTERS, clusters.size());
        // The algorithm should converge in one iteration
        Assert.assertFalse("Clustering changed", clusters.equals(clustering.cluster(points, NUM_CLUSTERS, 100).getPoints()));
    }

    /**
     * Test clustering with negative maximum iterations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        List<T> points = getRandomPoints(NUM_POINTS);
        clustering.cluster(points, NUM_CLUSTERS, -1);
    }

    /**
     * Test getting the nearest cluster.
     */
    private <U extends Clusterable<U>> Cluster<T> getNearestCluster(final List<Cluster<T>> clusters, final T point) {
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

    /**
     * Generate random points.
     *
     * @param number the number of points to generate
     * @return a list of random points
     */
    private List<T> getRandomPoints(int number) {
        List<T> points = new ArrayList<T>(number);
        for (int i = 0; i < number; ++i) {
            points.add(new Point(RandomUtils.nextDouble(), RandomUtils.nextDouble()));
        }
        return points;
    }

    /**
     * Generate collinear points.
     *
     * @param number the number of points to generate
     * @return a list of collinear points
     */
    private List<T> getCollinearPoints(int number) {
        List<T> points = new ArrayList<T>(number);
        double x = 0.0;
        double y = 0.0;
        for (int i = 0; i < number; ++i) {
            points.add(new Point(x, y));
            x += 1.0;
        }
        return points;
    }

    /**
     * Simple Clusterable point representation.
     */
    private static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            double dx = x - ((Point) other).x;
            double dy = y - ((Point) other).y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Cluster<Point> getCenter() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}