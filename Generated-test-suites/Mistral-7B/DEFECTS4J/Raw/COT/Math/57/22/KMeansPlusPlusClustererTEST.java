package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.PointMassCluster;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

/**
 * Test class for KMeansPlusPlusClusterer
 *
 * @author John Doe
 * @version 1.0
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test method for {@link KMeansPlusPlusClusterer#KMeansPlusPlusClusterer(Random)}.
     */
    @Test
    public void testConstructor() {
        final Random random = new Random();
        final KMeansPlusPlusClusterer<PointMassCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        assertNotNull(clustering);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#KMeansPlusPlusClusterer(Random, EmptyClusterStrategy)}.
     */
    @Test
    public void testConstructorWithStrategy() {
        final Random random = new Random();
        final KMeansPlusPlusClusterer<PointMassCluster> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        assertNotNull(clustering);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}.
     */
    @Test
    public void testCluster() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final Random random = new Random();
        final KMeansPlusPlusClusterer<PointMassCluster> clustering = new KMeansPlusPlusClusterer<>(random);

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = random.nextGaussian();
            y[i] = random.nextGaussian();
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final int k = 3;
        final int maxIterations = 10;
        final List<Cluster<PointMassCluster>> result = clustering.cluster(points, k, maxIterations);
        assertNotNull(result);
        assertEquals(k, result.size());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#chooseInitialCenters(Collection, int, Random)}.
     */
    @Test
    public void testChooseInitialCenters() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = i;
            y[i] = i;
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final int k = 3;
        final List<Cluster<PointMassCluster>> result = KMeansPlusPlusClusterer.chooseInitialCenters(points, k, random);
        assertNotNull(result);
        assertEquals(k, result.size());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#assignPointsToClusters(Collection, Collection)}.
     */
    @Test
    public void testAssignPointsToClusters() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final List<Cluster<PointMassCluster>> clusters = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = i;
            y[i] = i;
            clusters.add(new PointMassCluster(x[i], y[i]));
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i] + 1, y[i] + 1));
        }

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);

        for (final Cluster<PointMassCluster> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestVarianceCluster(Collection)}.
     */
    @Test
    public void testGetPointFromLargestVarianceCluster() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final List<Cluster<PointMassCluster>> clusters = new ArrayList<>();
        final Random random = new Random();
        final NormalDistribution normalDistribution = new NormalDistribution();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = normalDistribution.inverseCumulativeProbability(random.nextDouble());
            y[i] = normalDistribution.inverseCumulativeProbability(random.nextDouble());
            clusters.add(new PointMassCluster(x[i], y[i]));
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final Cluster<PointMassCluster> pointFromLargestVarianceCluster = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);
        assertEquals(1, pointFromLargestVarianceCluster.getPoints().size());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getPointFromLargestNumberCluster(Collection)}.
     */
    @Test
    public void testGetPointFromLargestNumberCluster() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final List<Cluster<PointMassCluster>> clusters = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = i;
            y[i] = i;
            clusters.add(new PointMassCluster(x[i], y[i]));
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final Cluster<PointMassCluster> pointFromLargestNumberCluster = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);
        assertEquals(1, pointFromLargestNumberCluster.getPoints().size());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getFarthestPoint(Collection)}.
     */
    @Test
    public void testGetFarthestPoint() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final List<Cluster<PointMassCluster>> clusters = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = i;
            y[i] = i;
            clusters.add(new PointMassCluster(x[i], y[i]));
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final Cluster<PointMassCluster> farthestPoint = KMeansPlusPlusClusterer.getFarthestPoint(clusters);
        assertEquals(1, farthestPoint.getPoints().size());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)}.
     */
    @Test
    public void testGetNearestCluster() {
        final int numberOfPoints = 10;
        final double[] x = new double[numberOfPoints];
        final double[] y = new double[numberOfPoints];
        final List<Cluster<PointMassCluster>> clusters = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++) {
            x[i] = i;
            y[i] = i;
            clusters.add(new PointMassCluster(x[i], y[i]));
        }

        final List<PointMassCluster> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new PointMassCluster(x[i], y[i]));
        }

        final double[] testPointsX = new double[]{2.5, 5.0, 7.5};
        final double[] testPointsY = new double[]{2.5, 5.0, 7.5};

        for (int i = 0; i < 3; i++) {
            final Cluster<PointMassCluster> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, new PointMassCluster(testPointsX[i], testPointsY[i]));
            assertEquals(1, nearestCluster.getPoints().size());
            final double distance = nearestCluster.getCenter().distanceFrom(new PointMassCluster(testPointsX[i], testPointsY[i]));
            assertEquals(0.0, distance, 0.001);
        }
    }

}