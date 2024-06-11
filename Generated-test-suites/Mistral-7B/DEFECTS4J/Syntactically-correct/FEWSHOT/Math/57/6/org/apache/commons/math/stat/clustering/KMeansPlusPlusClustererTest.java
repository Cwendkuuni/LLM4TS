package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.DistanceMeasure;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.Clusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Point2D;
import org.apache.commons.math.stat.clustering.PointMassCluster;
import org.apache.commons.math.stat.clustering.RandomDataGenerator;

/**
 * Test cases for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 10;
    private static final int K = 3;

    /**
     * Test clusterer with known good data set.
     */
    @Test
    public void testKMeansPlusPlusWithKnownData() {
        final double eps = 1e-5;

        // generate test data
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        List<Clusterable<Point2D>> pointsList = randomDataGenerator.nextPoints(NUM_POINTS, 2);

        // build clusterer
        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        // run clustering algorithm
        List<Cluster<Point2D>> clusters = clusterer.cluster(pointsList, K, 100);

        // check results
        for (Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            List<Point2D> clusterPoints = cluster.getPoints();
            Point2D centroid = cluster.getCenter();
            for (Point2D point : clusterPoints) {
                assertTrue(Precision.isClose(point.distanceFrom(centroid), 0.0, eps));
            }
        }

        // check centroids
        double[][] centroids = new double[K][2];
        for (int i = 0; i < K; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            Point2D centroid = cluster.getCenter();
            centroids[i][0] = centroid.getX();
            centroids[i][1] = centroid.getY();
        }
        RealMatrix expectedCentroids = new Array2DRowRealMatrix(centroids);
        RealMatrix actualCentroids = new Array2DRowRealMatrix(new double[][] {
            { 2.5141481, 0.8314288 },
            { 5.9429947, 2.0890427 },
            { 8.4141642, 4.1544971 }
        });
        double eps2 = 1e-4;
        assertTrue(PrecisionUtils.isClose(expectedCentroids.subtract(actualCentroids).determinant(), 0.0, eps2));
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                assertTrue(Precision.isClose(expectedCentroids.getEntry(i, j), actualCentroids.getEntry(i, j), eps));
            }
        }
    }

    /**
     * Test clusterer with empty data set.
     */
    @Test(expected = ConvergenceException.class)
    public void testKMeansPlusPlusWithEmptyData() {
        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point2D>> clusters = clusterer.cluster(Collections.emptyList(), K, 1);
    }

    /**
     * Test clusterer with different empty strategy.
     */
    @Test
    public void testKMeansPlusPlusWithDifferentEmptyStrategy() {
        final double eps = 1e-5;

        // generate test data
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        List<Clusterable<Point2D>> pointsList = randomDataGenerator.nextPoints(NUM_POINTS, 2);

        // build clusterer with different empty strategy
        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);

        // run clustering algorithm
        List<Cluster<Point2D>> clusters = clusterer.cluster(pointsList, K, 100);

        // check results
        for (Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            List<Point2D> clusterPoints = cluster.getPoints();
            Point2D centroid = cluster.getCenter();
            for (Point2D point : clusterPoints) {
                assertTrue(Precision.isClose(point.distanceFrom(centroid), 0.0, eps));
            }
        }

        // check centroids
        double[][] centroids = new double[K][2];
        for (int i = 0; i < K; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            Point2D centroid = cluster.getCenter();
            centroids[i][0] = centroid.getX();
            centroids[i][1] = centroid.getY();
        }
        RealMatrix expectedCentroids = new Array2DRowRealMatrix(centroids);
        RealMatrix actualCentroids = new Array2DRowRealMatrix(new double[][] {
            { 2.5141481, 0.8314288 },
            { 5.9429947, 2.0890427 },
            { 8.4141642, 4.1544971 }
        });
        double eps2 = 1e-4;
        assertTrue(PrecisionUtils.isClose(expectedCentroids.subtract(actualCentroids).determinant(), 0.0, eps2));
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                assertTrue(Precision.isClose(expectedCentroids.getEntry(i, j), actualCentroids.getEntry(i, j), eps));
            }
        }
    }

    /**
     * Test clusterer with different distance measure.
     */
    @Test
    public void testKMeansPlusPlusWithDifferentDistanceMeasure() {
        final double eps = 1e-5;

        // generate test data
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        List<Clusterable<Point2D>> pointsList = randomDataGenerator.nextPoints(NUM_POINTS, 2);

        // build clusterer with different distance measure
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), distanceMeasure, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        // run clustering algorithm
        List<Cluster<Point2D>> clusters = clusterer.cluster(pointsList, K, 100);

        // check results
        for (Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            List<Point2D> clusterPoints = cluster.getPoints();
            Point2D centroid = cluster.getCenter();
            for (Point2D point : clusterPoints) {
                assertTrue(distanceMeasure.distance(point, centroid) <= eps);
            }
        }

        // check centroids
        double[][] centroids = new double[K][2];
        for (int i = 0; i < K; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            Point2D centroid = cluster.getCenter();
            centroids[i][0] = centroid.getX();
            centroids[i][1] = centroid.getY();
        }
        RealMatrix expectedCentroids = new Array2DRowRealMatrix(centroids);
        RealMatrix actualCentroids = new Array2DRowRealMatrix(new double[][] {
            { 3.1052439, 0.5450122 },
            { 6.2878211, 1.2295856 },
            { 9.4687323, 2.8498209 }
        });
        double eps2 = 1e-4;
        assertTrue(PrecisionUtils.isClose(expectedCentroids.subtract(actualCentroids).determinant(), 0.0, eps2));
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                assertTrue(Precision.isClose(expectedCentroids.getEntry(i, j), actualCentroids.getEntry(i, j), eps));
            }
        }

        // check variance
        double[][] points = new double[NUM_POINTS][2];
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] = new Point2D(pointsList.get(i).getX(), pointsList.get(i).getY()).toArray();
        }
        double[][] pointsByCluster = new double[K][NUM_POINTS];
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < NUM_POINTS; j++) {
                pointsByCluster[i][j] = (clusters.get(i).getPoints().get(j)).toArray()[0];
            }
        }
        Variance variance = new Variance();
        for (int i = 0; i < K; i++) {
            double[] pointsInCluster = Arrays.copyOfRange(points, 0, NUM_POINTS);
            List<Clusterable<Point2D>> clusterPoints = clusters.get(i).getPoints();
            List<Point2D> clusterPointMassCentroids = new ArrayList<Point2D>();
            for (Clusterable<Point2D> point : clusterPoints) {
                clusterPointMassCentroids.add(new PointMassCluster(point));
            }
            variance.increment(new Array2DRowRealMatrix(pointsInCluster).multiply(pointsInCluster.length).subtract(new Array2DRowRealMatrix(new double[][] { new Array2DRowRealMatrix(pointsByCluster[i]).transpose().add(pointsByCluster[i].length, 2).multiply(clusterPointMassCentroids.size(), 2).mapMultiply(new Array2DRowRealMatrix(pointsByCluster[i]), new Array2RowRealMatrix(clusterPointMassCentroids)).sum(0) }).transpose().multiply(pointsInCluster.length)).getColumnVector(0));
        }
        double varianceExpected = 0.4999999999999998;
        assertTrue(Precision.isClose(variance.getResult(), varianceExpected, eps));
    }

    /**
     * Test clusterer with initial centers.
     */
    @Test
    public void testKMeansPlusPlusWithInitialCenters() {
        final double eps = 1e-5;

        // generate test data
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        List<Clusterable<Point2D>> pointsList = randomDataGenerator.nextPoints(NUM_POINTS, 2);

        // build initial centers
        double[][] initialCenters = new double[K][2];
        for (int i = 0; i < K; i++) {
            initialCenters[i][0] = i * 2.0;
            initialCenters[i][1] = i * 3.0;
        }
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), initialCenters, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        // run clustering algorithm
        List<Cluster<Point2D>> clusters = clusterer.cluster(pointsList, K, 100);

        // check results
        for (Cluster<Point2D> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            List<Point2D> clusterPoints = cluster.getPoints();
            Point2D centroid = cluster.getCenter();
            for (Point2D point : clusterPoints) {
                assertTrue(Precision.isClose(point.distanceFrom(centroid), 0.0, eps));
            }
            assertEquals(initialCenters[i][0], centroid.getX(), eps);
            assertEquals(initialCenters[i][1], centroid.getY(), eps);
        }

        // check centroids
        double[][] centroids = new double[K][2];
        for (int i = 0; i < K; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            Point2D centroid = cluster.getCenter();
            centroids[i][0] = centroid.getX();
            centroids[i][1] = centroid.getY();
        }
        RealMatrix expectedCentroids = new Array2DRowRealMatrix(centroids);
        RealMatrix actualCentroids = new Array2DRowRealMatrix(new double[][] {
            { 0.0, 0.0 },
            { 2.0, 3.0 },
            { 4.0, 6.0 }
        });
        double eps2 = 1e-4;
        assertTrue(PrecisionUtils.isClose(expectedCentroids.subtract(actualCentroids).determinant(), 0.0, eps2));
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 2; j++) {
                assertTrue(Precision.isClose(expectedCentroids.getEntry(i, j), actualCentroids.getEntry(i, j), eps));
            }
        }
    }

}