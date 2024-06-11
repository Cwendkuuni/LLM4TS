package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-5;
    private static final double DISTANCE_TOLERANCE = 1e-6;
    private List<Point2D> points;
    private Point2D centerA, centerB, centerC;
    private Cluster<Point2D> clusterA, clusterB, clusterC;
    private Clusterer<Point2D> clustering;

    @Before
    public void setUp() {
        points = new ArrayList<>();
        centerA = new Point2D(0.1, 0.1);
        centerB = new Point2D(0.3, 0.3);
        centerC = new Point2D(0.5, 0.5);
        clusterA = new Cluster<Point2D>(centerA);
        clusterB = new Cluster<Point2D>(centerB);
        clusterC = new Cluster<Point2D>(centerC);
        clustering = new KMeansPlusPlusClusterer<Point2D>(new Random());
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}.
     */
    @Test
    public void testCluster() {
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(0.4, 0.4));
        points.add(new Point2D(0.2, 0.2));
        points.add(new Point2D(0.6, 0.6));

        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 10);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertEquals(1, cluster.getSize());
            Point2D point = cluster.getPoints().get(0);
            assertEquals(true, cluster.isDense());
            assertEquals(clusterDistance(point, centerA), cluster.getDistanceToCentroid(), DISTANCE_TOLERANCE);
            assertEquals(centerA, cluster.getCenter());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with empty strategy.
     */
    @Test
    public void testClusterEmptyStrategy() {
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(0.4, 0.4));

        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 10, EmptyClusterStrategy.ERROR);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNull(cluster.getCenter());
            assertEquals(0, cluster.getSize());
            assertEquals(0.0, cluster.getDistanceToCentroid(), EPSILON);
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with largest variance strategy.
     */
    @Test
    public void testClusterLargestVarianceStrategy() {
        points.addAll(Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.4, 0.4), new Point2D(0.2, 0.2),
                new Point2D(0.6, 0.6), new Point2D(1.0, 1.0)));

        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 10, EmptyClusterStrategy.LARGEST_VARIANCE);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertEquals(Math.min(2, points.size()), cluster.getSize());
            Point2D pointA = cluster.getPoints().get(0);
            Point2D pointB = cluster.getPoints().get(1);
            assertEquals(clusterDistance(pointA, centerA), cluster.getDistanceToCentroid(), DISTANCE_TOLERANCE);
            assertEquals(centerA, cluster.getCenter());
            assertEquals(Math.pow(clusterDistance(pointA, centerB), 2.0) + Math.pow(clusterDistance(pointB, centerB), 2.0),
                    cluster.getVariance(), EPSILON);
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with largest number of points strategy.
     */
    @Test
    public void testClusterLargestPointsNumberStrategy() {
        points.addAll(Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.4, 0.4), new Point2D(0.2, 0.2),
                new Point2D(0.6, 0.6), new Point2D(1.0, 1.0)));

        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 10, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertEquals(Math.min(3, points.size()), cluster.getSize());
            Point2D pointA = cluster.getPoints().get(0);
            Point2D pointB = cluster.getPoints().get(1);
            Point2D pointC = cluster.getPoints().get(2);

            assertEquals(clusterDistance(pointA, centerA), cluster.getDistanceToCentroid(), DISTANCE_TOLERANCE);
            assertEquals(centerA, cluster.getCenter());

            int indexA = points.indexOf(pointA);
            int indexB = points.indexOf(pointB);
            int indexC = points.indexOf(pointC);

            // If the strategy is Largest Points Number, the two first points are in the
            // same cluster.
            assertEquals(indexA, indexB);

            // So, the third point is in the third cluster.
            assertEquals(Collections.singletonList(points.get(indexA + 1)), result.get(2).getPoints());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with farthest point strategy.
     */
    @Test
    public void testClusterFarthestPointStrategy() {
        points.addAll(Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.4, 0.4), new Point2D(0.2, 0.2),
                new Point2D(0.6, 0.6), new Point2D(1.0, 1.0)));

        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 10, EmptyClusterStrategy.FARTHEST_POINT);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertEquals(1, cluster.getSize());
            Point2D point = cluster.getPoints().get(0);
            Point2D expectedCenter;

            if (cluster.equals(clusterA)) {
                expectedCenter = centerA;
            } else if (cluster.equals(clusterB)) {
                expectedCenter = centerB;
            } else {
                expectedCenter = centerC;
            }

            assertEquals(clusterDistance(point, expectedCenter), cluster.getDistanceToCentroid(), DISTANCE_TOLERANCE);
            assertEquals(expectedCenter, cluster.getCenter());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#chooseInitialCenters(Collection, int, Random)}.
     */
    @Test
    public void testChooseInitialCenters() {
        points.addAll(Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.4, 0.4), new Point2D(0.2, 0.2),
                new Point2D(0.6, 0.6), new Point2D(1.0, 1.0)));

        List<Cluster<Point2D>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, 3, new Random());

        assertNotNull(initialCenters);
        assertEquals(3, initialCenters.size());
        for (Cluster<Point2D> cluster : initialCenters) {
            assertEquals(1, cluster.getSize());
            Point2D point = cluster.getPoints().get(0);
            assertEquals(true, cluster.isDense());
            assertEquals(clusterDistance(point, centerA), cluster.getDistanceToCentroid(), DISTANCE_TOLERANCE);
            assertEquals(centerA, cluster.getCenter());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#assignPointsToClusters(Collection, Collection)}.
     */
    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point2D>> clusters = Arrays.asList(clusterA, clusterB, clusterC);
        List<Point2D> pointsToAdd = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(0.4, 0.4),
                new Point2D(0.2, 0.2), new Point2D(0.6, 0.6), new Point2D(1.0, 1.0));

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, pointsToAdd);

        for (Cluster<Point2D> cluster : clusters) {
            assertEquals(Math.min(3, pointsToAdd.size()), cluster.getSize());
            for (Point2D point : cluster.getPoints()) {
                assertEquals(clusterDistance(point, cluster.getCenter()), point.distanceFrom(cluster.getCenter()),
                        DISTANCE_TOLERANCE);
            }
        }

        for (Point2D point : pointsToAdd) {
            assertNull(point.getCluster());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Point2D)}.
     */
    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point2D>> clusters = Arrays.asList(clusterA, clusterB, clusterC);
        List<Point2D> points = Arrays.asList(new Point2D(0.2, 0.2), new Point2D(0.6, 0.6), new Point2D(1.0, 1.0));

        for (Point2D point : points) {
            Cluster<Point2D> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
            assertEquals(nearestCluster, getNearestCluster(clusters, point));
            assertEquals(nearestCluster.getCenter(), point.nearest(clusters));
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Point2D)} when all clusters are empty.
     */
    @Test(expected = ConvergenceException.class)
    public void testGetNearestClusterWhenAllClustersAreEmpty() {
        List<Cluster<Point2D>> clusters = Collections.emptyList();
        Point2D point = new Point2D(0.0, 0.0);
        KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
    }

    private double clusterDistance(Point2D p1, Point2D p2) {
        return Math.pow(Math.pow(p1.getX() - p2.getX(), 2.0) + Math.pow(p1.getY() - p2.getY(), 2.0), 0.5);
    }

    private Cluster<Point2D> getNearestCluster(List<Cluster<Point2D>> clusters, Point2D point) {
        Cluster<Point2D> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<Point2D> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

}