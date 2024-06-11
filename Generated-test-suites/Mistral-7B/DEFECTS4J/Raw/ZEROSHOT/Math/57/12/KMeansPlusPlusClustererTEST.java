package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-14;

    @ org.junit.Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(0.0, 1.0));
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        assertTrue(clusters.get(0).isEmpty());
        assertTrue(clusters.get(1).isEmpty());
        try {
            kmeans.cluster(points, 2, 1);
            fail("Expected a ConvergenceException");
        } catch (ConvergenceException e) {
            //Expected
        }
    }

    @ org.junit.Test
    public void testInitializeClusters() {
        Random random = new Random();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            assertEquals(1, clusters.get(i).getPoints().size());
            assertEquals(points.get(i), clusters.get(i).getCenter());
        }
    }

    @ org.junit.Test
    public void testIteration() {
        Random random = new Random();
        List<Point2D> points = new ArrayList<>();
        List<Point2D> expectedPoints = new ArrayList<>();
        expectedPoints.add(new Point2D(1.0, 1.0));
        expectedPoints.add(new Point2D(2.0, 2.0));
        expectedPoints.add(new Point2D(3.0, 3.0));
        points.addAll(expectedPoints);
        points.add(new Point2D(0.5, 0.5));
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<Point2D>(expectedPoints.get(0)));
        clusters.add(new Cluster<Point2D>(expectedPoints.get(1)));
        clusters.add(new Cluster<Point2D>(expectedPoints.get(2)));
        Cluster<Point2D> nearestCluster = null;
        for (Cluster<Point2D> c : clusters) {
            double nearestDistance = Double.MAX_VALUE;
            for (Point2D point : points) {
                double distance = point.distanceFrom(c.getCenter());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestCluster = c;
                }
            }
            assertEquals(expectedPoints.get(clusters.indexOf(c)), nearestCluster.getCenter());
            points.remove(nearestCluster.getPoints().get(0));
        }
        clusters = kmeans.cluster(points, 3, 10);
        for (int i = 0; i < clusters.size(); i++) {
            assertEquals(1, clusters.get(i).getPoints().size());
            assertEquals(points.get(i), clusters.get(i).getCenter());
        }
    }

    @ org.junit.Test
    public void testEmptyClusterStrategyLargestVariance() {
        Random random = new Random();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(1.0, 3.0));
        List<Cluster<Point2D>> initialClusters = new ArrayList<>();
        initialClusters.add(new Cluster<Point2D>(points.get(0)));
        initialClusters.add(new Cluster<Point2D>(points.get(1)));
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).isEmpty()) {
                Cluster<Point2D> nearestCluster = getNearestCluster(clusters, points.get(2));
                assertEquals(nearestCluster, clusters.get(i));
                break;
            }
            assertEquals(1, clusters.get(i).getPoints().size());
            assertEquals(points.get(i), clusters.get(i).getCenter());
        }
    }

    @ org.junit.Test
    public void testEmptyClusterStrategyLargestNumber() {
        Random random = new Random();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(1.0, 3.0));
        List<Cluster<Point2D>> initialClusters = new ArrayList<>();
        initialClusters.add(new Cluster<Point2D>(points.get(0)));
        initialClusters.add(new Cluster<Point2D>(points.get(1)));
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).isEmpty()) {
                Cluster<Point2D> nearestCluster = getNearestCluster(clusters, points.get(2));
                assertEquals(nearestCluster, clusters.get(i));
                break;
            }
            assertEquals(1, clusters.get(i).getPoints().size());
            assertEquals(points.get(i), clusters.get(i).getCenter());
        }
    }

    @ org.junit.Test
    public void testEmptyClusterStrategyFarthestPoint() {
        Random random = new Random();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(1.0, 3.0));
        List<Cluster<Point2D>> initialClusters = new ArrayList<>();
        initialClusters.add(new Cluster<Point2D>(points.get(0)));
        initialClusters.add(new Cluster<Point2D>(points.get(1)));
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).isEmpty()) {
                Cluster<Point2D> nearestCluster = getNearestCluster(clusters, points.get(2));
                assertEquals(nearestCluster, clusters.get(i));
                break;
            }
            assertEquals(1, clusters.get(i).getPoints().size());
            assertEquals(points.get(i), clusters.get(i).getCenter());
        }
    }

    private Cluster<Point2D> getNearestCluster(List<Cluster<Point2D>> clusters, Point2D point) {
        Cluster<Point2D> nearestCluster = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster<Point2D> c : clusters) {
            double distance = point.distanceFrom(c.getCenter());
            if (distance < nearestDistance) {
                nearestCluster = c;
                nearestDistance = distance;
            }
        }
        return nearestCluster;
    }

    private static class Point2D implements Clusterable<Point2D> {
        private final double x;
        private final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            Point2D otherPoint = (Point2D) other;
            return Math.sqrt(Precision.pow(this.x - otherPoint.x, 2.0) + Precision.pow(this.y - otherPoint.y, 2.0));
        }

        @Override
        public double[] getMean() {
            return new double[] { this.x, this.y };
        }

        @Override
        public double[] getStandardDeviation() {
            return new double[] { 0.0, 0.0 };
        }

        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return String.format("(%f, %f)", this.x, this.y);
        }
    }

}