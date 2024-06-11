package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.clustering.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        double eps = 1e-4;

        // Test empty collection
        List<Clusterable<Point2D>> pointsEmpty = new ArrayList<>();
        List<Cluster<Point2D>> clustersEmpty = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR).cluster(pointsEmpty, 2, 10);
        assertEquals(0, clustersEmpty.size());

        // Test single point
        Point2D point1 = new Point2D(1.0, 1.0);
        List<Clusterable<Point2D>> pointsSingle = Arrays.asList(point1);
        List<Cluster<Point2D>> clustersSingle = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(pointsSingle, 2, 10);
        assertEquals(1, clustersSingle.size());
        assertEquals(point1, clustersSingle.get(0).getCenter());

        // Test two points
        Point2D point2 = new Point2D(2.0, 2.0);
        List<Clusterable<Point2D>> pointsTwo = Arrays.asList(point1, point2);
        List<Cluster<Point2D>> clustersTwo = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(pointsTwo, 2, 10);
        assertEquals(2, clustersTwo.size());

        Cluster<Point2D> cluster1 = clustersTwo.get(0);
        Cluster<Point2D> cluster2 = clustersTwo.get(1);
        assertEquals(point1, cluster1.getCenter());
        assertEquals(point2, cluster2.getCenter());
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());

        // Test larger dataset
        int n = 100;
        List<Clusterable<Point2D>> pointsLarge = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x = Math.random();
            double y = Math.random();
            Point2D point = new Point2D(x, y);
            pointsLarge.add(point);
        }

        List<Cluster<Point2D>> clustersLarge = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(pointsLarge, 10, 100);
        assertEquals(10, clustersLarge.size());
        for (Cluster<Point2D> cluster : clustersLarge) {
            double variance = new Variance().increment(cluster.getPoints().stream().mapToDouble(p -> p.distanceFrom(cluster.getCenter())).sum())
                    .getResult();
            assertTrue(variance < eps);
        }
    }

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        List<Clusterable<Point2D>> points = new ArrayList<>();

        // Test largest variance strategy
        List<Cluster<Point2D>> clustersLargestVariance = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(points, 2, 10);
        assertEquals(0, clustersLargestVariance.size());
        try {
            new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test largest points number strategy
        List<Cluster<Point2D>> clustersLargestPointsNumber = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER).cluster(points, 2, 10);
        assertEquals(0, clustersLargestPointsNumber.size());
        try {
            new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER).cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test farthest point strategy
        List<Cluster<Point2D>> clustersFarthestPoint = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT).cluster(points, 2, 10);
        assertEquals(0, clustersFarthestPoint.size());
        try {
            new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT).cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test error strategy
        List<Cluster<Point2D>> clustersError = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR).cluster(points, 2, 10);
        assertEquals(0, clustersError.size());
        try {
            new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR).cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}