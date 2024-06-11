package org.apache.commons.math.stat.clustering; // package import

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.descriptive.moment.Variance;

/**
 * Test for the KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    private static final int DIMENSION = 2;
    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private static final double EPSILON = 1e-15;

    private Random random;
    private double[][] points;
    private List<Cluster<Point2D>> expectedClusters;

    @Before
    public void setUp() {
        random = new Random();
        points = new double[NUM_POINTS][DIMENSION];
        expectedClusters = new ArrayList<>();

        // Initialize points
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] = new double[]{Math.random(), Math.random()};
        }

        // Initialize expected clusters based on given algorithm
        expectedClusters.add(new Cluster<Point2D>(new Point2D(0.5, 0.5), Arrays.asList(points[0], points[1])));
        expectedClusters.add(new Cluster<Point2D>(new Point2D(1.5, 1.5), Arrays.asList(points[2], points[3])));
        expectedClusters.add(new Cluster<Point2D>(new Point2D(2.5, 2.5), Arrays.asList(points[4], points[5], points[6], points[7], points[8], points[9])));
    }

    @Test
    public void testClusterEmptySet() {
        Collection<Clusterable<Point2D>> emptySet = Arrays.asList();
        try {
            KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
            List<Cluster<Point2D>> clusters = clusterer.cluster(emptySet, NUM_CLUSTERS, 1);
            fail("Expected ConvergenceException for empty set");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testClusterSinglePointSet() {
        Collection<Clusterable<Point2D>> singlePointSet = Arrays.asList(new Point2D(0.0, 0.0));
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(random).cluster(singlePointSet, NUM_CLUSTERS, 1);
        assertEquals(1, clusters.size());
        assertEquals(0.0, clusters.get(0).getCenter().getX(), EPSILON);
        assertEquals(0.0, clusters.get(0).getCenter().getY(), EPSILON);
    }

    @Test
    public void testClusterPointsSet() {
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(random).cluster(Arrays.asList(points), NUM_CLUSTERS, 10);
        assertEquals(expectedClusters.size(), clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> expectedCluster = expectedClusters.get(i);
            Cluster<Point2D> actualCluster = clusters.get(i);
            assertEquals(expectedCluster.getCenter().getX(), actualCluster.getCenter().getX(), EPSILON);
            assertEquals(expectedCluster.getCenter().getY(), actualCluster.getCenter().getY(), EPSILON);
            assertEquals(expectedCluster.getPoints().size(), actualCluster.getPoints().size());
            for (int j = 0; j < expectedCluster.getPoints().size(); j++) {
                Point2D expectedPoint = expectedCluster.getPoints().get(j);
                Point2D actualPoint = actualCluster.getPoints().get(j);
                assertEquals(expectedPoint.getX(), actualPoint.getX(), EPSILON);
                assertEquals(expectedPoint.getY(), actualPoint.getY(), EPSILON);
            }
        }
    }

    @Test
    public void testEmptyClusterStrategy() {
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE).cluster(Arrays.asList(points), NUM_CLUSTERS, 10);
        assertEquals(expectedClusters.size(), clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> expectedCluster = expectedClusters.get(i);
            Cluster<Point2D> actualCluster = clusters.get(i);
            if (expectedCluster.getPoints().isEmpty()) {
                // Empty cluster strategy was applied
                if (emptyStrategyIsCorrect(expectedCluster, actualCluster)) {
                    continue;
                }
                fail("Expected empty cluster strategy to be applied correctly");
            }
            assertEquals(expectedCluster.getCenter().getX(), actualCluster.getCenter().getX(), EPSILON);
            assertEquals(expectedCluster.getCenter().getY(), actualCluster.getCenter().getY(), EPSILON);
            assertEquals(expectedCluster.getPoints().size(), actualCluster.getPoints().size());
            for (int j = 0; j < expectedCluster.getPoints().size(); j++) {
                Point2D expectedPoint = expectedCluster.getPoints().get(j);
                Point2D actualPoint = actualCluster.getPoints().get(j);
                assertEquals(expectedPoint.getX(), actualPoint.getX(), EPSILON);
                assertEquals(expectedPoint.getY(), actualPoint.getY(), EPSILON);
            }
        }
    }

    private boolean emptyStrategyIsCorrect(Cluster<Point2D> expectedCluster, Cluster<Point2D> actualCluster) {
        if (expectedCluster.getPoints().isEmpty() && actualCluster.getPoints().isEmpty()) {
            Cluster<Point2D> largestVarianceCluster = findLargestVarianceCluster(clusters);
            Point2D selectedPoint = largestVarianceCluster.getPoints().get(new Random().nextInt(largestVarianceCluster.getPoints().size()));
            assertEquals(expectedCluster.getCenter().getX(), selectedPoint.getX(), EPSILON);
            assertEquals(expectedCluster.getCenter().getY(), selectedPoint.getY(), EPSILON);
            return true;
        }
        return false;
    }

    private Cluster<Point2D> findLargestVarianceCluster(List<Cluster<Point2D>> clusters) {
        Cluster<Point2D> result = clusters.get(0);
        for (Cluster<Point2D> cluster : clusters) {
            if (cluster.getPoints().size() > result.getPoints().size()) {
                result = cluster;
            }
        }
        return result;
    }

    /**
     * Test points with very close coordinates
     */
    @Test
    public void testClosePoints() {
        double epsilon = 0.01;
        double[][] points = new double[][]{
                {1.0, 1.0},
                {1.0, 1.0001},
                {1.0001, 1.0},
                {1.0001, 1.0001}
        };
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(random).cluster(Arrays.asList(points), NUM_CLUSTERS, 10);
        assertEquals(1, clusters.size());
        Cluster<Point2D> cluster = clusters.get(0);
        assertEquals(4, cluster.getPoints().size());
        for (Point2D point : cluster.getPoints()) {
            assertTrue("Cluster points should be close", point.distanceFrom(cluster.getCenter()) < epsilon);
        }
    }

    /**
     * Test points with very different coordinates
     */
    @Test
    public void testDifferentPoints() {
        double epsilon = 0.5;
        double[][] points = new double[][]{
                {1.0, 1.0},
                {2.0, 2.0},
                {3.0, 3.0},
                {4.0, 4.0}
        };
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(random).cluster(Arrays.asList(points), NUM_CLUSTERS, 10);
        assertEquals(NUM_CLUSTERS, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            int size = cluster.getPoints().size();
            if (size > 0) {
                Point2D center = cluster.getCenter();
                double minDistance = Double.MAX_VALUE;
                for (Point2D point : cluster.getPoints()) {
                    double distance = point.distanceFrom(center);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
                assertTrue("Cluster points should be close to center", minDistance < epsilon);
            }
        }
    }

    /**
     * Test points with different dimensions
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testDifferentDimensions() {
        double[][] points = new double[][]{
                {1.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.0}
        };
        new KMeansPlusPlusClusterer<>(random).cluster(Arrays.asList(points), NUM_CLUSTERS, 10);
    }
}

class Point2D implements Clusterable<Point2D> {

    private double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceFrom(Point2D other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}