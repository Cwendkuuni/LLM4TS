package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.DistanceMeasure;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.Point2D;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy_largestVariance() {
        Random random = mock(Random.class);
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.5, 1.5));
        points.add(new Point2D(2.5, 2.5));
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(3.0, 3.0));

        List<Cluster<Point2D>> clusters = clustering.cluster(points, 3, -1);

        Cluster<Point2D> largestVarianceCluster = clusters.get(0);
        assertEquals(2, largestVarianceCluster.getPoints().size());
        assertEquals(1.5, largestVarianceCluster.getCenter().getX(), 0.001);
        assertEquals(1.5, largestVarianceCluster.getCenter().getY(), 0.001);

        Cluster<Point2D> splitCluster = clusters.get(1);
        assertEquals(1, splitCluster.getPoints().size());
        assertEquals(3.0, splitCluster.getCenter().getX(), 0.001);
        assertEquals(3.0, splitCluster.getCenter().getY(), 0.001);

        List<Point2D> expectedPoints = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        List<Point2D> actualPoints = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            actualPoints.addAll(cluster.getPoints());
        }
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testEmptyClusterStrategy_largestPointsNumber() {
        Random random = mock(Random.class);
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.5, 1.5));
        points.add(new Point2D(2.5, 2.5));
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(3.0, 3.0));

        List<Cluster<Point2D>> clusters = clustering.cluster(points, 3, -1);

        Cluster<Point2D> largestPointsNumberCluster = clusters.get(0);
        assertEquals(2, largestPointsNumberCluster.getPoints().size());
        assertEquals(3.0, largestPointsNumberCluster.getCenter().getX(), 0.001);
        assertEquals(3.0, largestPointsNumberCluster.getCenter().getY(), 0.001);

        Cluster<Point2D> splitCluster = clusters.get(1);
        assertEquals(1, splitCluster.getPoints().size());
        assertEquals(1.0, splitCluster.getCenter().getX(), 0.001);
        assertEquals(1.0, splitCluster.getCenter().getY(), 0.001);

        List<Point2D> expectedPoints = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(1.5, 1.5));
        List<Point2D> actualPoints = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            actualPoints.addAll(cluster.getPoints());
        }
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testEmptyClusterStrategy_farthestPoint() {
        Random random = mock(Random.class);
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.5, 1.5));
        points.add(new Point2D(2.5, 2.5));
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(3.0, 3.0));

        List<Cluster<Point2D>> clusters = clustering.cluster(points, 3, -1);

        Cluster<Point2D> farthestPointCluster = clusters.get(0);
        assertEquals(1, farthestPointCluster.getPoints().size());
        assertEquals(0.0, farthestPointCluster.getCenter().getX(), 0.001);
        assertEquals(0.0, farthestPointCluster.getCenter().getY(), 0.001);

        Cluster<Point2D> splitCluster = clusters.get(1);
        assertEquals(2, splitCluster.getPoints().size());
        assertEquals(1.5, splitCluster.getCenter().getX(), 0.001);
        assertEquals(1.5, splitCluster.getCenter().getY(), 0.001);

        List<Point2D> expectedPoints = Arrays.asList(new Point2D(3.0, 3.0), new Point2D(1.0, 1.0));
        List<Point2D> actualPoints = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            actualPoints.addAll(cluster.getPoints());
        }
        assertEquals(expectedPoints, actualPoints);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategy_error() {
        Random random = mock(Random.class);
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.5, 1.5));
        points.add(new Point2D(2.5, 2.5));
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(3.0, 3.0));

        List<Cluster<Point2D>> clusters = clustering.cluster(points, 3, -1);

        List<Point2D> expectedPoints = Arrays.asList(new Point2D(1.0, 1.0), new Point2D(1.5, 1.5), new Point2D(2.5, 2.5));
        List<Point2D> actualPoints = new ArrayList<>();
        for (Cluster<Point2D> cluster : clusters) {
            actualPoints.addAll(cluster.getPoints());
        }
        assertEquals(expectedPoints, actualPoints);

        when(random.nextInt(clusters.size())).thenReturn(3);
        clusters = clustering.cluster(points, 3, -1);
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        Cluster<Point2D> cluster1 = new Cluster<Point2D>(new Point2D(1.0, 1.0));
        Cluster<Point2D> cluster2 = new Cluster<Point2D>(new Point2D(2.0, 2.0));
        clusters.add(cluster1);
        clusters.add(cluster2);

        List<Point2D> points = Arrays.asList(new Point2D(3.0, 3.0), new Point2D(1.0, 1.0), new Point2D(2.0, 2.0), new Point2D(3.0, 3.0));

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);

        List<Point2D> expectedPoints1 = Arrays.asList(new Point2D(1.0, 1.0), new Point2D(1.0, 1.0));
        List<Point2D> expectedPoints2 = Arrays.asList(new Point2D(2.0, 2.0), new Point2D(3.0, 3.0));

        List<Point2D> actualPoints1 = new ArrayList<>();
        List<Point2D> actualPoints2 = new ArrayList<>();

        for (Cluster<Point2D> cluster : clusters) {
            actualPoints1.addAll(cluster.getPoints());
            actualPoints2.addAll(cluster.getPoints());
        }

        assertEquals(expectedPoints1, actualPoints1);
        assertEquals(expectedPoints2, actualPoints2);
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        Cluster<Point2D> cluster1 = new Cluster<Point2D>(new Point2D(1.0, 1.0));
        Cluster<Point2D> cluster2 = new Cluster<Point2D>(new Point2D(2.0, 2.0));
        Point2D point = new Point2D(1.5, 1.5);
        clusters.add(cluster1);
        clusters.add(cluster2);

        when(cluster1.getCenter()).thenReturn(new Point2D(1.1, 1.1));
        when(cluster2.getCenter()).thenReturn(new Point2D(2.1, 2.1));

        Cluster<Point2D> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        assertEquals(cluster1, nearestCluster);

        DistanceMeasure distanceMeasure = mock(DistanceMeasure.class);
        when(distanceMeasure.distance(point, cluster1.getCenter())).thenReturn(1.0);
        when(distanceMeasure.distance(point, cluster2.getCenter())).thenReturn(2.0);

        nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point, distanceMeasure);

        assertEquals(cluster1, nearestCluster);
    }
}