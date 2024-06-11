package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;

/**
 * JUnit tests for the KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-10;
    private static final DistanceMeasure DISTANCE = new EuclideanDistance();
    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private static final int MAX_ITERATIONS = 10;
    private static final double MEAN_VARIANCE = 1.0;
    private static final double MEAN_LARGEST_POINTS_NUMBER = 5.0;
    private static final double MEAN_FARTHEST_POINT = 7.0;

    private Random random;
    private List<Clusterable<Double>> points;
    private KMeansPlusPlusClusterer<Clusterable<Double>> clusterer;

    @Before
    public void setUp() {
        random = new Random();
        points = new ArrayList<Clusterable<Double>>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new GaussianClusterable(new NormalDistribution(i % 3 * 1.5 + 2.5, 0.3)));
        }
        clusterer = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @After
    public void tearDown() {
        random = null;
        points = null;
        clusterer = null;
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a valid number of clusters
     * and points.
     */
    @Test
    public void testValidRun() {
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        assertNotNull(clusters);
        assertEquals(NUM_CLUSTERS, clusters.size());
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(0, cluster.getPoints().size() % NUM_POINTS, 0);
        }
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a invalid number of clusters,
     * i.e. less than the number of points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfClusters() {
        clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS - 1, MAX_ITERATIONS);
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a invalid number of iterations,
     * i.e. less than zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfIterations() {
        clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, -1);
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a empty list of points.
     */
    @Test(expected = IllegalStateException.class)
    public void testEmptyListOfPoints() {
        clusterer.cluster(new ArrayList<Clusterable<Double>>(), NUM_CLUSTERS, MAX_ITERATIONS);
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a empty strategy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStrategy() {
        clusterer = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with a strategy that generates an error.
     */
    @Test(expected = ConvergenceException.class)
    public void testErrorStrategy() {
        clusterer = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
    }

    /**
     * Tests the KMeansPlusPlusClusterer when run with different strategies.
     */
    @Test
    public void testDifferentStrategies() {
        List<Cluster<Clusterable<Double>>> clusters1 = clusterer.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        EmptyClusterStrategy strategy2 = EmptyClusterStrategy.LARGEST_POINTS_NUMBER;
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer2 = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, strategy2);
        List<Cluster<Clusterable<Double>>> clusters2 = clusterer2.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        EmptyClusterStrategy strategy3 = EmptyClusterStrategy.FARTHEST_POINT;
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer3 = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, strategy3);
        List<Cluster<Clusterable<Double>>> clusters3 = clusterer3.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);

        assertNotNull(clusters1);
        assertNotNull(clusters2);
        assertNotNull(clusters3);
        assertEquals(NUM_CLUSTERS, clusters1.size());
        assertEquals(NUM_CLUSTERS, clusters2.size());
        assertEquals(NUM_CLUSTERS, clusters3.size());

        // test that the centroids are close
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster<Clusterable<Double>> cluster1 = clusters1.get(i);
            Cluster<Clusterable<Double>> cluster2 = clusters2.get(i);
            Cluster<Clusterable<Double>> cluster3 = clusters3.get(i);
            double[] centroid1 = cluster1.getCenter().toArray();
            double[] centroid2 = cluster2.getCenter().toArray();
            double[] centroid3 = cluster3.getCenter().toArray();
            assertEquals(centroid1.length, centroid2.length);
            assertEquals(centroid1.length, centroid3.length);
            for (int j = 0; j < centroid1.length; j++) {
                assertEquals(Precision.abs(centroid1[j] - centroid2[j], EPSILON), centroid1[j] - centroid2[j], EPSILON);
                assertEquals(Precision.abs(centroid1[j] - centroid3[j], EPSILON), centroid1[j] - centroid3[j], EPSILON);
            }
        }

        // test that the points are assigned to the correct clusters
        for (Cluster<Clusterable<Double>> cluster : clusters1) {
            for (Clusterable<Double> point : cluster.getPoints()) {
                Cluster<Clusterable<Double>> correctCluster = null;
                for (Cluster<Clusterable<Double>> testCluster : clusters2) {
                    if (Arrays.equals(testCluster.getCenter().toArray(), point.toArray())) {
                        correctCluster = testCluster;
                        break;
                    }
                }
                assertEquals(correctCluster, cluster);
            }
        }

        // test that the variance is correct
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster<Clusterable<Double>> cluster1 = clusters1.get(i);
            Cluster<Clusterable<Double>> cluster2 = clusters2.get(i);
            Variance variance1 = new Variance();
            for (Clusterable<Double> point : cluster1.getPoints()) {
                variance1.increment(point.distanceFrom(cluster1.getCenter()));
            }
            double variance1Value = variance1.getResult();
            Variance variance2 = new Variance();
            for (Clusterable<Double> point : cluster2.getPoints()) {
                variance2.increment(point.distanceFrom(cluster2.getCenter()));
            }
            double variance2Value = variance2.getResult();
            assertEquals(Precision.abs(variance1Value - variance2Value, EPSILON), variance1Value - variance2Value, EPSILON);
        }

        // test that the largest variance strategy works
        EmptyClusterStrategy strategy4 = EmptyClusterStrategy.LARGEST_VARIANCE;
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer4 = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, strategy4);
        List<Cluster<Clusterable<Double>>> clusters4 = clusterer4.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        Cluster<Clusterable<Double>> largestVarianceCluster = null;
        double largestVariance = Double.NEGATIVE_INFINITY;
        for (Cluster<Clusterable<Double>> cluster : clusters4) {
            Variance variance = new Variance();
            for (Clusterable<Double> point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            double varianceValue = variance.getResult();
            if (varianceValue > largestVariance) {
                largestVariance = varianceValue;
                largestVarianceCluster = cluster;
            }
        }
        List<Clusterable<Double>> largestVariancePoints = largestVarianceCluster.getPoints();
        double largestVariancePoint1Distance = largestVariancePoints.get(0).distanceFrom(largestVarianceCluster.getCenter());
        double largestVariancePoint2Distance = largestVariancePoints.get(1).distanceFrom(largestVarianceCluster.getCenter());
        // the largest variance strategy should chose the cluster with the largest variance,
        // which is the cluster with the two points that are furthest away from each other
        assertEquals(2, largestVariancePoints.size());
        assertEquals(Precision.abs(largestVariancePoint1Distance - largestVariancePoint2Distance, MEAN_FARTHEST_POINT - MEAN_FARTHEST_POINT),
                largestVariancePoint1Distance - largestVariancePoint2Distance, MEAN_FARTHEST_POINT - MEAN_FARTHEST_POINT);

        // test that the largest number of points strategy works
        EmptyClusterStrategy strategy5 = EmptyClusterStrategy.LARGEST_POINTS_NUMBER;
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer5 = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, strategy5);
        List<Cluster<Clusterable<Double>>> clusters5 = clusterer5.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        // the largest number of points strategy should choose the cluster with the most points
        Cluster<Clusterable<Double>> largestPointsCluster = null;
        int largestNumberOfPoints = 0;
        for (Cluster<Clusterable<Double>> cluster : clusters5) {
            if (cluster.getPoints().size() > largestNumberOfPoints) {
                largestNumberOfPoints = cluster.getPoints().size();
                largestPointsCluster = cluster;
            }
        }
        List<Clusterable<Double>> largestPoints = largestPointsCluster.getPoints();
        int largestNumberOfPointsPoint1Index = 0;
        int largestNumberOfPointsPoint2Index = 1;
        for (int i = 0; i < NUM_POINTS; i++) {
            if (points.get(i).equals(largestPoints.get(largestNumberOfPointsPoint1Index))) {
                largestNumberOfPointsPoint1Index = i;
            }
            if (points.get(i).equals(largestPoints.get(largestNumberOfPointsPoint2Index))) {
                largestNumberOfPointsPoint2Index = i;
            }
        }
        // the largest number of points strategy should choose the cluster with the most points,
        // which is the cluster with the two points that have the indices with the largest numbers
        assertEquals(2, largestPoints.size());
        assertEquals(largestNumberOfPointsPoint1Index, largestPoints.get(0).getIndex());
        assertEquals(largestNumberOfPointsPoint2Index, largestPoints.get(1).getIndex());
        assertEquals(NUM_POINTS / 2, largestNumberOfPoints, 0);

        // test that the farthest point strategy works
        EmptyClusterStrategy strategy6 = EmptyClusterStrategy.FARTHEST_POINT;
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer6 = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, strategy6);
        List<Cluster<Clusterable<Double>>> clusters6 = clusterer6.cluster(points.subList(0, NUM_POINTS), NUM_CLUSTERS, MAX_ITERATIONS);
        // the farthest point strategy should choose the cluster with the point that is farthest away
        Cluster<Clusterable<Double>> farthestPointCluster = null;
        double farthestPointDistance = Double.NEGATIVE_INFINITY;
        for (Cluster<Clusterable<Double>> cluster : clusters6) {
            double clusterCentroidDistance = Double.POSITIVE_INFINITY;
            for (Clusterable<Double> point : cluster.getPoints()) {
                double pointDistance = DISTANCE.distance(point, cluster.getCenter());
                if (pointDistance > clusterCentroidDistance) {
                    clusterCentroidDistance = pointDistance;
                    farthestPointCluster = cluster;
                }
            }
            double furthestPointDistance = DISTANCE.distance(farthestPointCluster.getCenter(), points.get(0));
            if (furthestPointDistance < farthestPointDistance) {
                farthestPointDistance = furthestPointDistance;
            }
        }
        List<Clusterable<Double>> farthestPoints = farthestPointCluster.getPoints();
        double farthestPoint1Distance = DISTANCE.distance(farthestPointCluster.getCenter(), farthestPoints.get(0));
        double farthestPoint2Distance = DISTANCE.distance(farthestPointCluster.getCenter(), farthestPoints.get(1));
        // the farthest point strategy should choose the cluster with the point that is farthest away,
        // which is the point with the index that has the largest distance to the centroid
        assertEquals(2, farthestPoints.size());
        assertEquals(Precision.abs(farthestPoint1Distance - farthestPoint2Distance, MEAN_FARTHEST_POINT - MEAN_FARTHEST_POINT),
                farthestPoint1Distance - farthestPoint2Distance, MEAN_FARTHEST_POINT - MEAN_FARTHEST_POINT);
    }

}