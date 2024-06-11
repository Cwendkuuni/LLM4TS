package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;

public class KMeansPlusPlusClustererTest {

    /**
     * Test clustering of equal points
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testEqualPoints() throws NumberIsTooSmallException {
        Random rand = new Random(1);
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Clusterable> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(i, i));
        }
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (Cluster<Clusterable> cluster : clusters) {
            assertEquals(3, cluster.getPoints().size());
            for (Clusterable point : cluster.getPoints()) {
                assertEquals(point.getX(), cluster.getCenter().getX(), 0.001);
                assertEquals(point.getY(), cluster.getCenter().getY(), 0.001);
            }
        }
    }

    /**
     * Test clustering of points with different distances
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testDifferentDistances() throws NumberIsTooSmallException {
        Random rand = new Random(1);
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Clusterable> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(i, i));
        }
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        Cluster<Clusterable> cluster1 = clusters.get(0);
        Cluster<Clusterable> cluster2 = clusters.get(1);
        Cluster<Clusterable> cluster3 = clusters.get(2);
        Point p1 = (Point) cluster1.getPoints().get(0);
        Point p2 = (Point) cluster1.getPoints().get(1);
        Point p3 = (Point) cluster1.getPoints().get(2);
        Point q1 = (Point) cluster2.getPoints().get(0);
        Point q2 = (Point) cluster2.getPoints().get(1);
        Point q3 = (Point) cluster2.getPoints().get(2);
        Point r1 = (Point) cluster3.getPoints().get(0);
        Point r2 = (Point) cluster3.getPoints().get(1);
        Point r3 = (Point) cluster3.getPoints().get(2);
        // check distances
        double distance11 = p1.distanceFrom(p1);
        double distance12 = p1.distanceFrom(p2);
        double distance13 = p1.distanceFrom(p3);
        double distance21 = p2.distanceFrom(p1);
        double distance22 = p2.distanceFrom(p2);
        double distance23 = p2.distanceFrom(p3);
        double distance31 = p3.distanceFrom(p1);
        double distance32 = p3.distanceFrom(p2);
        double distance33 = p3.distanceFrom(p3);
        double distance1c = DistanceMeasure.euclideanDistance(p1, cluster1.getCenter());
        double distance2c = DistanceMeasure.euclideanDistance(p2, cluster1.getCenter());
        double distance3c = DistanceMeasure.euclideanDistance(p3, cluster1.getCenter());
        double distanceqc = DistanceMeasure.euclideanDistance(q1, cluster2.getCenter());
        double distancerc = DistanceMeasure.euclideanDistance(r1, cluster3.getCenter());
        // check that points are in their respective clusters
        assertTrue(cluster1.getPoints().contains(p1));
        assertTrue(cluster1.getPoints().contains(p2));
        assertTrue(cluster1.getPoints().contains(p3));
        assertTrue(cluster2.getPoints().contains(q1));
        assertTrue(cluster2.getPoints().contains(q2));
        assertTrue(cluster2.getPoints().contains(q3));
        assertTrue(cluster3.getPoints().contains(r1));
        assertTrue(cluster3.getPoints().contains(r2));
        assertTrue(cluster3.getPoints().contains(r3));
        // check that centers are in their respective clusters
        assertTrue(cluster1.getPoints().contains(cluster1.getCenter()));
        assertTrue(cluster2.getPoints().contains(cluster2.getCenter()));
        assertTrue(cluster3.getPoints().contains(cluster3.getCenter()));
        // check that the distances between points and their clusters are minimum
        for (Cluster<Clusterable> cluster : clusters) {
            for (Clusterable point : cluster.getPoints()) {
                assertEquals(DistanceMeasure.minDistance(point, cluster.getPoints()), distanceFromCenter(point, cluster), 0.001);
            }
        }
        // check that the distances between centers and their closest points are minimum
        for (Cluster<Clusterable> cluster : clusters) {
            Clusterable closestPoint = findClosestPoint(cluster.getPoints(), cluster.getCenter());
            assertEquals(DistanceMeasure.euclideanDistance(closestPoint, cluster.getCenter()), distanceFromCenter(closestPoint, cluster), 0.001);
        }
        // check that the distance between centers is greater than the distance between any two points
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = 0; j < clusters.size(); j++) {
                if (i != j) {
                    double distance = DistanceMeasure.euclideanDistance(clusters.get(i).getCenter(), clusters.get(j).getCenter());
                    for (Clusterable pointA : clusters.get(i).getPoints()) {
                        for (Clusterable pointB : clusters.get(j).getPoints()) {
                            double distancePointToPoint = DistanceMeasure.euclideanDistance(pointA, pointB);
                            assertTrue(distance > distancePointToPoint);
                        }
                    }
                }
            }
        }
    }

    /**
     * Test clustering of points with empty clusters
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testEmptyClusters() throws NumberIsTooSmallException {
        Random rand = new Random(1);
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Clusterable> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(i, i));
        }
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 1, 1);
        assertEquals(1, clusters.size());
        Cluster<Clusterable> cluster = clusters.get(0);
        List<Clusterable> emptyCluster = new ArrayList<>();
        // test error strategy
        KMeansPlusPlusClusterer<Clusterable> clustererError = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable>> clustersError = clustererError.cluster(points, 1, 1);
        assertEquals(1, clustersError.size());
        Cluster<Clusterable> clusterError = clustersError.get(0);
        try {
            clusterError.getCenter();
            fail("An error should have been thrown, but no error was.");
        } catch (ConvergenceException e) {
            // expected error
        }
        List<Clusterable> oldPoints = new ArrayList<>(points);
        points.remove(0);
        clusters = clusterer.cluster(points, 1, 1);
        assertEquals(1, clusters.size());
        cluster = clusters.get(0);
        // test largest variance strategy
        KMeansPlusPlusClusterer<Clusterable> clustererVariance = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable>> clustersVariance = clustererVariance.cluster(points, 1, 1);
        assertEquals(1, clustersVariance.size());
        cluster = clustersVariance.get(0);
        // check points are in their respective clusters
        for (Cluster<Clusterable> cluster1 : Collections.singletonList(cluster)) {
            for (Clusterable point : oldPoints) {
                assertTrue(cluster1.getPoints().contains(point));
            }
        }
        // check distance from each point to its center
        for (Clusterable point : oldPoints) {
            Cluster<Clusterable> nearestCluster = getNearestCluster(clusters, point);
            Cluster<Clusterable> clusterPoint = nearestCluster;
            assertEquals(point, clusterPoint.getPoints().get(0));
            assertEquals(point, clusterPoint.getCenter());
            assertEquals(0.0, DistanceMeasure.euclideanDistance(point, point), 0.0);
            double distance = DistanceMeasure.euclideanDistance(point, clusterPoint.getCenter());
            assertTrue(distance > 0.0);
            // check distance from center to each point in the cluster
            for (Clusterable point : clusterPoint.getPoints()) {
                assertEquals(point, point);
                assertEquals(point, clusterPoint.getCenter());
                assertEquals(0.0, DistanceMeasure.euclideanDistance(point, point), 0.0);
                double distanceCenter = DistanceMeasure.euclideanDistance(point, clusterPoint.getCenter());
                assertEquals(distance, distanceCenter, 0.001);
            }
        }
        // check distance between centers of different clusters
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Clusterable> cluster1 = clusters.get(i);
            for (int j = i + 1; j < clusters.size(); j++) {
                Cluster<Clusterable> cluster2 = clusters.get(j);
                double distance = DistanceMeasure.euclideanDistance(cluster1.getCenter(), cluster2.getCenter());
                assertTrue(distance > 0.0);
            }
        }
    }

    /**
     * Test clustering of points with different strategies
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testDifferentStrategies() throws NumberIsTooSmallException {
        Random rand = new Random(1);
        KMeansPlusPlusClusterer<Clusterable> clustererLargestVariance = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_VARIANCE);
        KMeansPlusPlusClusterer<Clusterable> clustererLargestPointsNumber = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        KMeansPlusPlusClusterer<Clusterable> clustererFarthestPoint = new KMeansPlusPlusClusterer<>(rand, EmptyClusterStrategy.FARTHEST_POINT);
        List<Clusterable> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(i, i));
        }
        List<Cluster<Clusterable>> clustersLargestVariance = clustererLargestVariance.cluster(points, 3, 10);
        List<Cluster<Clusterable>> clustersLargestPointsNumber = clustererLargestPointsNumber.cluster(points, 3, 10);
        List<Cluster<Clusterable>> clustersFarthestPoint = clustererFarthestPoint.cluster(points, 3, 10);
        // check that the clusters have the same points
        for (int i = 0; i < clustersLargestVariance.size(); i++) {
            Cluster<Clusterable> cluster1 = clustersLargestVariance.get(i);
            Cluster<Clusterable> cluster2 = clustersLargestPointsNumber.get(i);
            Cluster<Clusterable> cluster3 = clustersFarthestPoint.get(i);
            List<Clusterable> points1 = cluster1.getPoints();
            List<Clusterable> points2 = cluster2.getPoints();
            List<Clusterable> points3 = cluster3.getPoints();
            assertEquals(points1.size(), points2.size());
            assertEquals(points1.size(), points3.size());
            for (int j = 0; j < points1.size(); j++) {
                assertEquals(points1.get(j), points2.get(j));
                assertEquals(points1.get(j), points3.get(j));
            }
        }
        // check that the centers are not the same
        for (int i = 0; i < clustersLargestVariance.size(); i++) {
            Cluster<Clusterable> cluster1 = clustersLargestVariance.get(i);
            Cluster<Clusterable> cluster2 = clustersLargestPointsNumber.get(i);
            Cluster<Clusterable> cluster3 = clustersFarthestPoint.get(i);
            assertNotEquals(cluster1.getCenter(), cluster2.getCenter());
            assertNotEquals(cluster1.getCenter(), cluster3.getCenter());
            assertNotEquals(cluster2.getCenter(), cluster3.getCenter());
        }
    }

    private static double distanceFromCenter(Clusterable point, Cluster<Clusterable> cluster) {
        return DistanceMeasure.euclideanDistance(point, cluster.getCenter());
    }

    private static Clusterable findClosestPoint(List<Clusterable> points, Clusterable center) {
        Clusterable closestPoint = points.get(0);
        double closestDistance = DistanceMeasure.euclideanDistance(closestPoint, center);
        for (Clusterable point : points) {
            double distance = DistanceMeasure.euclideanDistance(point, center);
            if (distance < closestDistance) {
                closestPoint = point;
                closestDistance = distance;
            }
        }
        return closestPoint;
    }

    private static Cluster<Clusterable> getNearestCluster(List<Cluster<Clusterable>> clusters, Clusterable point) {
        Cluster<Clusterable> nearestCluster = clusters.get(0);
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster<Clusterable> cluster : clusters) {
            double distance = DistanceMeasure.euclideanDistance(point, cluster.getCenter());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    /**
     * Test point class
     */
    private static class Point implements Clusterable<Point> {

        private double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(Clusterable<?> other) {
            Point otherPoint = (Point) other;
            return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
        }

    }

}