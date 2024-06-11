package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.util.Precision;

/**
 * Test case for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-6;
    private static final double DELTA = 1e-6;

    private Random random;
    private KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer;
    private List<ClusterablePoint2D> points;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        points = Collections.nCopies(11, new ClusterablePoint2D(new double[]{-2.5, 2.5}));
    }

    @Test
    public void testClusterSize() {
        List<Cluster<ClusterablePoint2D>> result = clusterer.cluster(points, 3, 10);
        assertEquals(3, result.size());
        for (Cluster<ClusterablePoint2D> cluster : result) {
            assertFalse(cluster.isEmpty());
            assertEquals(3, cluster.getPoints().size());
        }
    }

    @Test
    public void testEmptyClusterHandling() {
        List<Cluster<ClusterablePoint2D>> result = clusterer.cluster(Collections.singletonList(new ClusterablePoint2D(new double[]{-100, -100})), 2, 1);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isEmpty());
        assertFalse(result.get(1).isEmpty());

        try {
            clusterer.cluster(Collections.emptyList(), 2, 1);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testClusterCentroids() {
        List<Cluster<ClusterablePoint2D>> result = clusterer.cluster(points, 3, 10);
        for (Cluster<ClusterablePoint2D> cluster : result) {
            ClusterablePoint2D centroid = cluster.getCenter();
            List<ClusterablePoint2D> pointsInCluster = cluster.getPoints();
            double[] expectedCentroid = new double[2];
            for (ClusterablePoint2D point : pointsInCluster) {
                System.arraycopy(point.getCoords(), 0, expectedCentroid, 0, 2);
            }
            Precision.assertClose(expectedCentroid[0], centroid.getX(), EPSILON);
            Precision.assertClose(expectedCentroid[1], centroid.getY(), EPSILON);
        }
    }

    @Test
    public void testClusterDistance() {
        List<Cluster<ClusterablePoint2D>> result = clusterer.cluster(points, 3, 10);
        Cluster<ClusterablePoint2D> cluster1 = result.get(0);
        Cluster<ClusterablePoint2D> cluster2 = result.get(1);

        double distance1 = cluster1.getCenter().distanceFrom(cluster2.getCenter());
        double expectedDistance = new NormalDistribution(0, 1).inverseCumulativeProbability(0.5);
        Precision.assertClose(expectedDistance, distance1, EPSILON);

        double variance1 = new Variance().increment(cluster1.getPoints().get(0).distanceFrom(cluster1.getCenter())).increment(cluster1.getPoints().get(1).distanceFrom(cluster1.getCenter())).getValue();
        double variance2 = new Variance().increment(cluster2.getPoints().get(0).distanceFrom(cluster2.getCenter())).increment(cluster2.getPoints().get(1).distanceFrom(cluster2.getCenter())).getValue();

        double sumOfVariance = variance1 + variance2;
        double distance2 = Math.sqrt(-2.0 * Math.log(Math.random()) / sumOfVariance);
        Precision.assertClose(distance2, distance1, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfClusters() {
        clusterer.cluster(points, -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        clusterer.cluster(points, 3, -1);
    }

}

class ClusterablePoint2D implements Clusterable<ClusterablePoint2D> {

    private double[] coords;

    public ClusterablePoint2D(double[] coords) {
        this.coords = coords;
    }

    public double getX() {
        return coords[0];
    }

    public double getY() {
        return coords[1];
    }

    public double distanceFrom(ClusterablePoint2D other) {
        double[] otherCoords = other.getCoords();
        return Math.sqrt(Math.pow(otherCoords[0] - coords[0], 2) + Math.pow(otherCoords[1] - coords[1], 2));
    }

    public double[] getCoords() {
        return coords;
    }

}