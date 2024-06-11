package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        final List<Clusterable<Double>> data = Arrays.asList(
            new Clusterable<Double>() {
                @Override
                public Double distanceFrom(Clusterable<?> other) {
                    return 0.0;
                }

                @Override
                public Double getX() {
                    return 0.0;
                }

                @Override
                public String toString() {
                    return "Empty";
                }
            },
            new Clusterable<Double>() {
                @Override
                public Double distanceFrom(Clusterable<?> other) {
                    return 1.0;
                }

                @Override
                public Double getX() {
                    return 1.0;
                }

                @Override
                public String toString() {
                    return "NonEmpty";
                }
            }
        );

        // Test with default strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(data, 2, 1);
        Cluster<Clusterable<Double>> emptyCluster = result.get(0);
        assertFalse(emptyCluster.isEmpty());
        assertTrue(emptyCluster.getPoints().isEmpty());

        // Test with LARGEST_VARIANCE strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> result1 = clustering1.cluster(data, 2, 1);
        Cluster<Clusterable<Double>> largestVarianceCluster = result1.get(0);
        assertFalse(largestVarianceCluster.isEmpty());
        assertTrue(largestVarianceCluster.getPoints().isEmpty());
        Cluster<Clusterable<Double>> nonEmptyCluster = result1.get(1);
        assertTrue(nonEmptyCluster.getPoints().size() > 0);

        // Test with LARGEST_POINTS_NUMBER strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> result2 = clustering2.cluster(data, 2, 1);
        Cluster<Clusterable<Double>> largestPointsNumberCluster = result2.get(0);
        assertFalse(largestPointsNumberCluster.isEmpty());
        assertTrue(largestPointsNumberCluster.getPoints().size() > 0);
        Cluster<Clusterable<Double>> emptyCluster2 = result2.get(1);
        assertFalse(emptyCluster2.isEmpty());
        assertTrue(emptyCluster2.getPoints().isEmpty());

        // Test with FARTHEST_POINT strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> result3 = clustering3.cluster(data, 2, 1);
        Cluster<Clusterable<Double>> farthestPointCluster = result3.get(0);
        assertFalse(farthestPointCluster.isEmpty());
        assertTrue(farthestPointCluster.getPoints().isEmpty());
        Cluster<Clusterable<Double>> nonEmptyCluster2 = result3.get(1);
        assertTrue(nonEmptyCluster2.getPoints().size() > 0);

        // Test with ERROR strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering4 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        try {
            clustering4.cluster(data, 2, 1);
            fail("Should throw a ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testCluster() {
        final List<Clusterable<Double>> data = Arrays.asList(
                1.0, 1.0, 1.0, 1.0, 1.0,
                2.0, 2.0, 2.0, 2.0, 2.0,
                3.0, 3.0, 3.0, 3.0, 3.0,
                4.0, 4.0, 4.0, 4.0, 4.0
        );

        final double epsilon = 1e-6;

        // Test with default strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(data, 3, 100);

        // Check the number of clusters
        assertEquals(3, result.size());

        // Check the size of each cluster
        for (Cluster<Clusterable<Double>> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check the distance between points and their respective cluster centers
        for (Cluster<Clusterable<Double>> cluster : result) {
            Clusterable<Double> center = cluster.getCenter();
            List<Clusterable<Double>> points = cluster.getPoints();
            for (Clusterable<Double> point : points) {
                double distance = Precision.doubleValue(point.distanceFrom(center));
                assertTrue(distance < epsilon);
            }
        }

        // Test with LARGEST_VARIANCE strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> result1 = clustering1.cluster(data, 3, 100);
        assertEquals(3, result1.size());
        for (Cluster<Clusterable<Double>> cluster : result1) {
            assertTrue(cluster.getPoints().size() > 0);
            Clusterable<Double> center1 = cluster.getCenter();
            List<Clusterable<Double>> points1 = cluster.getPoints();
            for (Clusterable<Double> point : points1) {
                double distance1 = Precision.doubleValue(point.distanceFrom(center1));
                assertTrue(distance1 < epsilon);
            }
        }

        // Test with LARGEST_POINTS_NUMBER strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> result2 = clustering2.cluster(data, 3, 100);
        assertEquals(3, result2.size());
        for (Cluster<Clusterable<Double>> cluster : result2) {
            assertTrue(cluster.getPoints().size() > 0);
            Clusterable<Double> center2 = cluster.getCenter();
            List<Clusterable<Double>> points2 = cluster.getPoints();
            for (Clusterable<Double> point : points2) {
                double distance2 = Precision.doubleValue(point.distanceFrom(center2));
                assertTrue(distance2 < epsilon);
            }
        }

        // Test with FARTHEST_POINT strategy
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> result3 = clustering3.cluster(data, 3, 100);
        assertEquals(3, result3.size());
        for (Cluster<Clusterable<Double>> cluster : result3) {
            assertTrue(cluster.getPoints().size() > 0);
            Clusterable<Double> center3 = cluster.getCenter();
            List<Clusterable<Double>> points3 = cluster.getPoints();
            for (Clusterable<Double> point : points3) {
                double distance3 = Precision.doubleValue(point.distanceFrom(center3));
                assertTrue(distance3 < epsilon);
            }
        }
    }
}