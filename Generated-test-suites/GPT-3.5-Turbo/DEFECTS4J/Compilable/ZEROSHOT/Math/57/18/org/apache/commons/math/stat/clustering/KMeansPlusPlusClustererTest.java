package org.apache.commons.math.stat.clustering;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<TestPoint> clusterer;
    private List<TestPoint> points;

    @Before
    public void setUp() {
        Random random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);

        points = new ArrayList<>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));
        points.add(new TestPoint(4, 4));
        points.add(new TestPoint(5, 5));
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 100;

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, k, maxIterations);

        assertEquals(k, clusters.size());

        for (Cluster<TestPoint> cluster : clusters) {
            List<TestPoint> clusterPoints = cluster.getPoints();
            assertTrue(clusterPoints.size() > 0);
        }
    }

    private class TestPoint implements Clusterable<TestPoint> {
        private double x;
        private double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(TestPoint other) {
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public TestPoint centroidOf(Collection<TestPoint> points) {
            double sumX = 0;
            double sumY = 0;
            for (TestPoint point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            int size = points.size();
            return new TestPoint(sumX / size, sumY / size);
        }
    }
}