package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Point2D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random(1);
        List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point2D>> result = clusterer.cluster(points, 3, 10);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Random random = new Random(1);
        List<Clusterable<Point2D>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Point2D>> result = clusterer.cluster(points, 3, 10);
        fail("Expected ConvergenceException to be thrown");
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new Random(1);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = new Cluster<>();
            clusters.add(cluster);
            for (int j = 0; j < 5; j++) {
                cluster.addPoint(new Point2D(Math.random(), Math.random()));
            }
        }
        Point2D point = new Point2D(0.5, 0.5);
        assertEquals(clusters.get(1), getNearestCluster(clusters, point));
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Random random = new Random(1);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = new Cluster<>();
            clusters.add(cluster);
            for (int j = 0; j < 5 + (i % 2 == 0 ? 2 : 0); j++) {
                cluster.addPoint(new Point2D(Math.random(), Math.random()));
            }
        }
        assertEquals(Collections.max(clusters.stream().map(Cluster::getPoints).map(List::size).boxed().get(0)),
                getPointFromLargestNumberCluster(clusters).getPoints().size());
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Random random = new Random(1);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = new Cluster<>();
            clusters.add(cluster);
            for (int j = 0; j < 5; j++) {
                cluster.addPoint(new Point2D(Math.random(), Math.random()));
            }
            Variance variance = new Variance();
            for (Clusterable<Point2D> pointable : cluster.getPoints()) {
                variance.increment(pointable.distanceFrom(cluster.getCenter()));
            }
            cluster.setCenter(cluster.getCenter().centroidOf(cluster.getPoints()));
        }
        double maxVariance = Collections.max(clusters.stream().map(cluster -> new Variance().getResult(cluster.getPoints().stream().map(pointable -> pointable.distanceFrom(cluster.getCenter())).toArrayDouble())).boxed().get(0));
        assertEquals(maxVariance, getPointFromLargestVarianceCluster(clusters).get(0).distanceFrom(getPointFromLargestVarianceCluster(clusters).getCenter()), 0.01);
    }

    @Test
    public void testGetFarthestPoint() {
        Random random = new Random(1);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = new Cluster<>();
            clusters.add(cluster);
            for (int j = 0; j < 5; j++) {
                cluster.addPoint(new Point2D(Math.random(), Math.random()));
            }
            cluster.setCenter(cluster.getCenter().centroidOf(cluster.getPoints()));
        }
        double maxDistance = 0;
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            List<Point2D> points = cluster.getPoints();
            Point2D farthest = points.get(0);
            double distance = farthest.distanceFrom(cluster.getCenter());
            for (int j = 1; j < points.size(); j++) {
                Point2D point = points.get(j);
                double d = point.distanceFrom(cluster.getCenter());
                if (d > distance) {
                    farthest = point;
                    distance = d;
                }
            }
            maxDistance = Math.max(maxDistance, distance);
        }
        assertEquals(maxDistance, getFarthestPoint(clusters).distanceFrom(getFarthestPoint(clusters).getCenter()), 0.01);
    }

    private static Cluster<Point2D> getNearestCluster(List<Cluster<Point2D>> clusters, Point2D point) {
        Cluster<Point2D> result = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<Point2D> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                result = cluster;
            }
        }
        return result;
    }

    private static Cluster<Point2D> getPointFromLargestNumberCluster(List<Cluster<Point2D>> clusters) {
        Cluster<Point2D> result = clusters.get(0);
        for (int i = 1; i < clusters.size(); i++) {
            if (clusters.get(i).getPoints().size() > result.getPoints().size()) {
                result = clusters.get(i);
            }
        }
        return result;
    }

    private static Cluster<Point2D> getPointFromLargestVarianceCluster(List<Cluster<Point2D>> clusters) {
        Cluster<Point2D> result = clusters.get(0);
        double maxVariance = new Variance().getResult(clusters.get(0).getPoints().stream().mapToDouble(pointable -> pointable.distanceFrom(result.getCenter())).toArray());
        for (int i = 1; i < clusters.size(); i++) {
            double variance = new Variance().getResult(clusters.get(i).getPoints().stream().mapToDouble(pointable -> pointable.distanceFrom(clusters.get(i).getCenter())).toArray());
            if (variance > maxVariance) {
                maxVariance = variance;
                result = clusters.get(i);
            }
        }
        return result;
    }

    private static Cluster<Point2D> getFarthestPoint(List<Cluster<Point2D>> clusters) {
        Cluster<Point2D> result = clusters.get(0).getPoints().get(0);
        double maxDistance = result.distanceFrom(result.getCenter());
        for (Cluster<Point2D> cluster : clusters) {
            for (Point2D point : cluster.getPoints()) {
                double distance = point.distanceFrom(cluster.getCenter());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    result = point;
                }
            }
        }
        return result;
    }

}