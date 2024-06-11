package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.Distance;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.stat.clustering.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterEmptyCollection() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = Collections.emptyList();
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 3, -1);
        assertEquals(0, clusters.size());
    }

    @Test
    public void testClusterNullCollection() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = null;
        try {
            clusterer.cluster(points, 3, -1);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClusterNegativeNumberOfClusters() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(2.0, 2.0));
        try {
            clusterer.cluster(points, -1, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClusterNumberOfPointsLessThanNumberOfClusters() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        try {
            clusterer.cluster(points, 2, -1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testClusterMaxIterationsReached() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(2.0, 2.0));
        points.add(new ClusterableTestPoint(3.0, 3.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        assertTrue(clusters.get(0).getPoints().size() > 0);
        assertTrue(clusters.get(1).getPoints().size() > 0);
    }

    @Test
    public void testClusterSamePointInDifferentClusters() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(2.0, 2.0));
        points.add(new ClusterableTestPoint(1.0, 1.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        int cluster1Size = clusters.get(0).getPoints().size();
        int cluster2Size = clusters.get(1).getPoints().size();
        assertTrue(cluster1Size > 0);
        assertTrue(cluster2Size > 0);
        assertFalse(clusters.get(0).getPoints().containsAll(clusters.get(1).getPoints()));
        assertFalse(clusters.get(1).getPoints().containsAll(clusters.get(0).getPoints()));
    }

    @Test
    public void testClusterPointsAtDifferentDistancesFromTheirCentroids() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(2.0, 2.0));
        points.add(new ClusterableTestPoint(3.0, 3.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        double cluster1CentroidX = clusters.get(0).getCenter().getX();
        double cluster1CentroidY = clusters.get(0).getCenter().getY();
        double cluster2CentroidX = clusters.get(1).getCenter().getX();
        double cluster2CentroidY = clusters.get(1).getCenter().getY();
        for (ClusterableTestPoint point : clusters.get(0).getPoints()) {
            double distance1 = Distance.euclideanDistance(point, clusters.get(0).getCenter());
            double distance2 = Distance.euclideanDistance(point, clusters.get(1).getCenter());
            assertTrue(distance1 < distance2);
        }
        for (ClusterableTestPoint point : clusters.get(1).getPoints()) {
            double distance1 = Distance.euclideanDistance(point, clusters.get(0).getCenter());
            double distance2 = Distance.euclideanDistance(point, clusters.get(1).getCenter());
            assertTrue(distance2 < distance1);
        }
    }

    @Test
    public void testClusterSplitEmptyClusterByLargestVariance() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        assertTrue(clusters.get(0).getPoints().size() > 0);
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 2, 1);
        assertEquals(2, newClusters.size());
        assertTrue(newClusters.get(0).getPoints().size() > 0);
        assertTrue(newClusters.get(1).getPoints().size() > 0);
    }

    @Test
    public void testClusterSplitEmptyClusterByLargestNumberOfPoints() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        assertTrue(clusters.get(0).getPoints().size() > 0);
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 2, 1);
        assertEquals(2, newClusters.size());
        assertTrue(newClusters.get(0).getPoints().size() > 0);
        assertTrue(newClusters.get(1).getPoints().size() > 0);
    }

    @Test
    public void testClusterSplitEmptyClusterByFarthestPoint() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        assertTrue(clusters.get(0).getPoints().size() > 0);
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 2, 1);
        assertEquals(2, newClusters.size());
        assertTrue(newClusters.get(0).getPoints().size() > 0);
        assertTrue(newClusters.get(1).getPoints().size() > 0);
    }

    @Test
    public void testClusterSplitEmptyClusterByError() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        try {
            List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testClusterSplitNonEmptyClusterByLargestVariance() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(1.1, 1.1));
        points.add(new ClusterableTestPoint(1.2, 1.2));
        points.add(new ClusterableTestPoint(1.3, 1.3));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 3, 1);
        assertEquals(3, newClusters.size());
        for (Cluster<ClusterableTestPoint> cluster : newClusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testClusterSplitNonEmptyClusterByLargestNumberOfPoints() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(1.1, 1.1));
        points.add(new ClusterableTestPoint(1.2, 1.2));
        points.add(new ClusterableTestPoint(1.3, 1.3));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 3, 1);
        assertEquals(3, newClusters.size());
        for (Cluster<ClusterableTestPoint> cluster : newClusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testClusterSplitNonEmptyClusterByFarthestPoint() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(1.1, 1.1));
        points.add(new ClusterableTestPoint(1.2, 1.2));
        points.add(new ClusterableTestPoint(1.3, 1.3));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(1, clusters.size());
        List<Cluster<ClusterableTestPoint>> newClusters = clusterer.cluster(clusters.get(0).getPoints(), 3, 1);
        assertEquals(3, newClusters.size());
        for (Cluster<ClusterableTestPoint> cluster : newClusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testClusterSplitNonEmptyClusterByError() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(1.1, 1.1));
        points.add(new ClusterableTestPoint(1.2, 1.2));
        points.add(new ClusterableTestPoint(1.3, 1.3));
        try {
            List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testClusterCentroidsDifferentFromPoints() {
        KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<ClusterableTestPoint> points = new ArrayList<>();
        points.add(new ClusterableTestPoint(1.0, 1.0));
        points.add(new ClusterableTestPoint(2.0, 2.0));
        points.add(new ClusterableTestPoint(3.0, 3.0));
        List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        for (Cluster<ClusterableTestPoint> cluster : clusters) {
            ClusterableTestPoint centroid = cluster.getCenter();
            List<ClusterableTestPoint> pointsInCluster = cluster.getPoints();
            for (ClusterableTestPoint point : pointsInCluster) {
                double distance = Distance.euclideanDistance(point, centroid);
                assertTrue(distance > 0);
            }
        }
    }

}

class ClusterableTestPoint implements Clusterable<ClusterableTestPoint> {

    private double x;
    private double y;

    public ClusterableTestPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(Clusterable<ClusterableTestPoint> obj) {
        ClusterableTestPoint other = (ClusterableTestPoint) obj;
        return Distance.euclideanDistance(this, other);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

}