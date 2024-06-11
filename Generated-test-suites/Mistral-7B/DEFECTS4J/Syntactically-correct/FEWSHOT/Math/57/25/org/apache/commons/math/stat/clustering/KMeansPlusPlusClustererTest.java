package org.apache.commons.cli;

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @SuppressWarnings("unchecked")
    private static <T extends Clusterable<T>> void testClustering(Random random, int numPoints, int numClusters,
            int maxIterations) {
        List<Double> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add((double) (Math.random() * 100));
        }
        Clusterer<Double> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> result = clustering.cluster(points, numClusters, maxIterations);
        assertEquals(numClusters, result.size());

        for (Cluster<Double> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertTrue(cluster.getPoints().size() > 0);
        }

        Cluster<Double> center = result.get(0).getCenter();
        for (Cluster<Double> cluster : result) {
            assertFalse(cluster.getCenter().equals(center));
            for (Double point : cluster.getPoints()) {
                assertTrue(cluster.getCenter().distanceFrom(point) < 1.5 * center.distanceFrom(point));
            }
        }

        double varianceSum = 0.0;
        for (Cluster<Double> cluster : result) {
            Variance stat = new Variance();
            for (Double point : cluster.getPoints()) {
                stat.increment(point.distanceFrom(cluster.getCenter()));
            }
            double variance = stat.getResult();
            varianceSum += variance;
            assertEquals("Cluster " + cluster.getIndex() + " variance: " + variance,
                         variance, cluster.getStats().getMeanDistanceFromClusterCenter(), 0.001);
        }
        assertEquals("Total variance: " + varianceSum, varianceSum, clustering.getTotalVariance(points), 0.001);
    }

    @Test
    public void testClusterEmpty() {
        List<Double> points = Arrays.asList();
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        try {
            clustering.cluster(points, 1, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testCluster1Point() {
        List<Double> points = Arrays.asList(1.0);
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> result = clustering.cluster(points, 1, 10);
        assertEquals(1, result.size());
        Cluster<Double> cluster = result.get(0);
        assertNotNull(cluster.getCenter());
        assertEquals(1.0, cluster.getCenter().getX(), 0.0);
        assertTrue(cluster.getPoints().size() == 1);
        assertEquals(1.0, cluster.getPoints().get(0).getX(), 0.0);
    }

    @Test
    public void testCluster2Points() {
        List<Double> points = Arrays.asList(1.0, 2.0);
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> result = clustering.cluster(points, 1, 10);
        assertEquals(1, result.size());
        Cluster<Double> cluster = result.get(0);
        assertNotNull(cluster.getCenter());
        double[] centers = new double[] { (1.0 + 2.0) / 2.0 };
        assertArrayEquals(centers, new double[] { cluster.getCenter().getX() }, 0.001);
        assertTrue(cluster.getPoints().size() == 2);
        List<Double> expectedPoints = Arrays.asList(1.0, 2.0);
        assertEquals(expectedPoints, cluster.getPoints());
    }

    @Test
    public void testCluster3Points() {
        List<Double> points = Arrays.asList(1.0, 2.0, 3.0);
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> result = clustering.cluster(points, 2, 10);
        assertEquals(2, result.size());

        for (Cluster<Double> cluster : result) {
            assertNotNull(cluster.getCenter());
            List<Double> clusterPoints = new ArrayList<>();
            if (cluster.getIndex() == 0) {
                clusterPoints.add(1.0);
                clusterPoints.add(2.0);
                assertEquals(clusterPoints, cluster.getPoints());
            } else {
                clusterPoints.add(3.0);
                assertEquals(clusterPoints, cluster.getPoints());
            }
        }

        double[] centers = new double[] { (1.0 + 2.0) / 2.0, 3.0 };
        for (Cluster<Double> cluster : result) {
            assertArrayEquals(centers, new double[] { cluster.getCenter().getX() }, 0.001);
        }
    }

    @Test
    public void testClusterRandomPoints() {
        int numPoints = 100;
        int numClusters = 5;
        int maxIterations = 10;
        testClustering(new Random(), numPoints, numClusters, maxIterations);
    }

    @Test
    public void testClusterLargePoints() {
        int numPoints = 10000;
        int numClusters = 5;
        int maxIterations = 10;
        testClustering(new Random(), numPoints, numClusters, maxIterations);
    }

    @Test
    public void testClusterMaxIterations() {
        int numPoints = 10;
        int numClusters = 2;
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> result = clustering.cluster(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0), numClusters, -1);
        assertEquals(numClusters, result.size());
        for (Cluster<Double> cluster : result) {
            assertNotNull(cluster.getCenter());
        }
    }

    @Test
    public void testClusterEmptyStrategy() {
        List<Double> points = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        KMeansPlusPlusClusterer<Double> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Double>> result = clustering.cluster(points, 2, 10);
        assertEquals(2, result.size());
        for (Cluster<Double> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertTrue(cluster.getPoints().size() > 0);
        }

        List<Double> points2 = Arrays.asList(1.0, 2.0, 3.0);
        KMeansPlusPlusClusterer<Double> clustering2 = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Double>> result2 = clustering2.cluster(points2, 2, 10);
        assertEquals(2, result2.size());
        for (Cluster<Double> cluster : result2) {
            assertNotNull(cluster.getCenter());
            assertTrue(cluster.getPoints().size() > 0);
        }

        List<Double> points3 = Arrays.asList(1.0, 2.0, 3.0);
        KMeansPlusPlusClusterer<Double> clustering3 = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        try {
            List<Cluster<Double>> result3 = clustering3.cluster(points3, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}