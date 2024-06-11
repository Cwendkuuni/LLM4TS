package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCluster() {
        Random random = new Random(1);

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<Point2D> points = Arrays.asList(
                new Point2D(1.0, 1.0),
                new Point2D(2.0, 2.0),
                new Point2D(3.0, 3.0),
                new Point2D(4.0, 4.0),
                new Point2D(5.0, 5.0)
        );

        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }

        // Check that each point belongs to the closest cluster
        for (Point2D point : points) {
            Cluster<Point2D> cluster = getNearestCluster(clusters, point);
            assertTrue(cluster.getPoints().contains(point));
        }

        // Check that the centers are well calculated
        List<Point2D> centers = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            Point2D center = cluster.getCenter();
            List<Point2D> pointsInCluster = cluster.getPoints();
            List<Point2D> expectedPoints = new ArrayList<>();
            for (Point2D point : pointsInCluster) {
                expectedPoints.add(point);
            }
            Point2D expectedCenter = calculateCenter(expectedPoints);
            assertEquals(expectedCenter, center);
        }

        // Test convergence
        List<Cluster<Point2D>> oldClusters = clusters;
        clusters = clusterer.cluster(points, 3, 1);
        assertNotEquals(oldClusters, clusters);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> minCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minCluster = cluster;
                minDistance = distance;
            }
        }
        return minCluster;
    }

    private static Point2D calculateCenter(List<Point2D> points) {
        double xSum = 0;
        double ySum = 0;
        for (Point2D point : points) {
            xSum += point.getX();
            ySum += point.getY();
        }
        return new Point2D(xSum / points.size(), ySum / points.size());
    }

    private static class Point2D implements Clusterable<Point2D> {
        private final double x, y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(Cluster<Point2D> cluster) {
            Point2D center = cluster.getCenter();
            return Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(y - center.getY(), 2));
        }

        public double distanceFrom(Point2D point) {
            return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
        }
    }

}