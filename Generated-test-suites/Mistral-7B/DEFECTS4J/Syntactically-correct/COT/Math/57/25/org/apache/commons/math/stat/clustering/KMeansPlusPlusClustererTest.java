package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private static final double MAX_ITERATIONS = 100;

    private final Class<T> pointClass;
    private final Random random;

    public KMeansPlusPlusClustererTest(Class<T> pointClass) {
        this.pointClass = pointClass;
        this.random = new Random();
    }

    @Parameterized.Parameters(name = "{0} points with {1} clusters and maxIterations={2}")
    public static List<Object[]> testData() {
        List<Object[]> data = new ArrayList<>();
        try {
            data.add(new Object[]{ClusterablePoint.class, NUM_POINTS, NUM_CLUSTERS, MAX_ITERATIONS});
            data.add(new Object[]{ClusterablePoint3D.class, NUM_POINTS, NUM_CLUSTERS, MAX_ITERATIONS});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Test
    public void testCluster() throws Exception {
        List<T> points = createTestPoints();
        int k = NUM_CLUSTERS;
        int maxIterations = Math.max(0, MAX_ITERATIONS);
        Clusterer<T> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<T>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() throws Exception {
        List<T> points = createTestPoints();
        int k = NUM_CLUSTERS + 1;
        int maxIterations = Math.max(0, MAX_ITERATIONS);
        Clusterer<T> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<T>> clusters = clusterer.cluster(points, k, maxIterations);
        fail("Expected ConvergenceException but got: " + clusters);
    }

    @Test
    public void testLargestVarianceStrategy() throws Exception {
        List<T> points = createTestPoints();
        int k = NUM_CLUSTERS + 1;
        int maxIterations = Math.max(0, MAX_ITERATIONS);
        Clusterer<T> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<T>> clusters = clusterer.cluster(points, k, maxIterations);
        assertTrue(clusters.size() > k);
    }

    @Test
    public void testLargestPointNumberStrategy() throws Exception {
        List<T> points = createTestPoints();
        int k = NUM_CLUSTERS + 1;
        int maxIterations = Math.max(0, MAX_ITERATIONS);
        Clusterer<T> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<T>> clusters = clusterer.cluster(points, k, maxIterations);
        assertTrue(clusters.size() > k);
    }

    @Test
    public void testFarthestPointStrategy() throws Exception {
        List<T> points = createTestPoints();
        int k = NUM_CLUSTERS + 1;
        int maxIterations = Math.max(0, MAX_ITERATIONS);
        Clusterer<T> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<T>> clusters = clusterer.cluster(points, k, maxIterations);
        assertTrue(clusters.size() > k);
    }

    @Test
    public void testAssignPointsToClusters() throws Exception {
        List<T> points = createTestPoints();
        List<Cluster<T>> clusters = new ArrayList<>();
        Cluster<T> cluster1 = new Cluster<>();
        Cluster<T> cluster2 = new Cluster<>();
        Cluster<T> cluster3 = new Cluster<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        assignPointsToClusters(clusters, points);
        int expectedSize = (NUM_POINTS / NUM_CLUSTERS) + (NUM_POINTS % NUM_CLUSTERS > 0 ? 1 : 0);
        for (Cluster<T> cluster : clusters) {
            assertEquals(expectedSize, cluster.getPoints().size());
        }
    }

    @Test
    public void testChooseInitialCenters() throws Exception {
        List<T> points = createTestPoints();
        List<Cluster<T>> clusters = chooseInitialCenters(points, NUM_CLUSTERS, random);
        assertEquals(NUM_CLUSTERS, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertTrue(!cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterAfterChooseInitialCenters() throws Exception {
        List<T> points = Collections.singletonList(new ClusterablePoint());
        List<Cluster<T>> clusters = chooseInitialCenters(points, 1, random);
        fail("Expected ConvergenceException but got: " + clusters);
    }

    @Test
    public void testGetNearestCluster() throws Exception {
        List<Cluster<T>> clusters = createTestClusters();
        T point = createTestPoint();
        Cluster<T> nearestCluster = getNearestCluster(clusters, point);
        assertTrue(nearestCluster != null);
        double expectedDistance = point.distanceFrom(nearestCluster.getCenter());
        for (Cluster<T> cluster : clusters) {
            if (cluster != nearestCluster) {
                double distance = point.distanceFrom(cluster.getCenter());
                assertTrue(distance > expectedDistance);
            }
        }
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() throws Exception {
        List<Cluster<T>> clusters = createTestClusters();
        T pointFromLargestVarianceCluster = getPointFromLargestVarianceCluster(clusters);
        assertNotNull(pointFromLargestVarianceCluster);
    }

    @Test(expected = ConvergenceException.class)
    public void testGetPointFromEmptyCluster() throws Exception {
        List<Cluster<T>> clusters = createTestClusters();
        Cluster<T> emptyCluster = clusters.remove(0);
        T pointFromEmptyCluster = getPointFromLargestVarianceCluster(clusters);
        fail("Expected ConvergenceException but got: " + pointFromEmptyCluster);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() throws Exception {
        List<Cluster<T>> clusters = createTestClusters();
        T pointFromLargestNumberCluster = getPointFromLargestNumberCluster(clusters);
        assertNotNull(pointFromLargestNumberCluster);
    }

    @Test(expected = ConvergenceException.class)
    public void testGetPointFromEmptyClusterAfterRemoval() throws Exception {
        List<Cluster<T>> clusters = createTestClusters();
        Cluster<T> emptyCluster = clusters.remove(0);
        T pointFromEmptyCluster = getPointFromLargestNumberCluster(clusters);
        fail("Expected ConvergenceException but got: " + pointFromEmptyCluster);
    }

    private List<T> createTestPoints() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add((T) pointClass.newInstance());
        }
        return points;
    }

    private List<Cluster<T>> createTestClusters() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<T> points = createTestPoints();
        return chooseInitialCenters(points, NUM_CLUSTERS, random);
    }

    private T createTestPoint() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        T point = pointClass.newInstance();
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        for (int i = 0; i < pointClass.getDeclaredFields().length; i++) {
            pointClass.getDeclaredFields()[i].setAccessible(true);
            pointClass.getDeclaredFields()[i].set(point, distribution.sample());
        }
        return point;
    }

    private List<Cluster<T>> chooseInitialCenters(Collection<T> points, int k, Random random) throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        return KMeansPlusPlusClusterer.chooseInitialCenters(points, k, random);
    }

    private void assignPointsToClusters(List<Cluster<T>> clusters, Collection<T> points) {
        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);
    }

    private T getPointFromLargestVarianceCluster(List<Cluster<T>> clusters) {
        return KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);
    }

    private T getPointFromLargestNumberCluster(List<Cluster<T>> clusters) {
        return KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);
    }

    private T getFarthestPoint(List<Cluster<T>> clusters) {
        return KMeansPlusPlusClusterer.getFarthestPoint(clusters);
    }

    private Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        return KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
    }

}

class ClusterablePoint implements Clusterable<ClusterablePoint> {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;

    public ClusterablePoint() {}

    public ClusterablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceFrom(ClusterablePoint other) {
        if (other == null) {
            throw new IllegalArgumentException("other is null");
        }
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}

class ClusterablePoint3D implements Clusterable<ClusterablePoint3D> {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private double z;

    public ClusterablePoint3D() {}

    public ClusterablePoint3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double distanceFrom(ClusterablePoint3D other) {
        if (other == null) {
            throw new IllegalArgumentException("other is null");
        }
        return new EuclideanDistance().distance(this, other);
    }
}