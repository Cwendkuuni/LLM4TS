package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test case for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test the clustering algorithm with 2 dimensions points.
     */
    @Test
    public void testCluster2D() {
        Random random = new Random();
        List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Double[] p = new Double[2];
            p[0] = random.nextGaussian();
            p[1] = random.nextGaussian();
            points.add(p);
        }

        List<Cluster<Double[]>> clusters = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE)
                .cluster(points, 3, 100);

        assertEquals(3, clusters.size());
        for (Cluster<Double[]> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }

        // Check that each point is assigned to the nearest cluster.
        for (Clusterable<Double[]> point : points) {
            Cluster<Double[]> cluster = getNearestCluster(clusters, point);
            assertEquals(point, cluster.getCenter());
        }
    }

    /**
     * Test the clustering algorithm with 3 dimensions points.
     */
    @Test
    public void testCluster3D() {
        Random random = new Random();
        List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Double[] p = new Double[3];
            p[0] = random.nextGaussian();
            p[1] = random.nextGaussian();
            p[2] = random.nextGaussian();
            points.add(p);
        }

        List<Cluster<Double[]>> clusters = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE)
                .cluster(points, 3, 100);

        assertEquals(3, clusters.size());
        for (Cluster<Double[]> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }

        // Check that each point is assigned to the nearest cluster.
        for (Clusterable<Double[]> point : points) {
            Cluster<Double[]> cluster = getNearestCluster(clusters, point);
            Double[] center = cluster.getCenter().getPoint();
            for (int i = 0; i < 3; i++) {
                assertEquals(point[i], center[i], 0.001);
            }
        }

        // Check that the clusters are well separated.
        for (int i = 0; i < clusters.size(); i++) {
            List<Clusterable<Double[]>> subPoints = clusters.get(i).getPoints();
            Double[] center = clusters.get(i).getCenter().getPoint();
            for (Clusterable<Double[]> p1 : subPoints) {
                for (Clusterable<Double[]> p2 : clusters.get(i % 2 != 0 ? 0 : 1).getPoints()) {
                    double distance = p1.distanceFrom(p2.getPoint());
                    assertTrue(distance > 3);
                }
            }
        }
    }

    /**
     * Test the strategy to handle empty clusters.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        List<Clusterable<Double[]>> points = new ArrayList<>();
        points.add(new Double[]{0.0, 0.0});
        points.add(new Double[]{0.0, 0.0});
        points.add(new Double[]{0.0, 0.0});
        points.add(new Double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY});

        List<Cluster<Double[]>> clusters = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR)
                .cluster(points, 2, 100);

        assertEquals(2, clusters.size());
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertTrue(clusters.get(1).getPoints().isEmpty());
    }

    /**
     * Test the nearest cluster method.
     *
     * @param clusters the clusters to find the nearest one for
     * @param point the point to find the nearest cluster for
     * @return the nearest cluster to the given point
     */
    private Cluster<Double[]> getNearestCluster(List<Cluster<Double[]>> clusters, Clusterable<Double[]> point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<Double[]> minCluster = null;
        for (Cluster<Double[]> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter().getPoint());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }
}