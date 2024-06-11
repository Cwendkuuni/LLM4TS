package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.clustering.DistanceMeasure;
import org.apache.commons.math.clustering.Cluster;
import org.apache.commons.math.clustering.Point2D;
import org.apache.commons.math.clustering.distance.EuclideanDistance;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private List<Cluster<Point2D>> clusters;
    private List<Point2D> points;
    private KMeansPlusPlusClusterer<Point2D> kMeansPlusPlusClusterer;
    private MersenneTwister random;

    @Before
    public void setUp() {
        random = new MersenneTwister();
        kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        points = new ArrayList<>();
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(2.0, 1.0));
        points.add(new Point2D(3.0, 3.0));
        clusters = kMeansPlusPlusClusterer.cluster(points, 3, -1);
    }

    @Test
    public void testCluster() {
        assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        clusters.clear();
        points.clear();
        kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        clusters = kMeansPlusPlusClusterer.cluster(points, 3, -1);
    }

    @Test
    public void testClusterWithLargestVarianceStrategy() {
        Cluster<Point2D> largestVarianceCluster = getClusterWithLargestVariance(clusters);
        assertEquals(3, largestVarianceCluster.size());

        Cluster<Point2D> newCluster = new Cluster<>(new Point2D(0.0, 0.0));
        clusters.set(0, newCluster);

        List<Cluster<Point2D>> newClusters = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            if (!cluster.isEmpty()) {
                T point = getFarthestPoint(cluster);
                newClusters.add(new Cluster<>(point));
            }
        }

        clusters = newClusters;
        assertEquals(4, clusters.size());
        assertFalse(largestVarianceCluster.isEmpty());
    }

    @Test
    public void testClusterWithLargestNumberOfPointsStrategy() {
        Cluster<Point2D> largestNumberCluster = getClusterWithLargestNumberOfPoints(clusters);
        assertEquals(3, largestNumberCluster.size());

        Cluster<Point2D> newCluster = new Cluster<>(new Point2D(0.0, 0.0));
        clusters.set(0, newCluster);

        List<Cluster<Point2D>> newClusters = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            if (!cluster.isEmpty()) {
                T point = cluster.getPoints().get(0);
                newClusters.add(new Cluster<>(point));
            }
        }

        clusters = newClusters;
        assertEquals(4, clusters.size());
        assertFalse(largestNumberCluster.isEmpty());
    }

    @Test
    public void testClusterWithFarthestPointStrategy() {
        Cluster<Point2D> farthestPointCluster = getClusterWithFarthestPoint(clusters);
        assertEquals(3, farthestPointCluster.size());

        Cluster<Point2D> newCluster = new Cluster<>(new Point2D(0.0, 0.0));
        clusters.set(0, newCluster);

        List<Cluster<Point2D>> newClusters = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            if (!cluster.isEmpty()) {
                T point = cluster.getCenter();
                newClusters.add(new Cluster<>(point));
            }
        }

        clusters = newClusters;
        assertEquals(4, clusters.size());
        assertFalse(farthestPointCluster.isEmpty());
    }

    @Test
    public void testGetNearestCluster() {
        Cluster<Point2D> nearestCluster = getNearestCluster(clusters, new Point2D(1.5, 1.5));
        assertSame(clusters.get(1), nearestCluster);
    }

    private Cluster<Point2D> getClusterWithLargestVariance(List<Cluster<Point2D>> clusters) {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<Point2D> selected = null;
        for (Cluster<Point2D> cluster : clusters) {
            if (!cluster.isEmpty()) {

                // compute the distance variance of the current cluster
                Variance stat = new Variance();
                for (Point2D point : cluster.getPoints()) {
                    stat.increment(point.distanceFrom(cluster.getCenter()));
                }
                double variance = stat.getResult();

                // select the cluster with the largest variance
                if (variance > maxVariance) {
                    maxVariance = variance;
                    selected = cluster;
                }

            }
        }
        return selected;
    }

    private Cluster<Point2D> getClusterWithLargestNumberOfPoints(List<Cluster<Point2D>> clusters) {
        int maxNumber = 0;
        Cluster<Point2D> selected = null;
        for (Cluster<Point2D> cluster : clusters) {

            // get the number of points of the current cluster
            int number = cluster.getPoints().size();

            // select the cluster with the largest number of points
            if (number > maxNumber) {
                maxNumber = number;
                selected = cluster;
            }

        }
        return selected;
    }

    private Cluster<Point2D> getClusterWithFarthestPoint(List<Cluster<Point2D>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<Point2D> selectedCluster = null;
        int selectedPoint = -1;
        for (Cluster<Point2D> cluster : clusters) {

            // get the farthest point
            Point2D center = cluster.getCenter();
            List<Point2D> points = cluster.getPoints();
            for (int i = 0; i < points.size(); ++i) {
                double distance = points.get(i).distanceFrom(center);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    selectedCluster = cluster;
                    selectedPoint = i;
                }
            }

        }
        return selectedCluster;
    }

    private T getFarthestPoint(Cluster<Point2D> cluster) {
        Point2D center = cluster.getCenter();
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        double maxDistance = Double.NEGATIVE_INFINITY;
        T farthestPoint = null;
        for (Point2D point : cluster.getPoints()) {
            double distance = distanceMeasure.distance(point, center);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPoint = point;
            }
        }
        return farthestPoint;
    }

    private T getPointFromMockedCluster() {
        Cluster<Point2D> mockedCluster = mock(Cluster.class);
        when(mockedCluster.getCenter()).thenReturn(new Point2D(1.0, 1.0));
        when(mockedCluster.getPoints()).thenReturn(Collections.singletonList(new Point2D(0.0, 0.0)));
        return mockedCluster.getPoints().get(0);
    }

}