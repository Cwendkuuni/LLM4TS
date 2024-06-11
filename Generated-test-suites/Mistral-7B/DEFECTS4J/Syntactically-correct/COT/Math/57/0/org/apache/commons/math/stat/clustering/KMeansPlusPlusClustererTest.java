package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;

/**
 * Test class for KMeansPlusPlusClusterer class
 *
 * @author John Doe
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     */
    @org.junit.Test
    public void testCluster() {
        // initialize test data
        Random random = new Random();
        int k = 3;
        List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);

        // test with valid arguments
        List<Cluster<Point2D>> result = clusterer.cluster(points, k, 10);
        assertEquals(k, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertFalse(cluster.getPoints().isEmpty());
        }

        // test with empty points collection
        points.clear();
        try {
            clusterer.cluster(points, k, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // test with k > number of points
        try {
            clusterer.cluster(points, k + 1, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // test with negative maxIterations
        try {
            clusterer.cluster(points, k, -1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#chooseInitialCenters(Collection, int, Random)}
     */
    @org.junit.Test
    public void testChooseInitialCenters() {
        // initialize test data
        List<Clusterable<Point2D>> points = new ArrayList<>();
        int k = 3;
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);

        // test with valid arguments
        List<Cluster<Point2D>> result = clusterer.chooseInitialCenters(points, k, random);
        assertEquals(k, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertFalse(cluster.getPoints().isEmpty());
        }

        // test with empty points collection
        points.clear();
        try {
            clusterer.chooseInitialCenters(points, k, random);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // test with k > number of points
        try {
            clusterer.chooseInitialCenters(points, k + 1, random);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getFarthestPoint(Collection)}
     */
    @org.junit.Test
    public void testGetFarthestPoint() {
        // initialize test data
        List<Cluster<Point2D>> clusters = Arrays.asList(
                new Cluster<Point2D>(new Point2D(0.0, 0.0)),
                new Cluster<Point2D>(new Point2D(1.0, 1.0)),
                new Cluster<Point2D>(new Point2D(2.0, 2.0)),
                new Cluster<Point2D>(new Point2D(3.0, 3.0))
        );

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // test with valid arguments
        Point2D point = clusterer.getFarthestPoint(clusters);
        assertEquals(point.getX(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);
        assertEquals(point.getY(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);

        // test with empty clusters collection
        clusters.clear();
        try {
            clusterer.getFarthestPoint(clusters);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)}
     */
    @org.junit.Test
    public void testGetNearestCluster() {
        // initialize test data
        List<Cluster<Point2D>> clusters = Arrays.asList(
                new Cluster<Point2D>(new Point2D(0.0, 0.0)),
                new Cluster<Point2D>(new Point2D(1.0, 1.0)),
                new Cluster<Point2D>(new Point2D(2.0, 2.0)),
                new Cluster<Point2D>(new Point2D(3.0, 3.0))
        );

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // test with valid arguments
        Point2D point = new Point2D(1.5, 1.5);
        Cluster<Point2D> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertEquals(1, nearestCluster.getIndex());

        // test with empty clusters collection
        clusters.clear();
        try {
            clusterer.getNearestCluster(clusters, new Point2D(0.0, 0.0));
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#assignPointsToClusters(Collection, Collection)}
     */
    @org.junit.Test
    public void testAssignPointsToClusters() {
        // initialize test data
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        List<Clusterable<Point2D>> points = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            clusters.add(new Cluster<Point2D>(new Point2D(i, i)));
            points.add(new Point2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // test with valid arguments
        clusterer.assignPointsToClusters(clusters, points);
        for (Cluster<Point2D> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }

        // test with empty points collection
        points.clear();
        clusters = Arrays.asList(new Cluster<Point2D>(new Point2D(0.0, 0.0)));
        try {
            clusterer.assignPointsToClusters(clusters, points);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestNumberCluster(Collection)}
     */
    @org.junit.Test
    public void testGetPointFromLargestNumberCluster() {
        // initialize test data
        List<Cluster<Point2D>> clusters = Arrays.asList(
                new Cluster<Point2D>(new Point2D(0.0, 0.0), Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.1, 0.1))),
                new Cluster<Point2D>(new Point2D(1.0, 1.0), Arrays.asList(new Point2D(1.0, 1.0), new Point2D(1.1, 1.1))),
                new Cluster<Point2D>(new Point2D(2.0, 2.0), Arrays.asList(new Point2D(2.0, 2.0), new Point2D(2.1, 2.1))),
                new Cluster<Point2D>(new Point2D(3.0, 3.0), Arrays.asList(new Point2D(3.0, 3.0), new Point2D(3.1, 3.1)))
        );

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // test with valid arguments
        Point2D point = clusterer.getPointFromLargestNumberCluster(clusters);
        assertEquals(point.getX(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);
        assertEquals(point.getY(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);

        // test with empty clusters collection
        clusters.clear();
        try {
            clusterer.getPointFromLargestNumberCluster(clusters);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestVarianceCluster(Collection)}
     */
    @org.junit.Test
    public void testGetPointFromLargestVarianceCluster() {
        // initialize test data
        List<Cluster<Point2D>> clusters = Arrays.asList(
                new Cluster<Point2D>(new Point2D(0.0, 0.0), Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.1, 0.1))),
                new Cluster<Point2D>(new Point2D(1.0, 1.0), Arrays.asList(new Point2D(1.0, 1.0), new Point2D(1.1, 1.1))),
                new Cluster<Point2D>(new Point2D(2.0, 2.0), Arrays.asList(new Point2D(2.0, 2.0), new Point2D(2.1, 2.1))),
                new Cluster<Point2D>(new Point2D(3.0, 3.0), Arrays.asList(new Point2D(3.0, 3.0), new Point2D(3.1, 3.1)))
        );

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // test with valid arguments
        Variance variance = new Variance();
        Point2D point = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertEquals(point.getX(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);
        assertEquals(point.getY(), Math.max(Math.max(1.0, 2.0), 3.0), 0.001);

        // calculate variance for each cluster
        for (Cluster<Point2D> cluster : clusters) {
            variance.clear();
            for (Clusterable<Point2D> point2D : cluster.getPoints()) {
                variance.increment(point2D.distanceFrom(cluster.getCenter()));
            }
            double varianceValue = variance.getResult();
            // the cluster with the largest variance should be the first one
            if (varianceValue > clusters.get(0).getVariance()) {
                fail("Unexpected results");
            }
        }

        // test with empty clusters collection
        clusters.clear();
        try {
            clusterer.getPointFromLargestVarianceCluster(clusters);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

}