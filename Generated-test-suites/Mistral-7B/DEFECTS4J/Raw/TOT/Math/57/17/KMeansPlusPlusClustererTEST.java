package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final int k;
    private final List<T> points;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int k, List<T> points, EmptyClusterStrategy emptyStrategy) {
        this.k = k;
        this.points = points;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameterized.Parameters(name = "{index}: k={0}, strategy={1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, Arrays.asList(new DummyPoint(0.0), new DummyPoint(1.0), new DummyPoint(2.0), new DummyPoint(3.0)), EmptyClusterStrategy.LARGEST_VARIANCE},
                {2, Arrays.asList(new DummyPoint(0.0), new DummyPoint(1.0), new DummyPoint(0.0), new DummyPoint(1.0), new DummyPoint(2.0), new DummyPoint(3.0)), EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                {3, Arrays.asList(new DummyPoint(0.0), new DummyPoint(1.0), new DummyPoint(2.0), new DummyPoint(3.0), new DummyPoint(4.0), new DummyPoint(5.0), new DummyPoint(6.0), new DummyPoint(7.0)), EmptyClusterStrategy.FARTHEST_POINT},
                {3, Collections.emptyList(), EmptyClusterStrategy.ERROR},
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPoints() {
        new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(null, k, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeK() {
        new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, -1, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStrategyError() {
        new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR).cluster(points, k, 1000);
    }

    @Test
    public void testNormalOperation() {
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, 100);
        assertEquals(k, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterDuringExecution() {
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, 1);
        clusters.remove(0); // Empty cluster was created, remove it
        List<Cluster<T>> newClusters = new ArrayList<>();
        for (Cluster<T> cluster : clusters) {
            newClusters.add(cluster);
        }
        new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, 100).equals(newClusters);
    }

    @Test
    public void testMaxIterations() throws InterruptedException, MaxCountExceededException {
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, -1);
        List<Cluster<T>> expectedClusters = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, 1000);
        for (int i = 0; i < clusters.size(); i++) {
            assertEquals(expectedClusters.get(i).getCenter(), clusters.get(i).getCenter());
        }
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy).cluster(points, k, 100);
        T testPoint = new DummyPoint(5.0);
        Cluster<T> expectedCluster = clusters.get(0);
        Cluster<T> actualCluster = getNearestCluster(clusters, testPoint);
        assertEquals(expectedCluster.getCenter(), actualCluster.getCenter());
    }

    @Test
    public void testChooseInitialCenters() {
        List<T> pointSet = new ArrayList<>(points);
        List<Cluster<T>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, k, new Random());
        assertEquals(k, initialCenters.size());
        for (Cluster<T> initialCenter : initialCenters) {
            assertTrue(pointSet.contains(initialCenter.getCenter()));
            pointSet.remove(initialCenter.getCenter());
        }
        assertTrue(pointSet.isEmpty());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<T>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster<T>(new DummyPoint(i)));
        }
        assignPointsToClusters(clusters, points);
        for (Cluster<T> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(final Collection<Cluster<T>> clusters, final T point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<T> minCluster = null;
        for (final Cluster<T> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }

}

class DummyPoint implements Clusterable<DummyPoint> {

    private final double value;

    public DummyPoint(double value) {
        this.value = value;
    }

    @Override
    public DummyPoint centroidOf(Collection<? extends Clusterable<DummyPoint>> points) {
        double sumX = 0;
        double sumY = 0;
        int count = 0;
        for (Clusterable<DummyPoint> point : points) {
            sumX += point.getValue();
            sumY += point.getValueSquared();
            count++;
        }
        return new DummyPoint(sumX / count);
    }

    @Override
    public double distanceFrom(Clusterable<DummyPoint> other) {
        DummyPoint otherPoint = (DummyPoint) other;
        return Math.sqrt(Math.pow(this.value - otherPoint.value, 2));
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getValueSquared() {
        return Math.pow(value, 2);
    }

}