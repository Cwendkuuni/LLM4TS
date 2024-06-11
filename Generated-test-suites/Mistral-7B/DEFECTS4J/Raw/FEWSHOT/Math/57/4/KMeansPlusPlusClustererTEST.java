package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {
    @SuppressWarnings("unchecked")
    private Cluster<T> createCluster(T center) {
        return new Cluster<T>() {
            private final List<T> points = new ArrayList<T>();

            @Override
            public T getCenter() {
                return center;
            }

            @Override
            public void addPoint(T point) {
                points.add(point);
            }

            @Override
            public int size() {
                return points.size();
            }

            @Override
            public List<T> getPoints() {
                return points;
            }
        };
    }

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (Cluster<DoublePoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(1, cluster.size());
        }
    }

    @Test
    public void testClusterInitialCenters() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, -1);
        Collection<DoublePoint> initialCenters = Arrays.asList(
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.5, 2.5),
                new DoublePoint(3.5, 3.5)
        );
        assertEquals(initialCenters.size(), clusters.size());
        for (int i = 0; i < initialCenters.size(); i++) {
            Cluster<DoublePoint> cluster = clusters.get(i);
            DoublePoint center = cluster.getCenter();
            assertEquals(initialCenters.get(i).getX(), center.getX(), 0.001);
            assertEquals(initialCenters.get(i).getY(), center.getY(), 0.001);
        }
    }

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        assertTrue(clusters.get(3).getPoints().isEmpty());
        try {
            clusters = clusterer.cluster(points, 3, 10);
        } catch (ConvergenceException e) {
            // expected
        }
        assertNull(clusters);
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5),
                new DoublePoint(4.0, 4.0),
                new DoublePoint(4.5, 4.5)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        DoublePoint center1 = new DoublePoint(1.5, 1.5);
        DoublePoint center2 = new DoublePoint(3.0, 3.0);
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertEquals(center1, clusters.get(1).getCenter());
        assertEquals(1, clusters.get(1).size());
        assertTrue(clusters.get(2).getPoints().isEmpty());
        assertEquals(center2, clusters.get(2).getCenter());
        assertEquals(1, clusters.get(2).size());
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5),
                new DoublePoint(4.0, 4.0),
                new DoublePoint(4.5, 4.5)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        DoublePoint center1 = new DoublePoint(1.5, 1.5);
        DoublePoint center2 = new DoublePoint(3.0, 3.0);
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertEquals(center2, clusters.get(1).getCenter());
        assertEquals(1, clusters.get(1).size());
        assertTrue(clusters.get(2).getPoints().isEmpty());
        assertEquals(center1, clusters.get(2).getCenter());
        assertEquals(1, clusters.get(2).size());
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        Collection<DoublePoint> points = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5),
                new DoublePoint(4.0, 4.0),
                new DoublePoint(4.5, 4.5)
        );
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        DoublePoint center1 = new DoublePoint(1.5, 1.5);
        DoublePoint center2 = new DoublePoint(3.0, 3.0);
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertEquals(center2, clusters.get(1).getCenter());
        assertEquals(1, clusters.get(1).size());
        assertTrue(clusters.get(2).getPoints().isEmpty());
        assertEquals(center1, clusters.get(2).getCenter());
        assertEquals(1, clusters.get(2).size());

        List<DoublePoint> points2 = Arrays.asList(
                new DoublePoint(1.0, 1.0),
                new DoublePoint(1.5, 1.5),
                new DoublePoint(2.0, 2.0),
                new DoublePoint(3.0, 3.0),
                new DoublePoint(3.5, 3.5),
                new DoublePoint(4.0, 4.0),
                new DoublePoint(4.5, 4.5),
                new DoublePoint(5.0, 5.0),
                new DoublePoint(5.5, 5.5)
        );
        clusters = clusterer.cluster(points2, 3, 10);
        assertEquals(3, clusters.size());
        DoublePoint center3 = new DoublePoint(5.0, 5.0);
        DoublePoint center4 = new DoublePoint(4.5, 4.5);
        assertTrue(clusters.get(0).getPoints().isEmpty());
        assertEquals(center4, clusters.get(1).getCenter());
        assertEquals(1, clusters.get(1).size());
        assertTrue(clusters.get(2).getPoints().isEmpty());
        assertEquals(center3, clusters.get(2).getCenter());
        assertEquals(1, clusters.get(2).size());
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<DoublePoint>> clusters = new ArrayList<Cluster<DoublePoint>>();
        DoublePoint p = new DoublePoint(1.5, 1.5);
        for (int i = 0; i < 4; i++) {
            Cluster<DoublePoint> cluster = createCluster(new DoublePoint(i * 0.5, i * 0.5));
            clusters.add(cluster);
        }
        for (Cluster<DoublePoint> cluster : clusters) {
            Cluster<DoublePoint> nearestCluster = getNearestCluster(clusters, p);
            assertEquals(cluster, nearestCluster);
        }
    }

    private double distance(DoublePoint p1, DoublePoint p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2.0) + Math.pow(p1.getY() - p2.getY(), 2.0));
    }

    @Test
    public void testGetNearestClusterWithUniformlyDistributedPoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        Collection<DoublePoint> points = new ArrayList<DoublePoint>();
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 10.0);
        for (int i = 0; i < 100; i++) {
            points.add(new DoublePoint(distribution.inverseCumulativeProbability(random.nextDouble()),
                                       distribution.inverseCumulativeProbability(random.nextDouble())));
        }

        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points, 5, 100);
        DoublePoint p = new DoublePoint(6.0, 6.0);
        Cluster<DoublePoint> nearestCluster = getNearestCluster(clusters, p);
        DoublePoint center = nearestCluster.getCenter();
        double minDistance = Double.MAX_VALUE;
        for (Cluster<DoublePoint> cluster : clusters) {
            double distance = distance(p, cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        assertEquals(nearestCluster, clusters.get(4));
        assertTrue(Precision.isClose(minDistance, distance(p, center), 0.01));
    }

    public static class DoublePoint implements Clusterable<DoublePoint> {
        private final double x;
        private final double y;

        public DoublePoint(double x, double y) {
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
            DoublePoint otherPoint = (DoublePoint) other;
            return distance(this, otherPoint);
        }

        @Override
        public String toString() {
            return "DoublePoint[x=" + x + ", y=" + y + "]";
        }
    }
}