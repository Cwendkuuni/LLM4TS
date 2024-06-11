package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double EPSILON = 1e-9;
    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private static final double MEAN_DIFFERENCE = 1.5;
    private static final int MAX_ITERATIONS = 10;
    private static final double VARIANCE_THRESHOLD = 0.01;

    private KMeansPlusPlusClusterer<T> clusterer;
    private List<T> points;
    private List<Cluster<T>> expectedClusters;

    @Before
    public void setup() {
        NormalDistribution random = new NormalDistribution(0.0, 1.0);
        clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        points = new ArrayList<>();
        expectedClusters = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            T point = createTestPoint(random);
            points.add(point);
            expectedClusters.add(createTestCluster(i % NUM_CLUSTERS, point));
        }
    }

    @Test
    public void testCluster() {
        List<Cluster<T>> actualClusters = clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        assertSameSizes(expectedClusters, actualClusters);
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster<T> expectedCluster = expectedClusters.get(i);
            Cluster<T> actualCluster = actualClusters.get(i);
            List<T> expectedPoints = expectedCluster.getPoints();
            List<T> actualPoints = actualCluster.getPoints();
            assertSameSize("Size", expectedPoints, actualPoints);
            for (int j = 0; j < expectedPoints.size(); j++) {
                T expectedPoint = expectedPoints.get(j);
                T actualPoint = actualPoints.get(j);
                assertSame("Points", expectedPoint, actualPoint);
            }
            T expectedCenter = expectedCluster.getCenter();
            T actualCenter = actualCluster.getCenter();
            assertEquals("Centroids", expectedCenter, actualCenter, EPSILON);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterException() {
        points.clear();
        clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
    }

    @Test
    public void testGetNearestCluster() {
        int index = 0;
        Cluster<T> expectedCluster = expectedClusters.get(index);
        T expectedPoint = expectedCluster.getPoints().get(0);
        T testPoint = expectedPoint;
        Cluster<T> actualCluster = getNearestCluster(clusterer, testPoint);
        assertEquals("Nearest cluster", expectedCluster, actualCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        int index = 0;
        Cluster<T> cluster = expectedClusters.get(index);
        T expectedPoint = cluster.getPoints().get(0);
        T actualPoint = getPointFromLargestVarianceCluster(clusterer, clusterer.getClusters());
        assertEquals("Point from largest variance cluster", expectedPoint, actualPoint);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        int index = 1;
        Cluster<T> cluster = expectedClusters.get(index);
        T expectedPoint = cluster.getPoints().get(cluster.getPoints().size() - 1);
        T actualPoint = getPointFromLargestNumberCluster(clusterer, clusterer.getClusters());
        assertEquals("Point from largest number cluster", expectedPoint, actualPoint);
    }

    @Test
    public void testGetFarthestPoint() {
        int index = 2;
        Cluster<T> cluster = expectedClusters.get(index);
        T expectedPoint = cluster.getPoints().get(0);
        T actualPoint = getFarthestPoint(clusterer, clusterer.getClusters());
        assertEquals("Farthest point", expectedPoint, actualPoint);
    }

    private T createTestPoint(NormalDistribution random) {
        return new TestPoint(random.inverseCDF(random.nextDouble()));
    }

    private Cluster<T> createTestCluster(int index, T point) {
        return new TestCluster<>(index, point);
    }

    private static <T extends Clusterable<T>> int size(Collection<Cluster<T>> clusters) {
        return clusters.size();
    }

    private static <T extends Clusterable<T>> void assertSameSizes(Collection<Cluster<T>> expected, Collection<Cluster<T>> actual) {
        assertEquals("Sizes", size(expected), size(actual));
    }

    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(KMeansPlusPlusClusterer<T> clusterer, T testPoint) {
        List<Cluster<T>> clusters = clusterer.getClusters();
        Cluster<T> nearestCluster = getNearestCluster(clusters, testPoint);
        return nearestCluster;
    }

    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T testPoint) {
        Cluster<T> nearestCluster = null;
        for (Cluster<T> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                fail("Empty cluster encountered during testing");
            }
            double distance = testPoint.distanceFrom(cluster.getCenter());
            if (nearestCluster == null || distance < nearestCluster.getCenter().distanceFrom(testPoint)) {
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    private static <T extends Clusterable<T>> T getPointFromLargestVarianceCluster(KMeansPlusPlusClusterer<T> clusterer,
                                                                               List<Cluster<T>> clusters) {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<T> selectedCluster = null;
        for (Cluster<T> cluster : clusters) {
            if (!cluster.isEmpty()) {
                Variance variance = new Variance();
                List<T> points = cluster.getPoints();
                for (T point : points) {
                    variance.increment(point.distanceFrom(cluster.getCenter()));
                }
                double varianceValue = variance.getResult();
                if (varianceValue > maxVariance) {
                    maxVariance = varianceValue;
                    selectedCluster = cluster;
                }
            }
        }
        if (selectedCluster == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        return Collections.max(selectedCluster.getPoints(), (p1, p2) -> Precision.compareDoubles(p1.distanceFrom(selectedCluster.getCenter()), p2.distanceFrom(selectedCluster.getCenter()), EPSILON));
    }

    private static <T extends Clusterable<T>> T getPointFromLargestNumberCluster(KMeansPlusPlusClusterer<T> clusterer,
                                                                              List<Cluster<T>> clusters) {
        int maxNumber = 0;
        Cluster<T> selectedCluster = null;
        for (Cluster<T> cluster : clusters) {
            List<T> points = cluster.getPoints();
            int number = points.size();
            if (number > maxNumber) {
                maxNumber = number;
                selectedCluster = cluster;
            }
        }
        if (selectedCluster == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        return Collections.max(selectedCluster.getPoints(), (p1, p2) -> 0);
    }

    private static <T extends Clusterable<T>> T getFarthestPoint(KMeansPlusPlusClusterer<T> clusterer,
                                                               List<Cluster<T>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<T> selectedCluster = null;
        for (Cluster<T> cluster : clusters) {
            List<T> points = cluster.getPoints();
            if (points.isEmpty()) {
                fail("Empty cluster encountered during testing");
            }
            double distance = points.get(0).distanceFrom(cluster.getCenter());
            if (distance > maxDistance) {
                maxDistance = distance;
                selectedCluster = cluster;
            }
        }
        if (selectedCluster == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        return Collections.max(selectedCluster.getPoints(), (p1, p2) -> Precision.compareDoubles(p1.distanceFrom(selectedCluster.getCenter()), p2.distanceFrom(selectedCluster.getCenter()), EPSILON).negate().getValue());
    }
}

class TestPoint implements Clusterable<TestPoint> {

    private final double value;

    public TestPoint(double value) {
        this.value = value;
    }

    @Override
    public double distanceFrom(Clusterable<TestPoint> other) {
        TestPoint otherPoint = (TestPoint) other;
        return Math.abs(value - otherPoint.getValue());
    }

    @Override
    public double distanceFrom(Cluster<TestPoint> cluster) {
        return cluster.getCenter().distanceFrom(this);
    }

    public double getValue() {
        return value;
    }

}

class TestCluster<T extends Clusterable<T>> implements Cluster<T> {

    private final int index;
    private final List<T> points;
    private T center;

    public TestCluster(int index, T point) {
        this.index = index;
        this.points = new ArrayList<>();
        this.points.add(point);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public List<T> getPoints() {
        return points;
    }

    @Override
    public T getCenter() {
        if (center == null) {
            center = calculateCenter();
        }
        return center;
    }

    private T calculateCenter() {
        TestPoint sum = new TestPoint(0.0);
        double sumOfWeights = 0.0;
        for (T point : points) {
            sum.setValue(sum.getValue() + point.getValue());
            sumOfWeights++;
        }
        return new TestPoint(sum.getValue() / sumOfWeights);
    }

}