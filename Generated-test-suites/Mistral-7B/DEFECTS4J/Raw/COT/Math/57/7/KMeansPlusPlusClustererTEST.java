package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.data.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Category(org.apache.commons.math3.test.category.Math2D)
public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(1.0, 1.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 2, -1);
        assertEquals(2, clusters.size());
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertTrue(clusters.get(1).getPoints().isEmpty());
        try {
            clustering.cluster(Collections.emptyList(), 2, -1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testChooseInitialCenters() {
        Random random = new Random();
        List<TestPoint> points = new RandomDataGenerator<>(50).nextPoints(50);
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 5, 10);
        assertEquals(5, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
        }
        List<TestPoint> initialCenters = new ArrayList<>();
        for (Cluster<TestPoint> cluster : clusters) {
            initialCenters.add(cluster.getCenter());
            cluster.clear();
        }
        assertEquals(5, initialCenters.size());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testChooseInitialCentersNegativeSize() {
        Random random = new Random();
        List<TestPoint> points = new ArrayList<>();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        clustering.cluster(points, 5, -1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testChooseInitialCentersMaxIterations() {
        Random random = new Random();
        List<TestPoint> points = new ArrayList<>();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        clustering.cluster(points, 5, Integer.MAX_VALUE);
    }

    @Test
    public void testAssignPointsToClusters() {
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        points.add(new TestPoint(4.0, 4.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>());
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        assignPointsToClusters(clustering, clusters, points);
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }
    }

    @Test
    public void testGetNearestCluster() {
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        points.add(new TestPoint(4.0, 4.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>());
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        Cluster<TestPoint> nearestCluster = getNearestCluster(clustering, new TestPoint(2.5, 2.5), clusters);
        assertEquals(clusters.get(1), nearestCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>());
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        T point = getPointFromLargestVarianceCluster(clustering, clusters);
        assertEquals(points.get(1), point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>());
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        T point = getPointFromLargestNumberCluster(clustering, clusters);
        assertEquals(points.get(0), point);
    }

    @Test
    public void testGetFarthestPoint() {
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>());
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        T point = getFarthestPoint(clustering, clusters);
        assertEquals(points.get(3), point);
    }

    private void assignPointsToClusters(KMeansPlusPlusClusterer<TestPoint> clustering,
                                       List<Cluster<TestPoint>> clusters, List<TestPoint> points) {
        clustering.assignPointsToClusters(clusters, points);
    }

    private T getNearestCluster(KMeansPlusPlusClusterer<TestPoint> clustering, TestPoint point,
                               List<Cluster<TestPoint>> clusters) {
        return clustering.getNearestCluster(point).getCenter();
    }

    private T getPointFromLargestVarianceCluster(KMeansPlusPlusClusterer<TestPoint> clustering,
                                               List<Cluster<TestPoint>> clusters) {
        return clustering.getPointFromLargestVarianceCluster(clusters).getCenter();
    }

    private T getPointFromLargestNumberCluster(KMeansPlusPlusClusterer<TestPoint> clustering,
                                             List<Cluster<TestPoint>> clusters) {
        return clustering.getPointFromLargestNumberCluster(clusters).getCenter();
    }

    private T getFarthestPoint(KMeansPlusPlusClusterer<TestPoint> clustering,
                              List<Cluster<TestPoint>> clusters) {
        return clustering.getFarthestPoint(clusters).getCenter();
    }

}

class TestPoint implements Clusterable<TestPoint> {

    private double x, y;

    public TestPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(Clusterable<TestPoint> other) {
        TestPoint point = (TestPoint) other;
        return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

}