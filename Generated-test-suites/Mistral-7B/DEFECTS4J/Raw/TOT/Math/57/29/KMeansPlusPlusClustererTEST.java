package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.distance.DistanceMeasure;
import org.apache.commons.math3.distance.EuclideanDistance;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 10;
    private static final int K = 3;

    private List<Clusterable<Double>> points;
    private KMeansPlusPlusClusterer<Clusterable<Double>> clustering;

    @Before
    public void setUp() {
        Random random = new Random();
        Clusterable<Double>[] pointsArray = new Clusterable[NUM_POINTS];
        for (int i = 0; i < NUM_POINTS; i++) {
            pointsArray[i] = new DummyPoint(i);
        }
        points = Arrays.asList(pointsArray);
        clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
    }

    /**
     * Test clustering with valid input.
     */
    @Test
    public void testClusterValidData() {
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(cluster.size(), 1 > cluster.getPoints().size() ? 0 : cluster.getPoints().size());
        }
    }

    /**
     * Test clustering with empty data.
     */
    @Test(expected = ConvergenceException.class)
    public void testClusterEmptyData() {
        clustering.cluster(Collections.emptyList(), K, 10);
    }

    /**
     * Test clustering with non-matching data types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterInvalidDataTypes() {
        List<Object> invalidPoints = new ArrayList<>();
        invalidPoints.add(new Integer(1));
        clustering.cluster(invalidPoints, K, 10);
    }

    /**
     * Test clustering with negative number of clusters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterNegativeNumberOfClusters() {
        clustering.cluster(points, -1, 10);
    }

    /**
     * Test clustering with negative maximum number of iterations.
     */
    @Test
    public void testClusterNegativeMaxIterations() {
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, -1);
        assertNotNull(clusters);
    }

    /**
     * Test clustering with empty initial centers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterEmptyInitialCenters() {
        List<Cluster<Clusterable<Double>>> emptyClusters = new ArrayList<>();
        clustering.cluster(points, K, emptyClusters);
    }

    /**
     * Test empty strategy: largest variance.
     */
    @Test
    public void testEmptyStrategyLargestVariance() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(cluster.size(), 1 > cluster.getPoints().size() ? 0 : cluster.getPoints().size());
            Cluster<Clusterable<Double>> largestVarianceCluster = null;
            double largestVariance = Double.NEGATIVE_INFINITY;
            for (Cluster<Clusterable<Double>> c : clusters) {
                Variance variance = new Variance();
                for (Clusterable<Double> p : c.getPoints()) {
                    variance.increment(p.distanceFrom(c.getCenter()));
                }
                double currentVariance = variance.getResult();
                if (currentVariance > largestVariance) {
                    largestVarianceCluster = c;
                    largestVariance = currentVariance;
                }
            }
            assertEquals(largestVarianceCluster, cluster);
        }
    }

    /**
     * Test empty strategy: largest points number.
     */
    @Test
    public void testEmptyStrategyLargestPointsNumber() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(cluster.size(), 1 > cluster.getPoints().size() ? 0 : cluster.getPoints().size());
            Cluster<Clusterable<Double>> largestPointsNumberCluster = null;
            int largestNumberOfPoints = 0;
            for (Cluster<Clusterable<Double>> c : clusters) {
                if (c.getPoints().size() > largestNumberOfPoints) {
                    largestPointsNumberCluster = c;
                    largestNumberOfPoints = c.getPoints().size();
                }
            }
            assertEquals(largestPointsNumberCluster, cluster);
        }
    }

    /**
     * Test empty strategy: farthest point.
     */
    @Test
    public void testEmptyStrategyFarthestPoint() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(cluster.size(), 1 > cluster.getPoints().size() ? 0 : cluster.getPoints().size());
            Cluster<Clusterable<Double>> farthestPointCluster = null;
            Double farthestDistance = Double.NEGATIVE_INFINITY;
            for (Cluster<Clusterable<Double>> c : clusters) {
                DistanceMeasure distanceMeasure = new EuclideanDistance();
                double distance = distanceMeasure.compute(c.getCenter(), new DummyPoint(0));
                if (distance > farthestDistance) {
                    farthestPointCluster = c;
                    farthestDistance = distance;
                }
            }
            assertEquals(farthestPointCluster, cluster);
        }
    }

    /**
     * Test error strategy.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyStrategyError() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable<Double>>> clusters = clustering.cluster(points, K, 10);
    }

}

class DummyPoint implements Clusterable<Double> {

    private final int id;

    public DummyPoint(int id) {
        this.id = id;
    }

    @Override
    public Double getX() {
        return (double) Math.random();
    }

    @Override
    public Double getY() {
        return (double) Math.random();
    }

    @Override
    public Double distanceFrom(Clusterable<Double> o) {
        DummyPoint other = (DummyPoint) o;
        return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getY() - other.getY(), 2));
    }

}