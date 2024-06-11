package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        assertNotNull(clusters);
        assertEquals(2, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            Collection<Point> clusterPoints = cluster.getPoints();
            assertTrue(clusterPoints.contains(new Point(1, 1)) || clusterPoints.contains(new Point(2, 2)) ||
                    clusterPoints.contains(new Point(3, 3)));
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Point> points = new ArrayList<>();
        clusterer.cluster(points, 2, 10);
    }

    private class Point implements Clusterable<Point> {

        private final double x;
        private final double y;

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

        @Override
        public double distanceFrom(Point other) {
            double dx = other.getX() - x;
            double dy = other.getY() - y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
}