package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test for KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyConstructor() {
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>();
        Assert.assertNotNull(kmeans);
        EmptyClusterStrategy strategy = kmeans.getEmptyClusterStrategy();
        Assert.assertEquals(EmptyClusterStrategy.LARGEST_VARIANCE, strategy);
    }

    @Test
    public void testStrategyConstructor() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        Assert.assertNotNull(kmeans);
        Assert.assertEquals(EmptyClusterStrategy.LARGEST_POINTS_NUMBER, kmeans.getEmptyClusterStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollection() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        kmeans.cluster(null, 2, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfClusters() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        kmeans.cluster(Collections.emptyList(), -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        kmeans.cluster(Collections.emptyList(), 2, -1);
    }

    @Test
    public void testClusterWithTwoPoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = kmeans.cluster(points, 2, 10);
        Assert.assertEquals(2, clusters.size());
        Cluster<TestPoint> cluster1 = clusters.get(0);
        TestPoint point1 = cluster1.getPoints().get(0);
        TestPoint point2 = cluster1.getPoints().get(1);
        Assert.assertEquals(point1.getX(), cluster1.getCenter().getX(), 0.01);
        Assert.assertEquals(point1.getY(), cluster1.getCenter().getY(), 0.01);
        Assert.assertEquals(point2.getX(), cluster1.getCenter().getX(), 0.01);
        Assert.assertEquals(point2.getY(), cluster1.getCenter().getY(), 0.01);
        Cluster<TestPoint> cluster2 = clusters.get(1);
        TestPoint point3 = cluster2.getPoints().get(0);
        TestPoint point4 = cluster2.getPoints().get(1);
        Assert.assertEquals(point3.getX(), cluster2.getCenter().getX(), 0.01);
        Assert.assertEquals(point3.getY(), cluster2.getCenter().getY(), 0.01);
        Assert.assertEquals(point4.getX(), cluster2.getCenter().getX(), 0.01);
        Assert.assertEquals(point4.getY(), cluster2.getCenter().getY(), 0.01);
    }

    @Test
    public void testClusterWithThreePoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        List<Cluster<TestPoint>> clusters = kmeans.cluster(points, 3, 10);
        Assert.assertEquals(3, clusters.size());
        Cluster<TestPoint> cluster1 = clusters.get(0);
        TestPoint point1 = cluster1.getPoints().get(0);
        TestPoint point2 = cluster1.getPoints().get(1);
        Assert.assertEquals(point1.getX(), cluster1.getCenter().getX(), 0.01);
        Assert.assertEquals(point1.getY(), cluster1.getCenter().getY(), 0.01);
        Assert.assertEquals(point2.getX(), cluster1.getCenter().getX(), 0.01);
        Assert.assertEquals(point2.getY(), cluster1.getCenter().getY(), 0.01);
        Cluster<TestPoint> cluster2 = clusters.get(1);
        TestPoint point3 = cluster2.getPoints().get(0);
        TestPoint point4 = cluster2.getPoints().get(1);
        Assert.assertEquals(point3.getX(), cluster2.getCenter().getX(), 0.01);
        Assert.assertEquals(point3.getY(), cluster2.getCenter().getY(), 0.01);
        Assert.assertEquals(point4.getX(), cluster2.getCenter().getX(), 0.01);
        Assert.assertEquals(point4.getY(), cluster2.getCenter().getY(), 0.01);
        Cluster<TestPoint> cluster3 = clusters.get(2);
        TestPoint point5 = cluster3.getPoints().get(0);
        TestPoint point6 = cluster3.getPoints().get(1);
        Assert.assertEquals(point5.getX(), cluster3.getCenter().getX(), 0.01);
        Assert.assertEquals(point5.getY(), cluster3.getCenter().getY(), 0.01);
        Assert.assertEquals(point6.getX(), cluster3.getCenter().getX(), 0.01);
        Assert.assertEquals(point6.getY(), cluster3.getCenter().getY(), 0.01);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterDuringIteration() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = kmeans.cluster(points, 2, 1);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            }
        }
        clusters = kmeans.cluster(points, 2, 2);
        Assert.assertEquals(2, clusters.size());
        Cluster<TestPoint> cluster1 = clusters.get(0);
        TestPoint point1 = cluster1.getPoints().get(0);
        TestPoint point2 = cluster1.getPoints().get(1);
        TestPoint point3 = new TestPoint(0.0, 0.0);
        cluster1.addPoint(point3);
        clusters = kmeans.cluster(points, 2, 1);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            }
        }
        for (Cluster<TestPoint> cluster : clusters) {
            cluster.removePoint(point3);
        }
        clusters = kmeans.cluster(points, 2, 2);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            }
        }
        clusters = kmeans.cluster(points, 2, 3);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            }
        }
        for (Cluster<TestPoint> cluster : clusters) {
            TestPoint point4 = new TestPoint(0.0, 0.0);
            cluster.addPoint(point4);
            clusters = kmeans.cluster(points, 2, 1);
            Assert.assertEquals(2, clusters.size());
            for (Cluster<TestPoint> cluster1 : clusters) {
                if (cluster1.getPoints().contains(point4)) {
                    throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
                }
            }
            for (Cluster<TestPoint> cluster1 : clusters) {
                cluster1.removePoint(point4);
            }
        }
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new Random();
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<TestPoint>(new TestPoint(0.0, 0.0)));
        clusters.add(new Cluster<TestPoint>(new TestPoint(4.0, 4.0)));
        clusters.add(new Cluster<TestPoint>(new TestPoint(5.0, 5.0)));
        for (TestPoint point : points) {
            Cluster<TestPoint> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
            Assert.assertNotNull(nearestCluster);
            int index = clusters.indexOf(nearestCluster);
            Assert.assertEquals(index, KMeansPlusPlusClusterer.getNearestIndex(clusters, point));
        }
    }

    private static class TestPoint implements Clusterable<TestPoint> {

        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Cluster<TestPoint> cluster) {
            TestPoint center = cluster.getCenter();
            return Math.sqrt(Math.pow(x - center.getX(), 2.0) + Math.pow(y - center.getY(), 2.0));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }

}