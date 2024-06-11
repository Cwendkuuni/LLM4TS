package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double EPSILON = 1e-9;
    private static final int NUM_POINTS = 10;

    private Random random;
    private KMeansPlusPlusClusterer<T> clusterer;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
    }

    @Test
    public void testEmptyClusterStrategy() {
        List<T> points = new ArrayList<>();
        T point = createPoint(0.0, 0.0);
        points.add(point);
        List<Cluster<T>> clusters = clusterer.cluster(points, 2, 10);
        assertNotNull(clusters.get(0));
        assertNotNull(clusters.get(1));
        assertNull(clusters.get(1).getCenter());
        assertEquals(1, clusters.get(1).getPoints().size());
        assertEquals(point, clusters.get(1).getPoints().get(0));

        // Test all strategies
        for (EmptyClusterStrategy strategy : EmptyClusterStrategy.values()) {
            clusterer = new KMeansPlusPlusClusterer<>(random, strategy);
            points = new ArrayList<>();
            for (int i = 0; i < NUM_POINTS; i++) {
                points.add(createPoint(i, i));
            }
            List<Cluster<T>> clusters2 = clusterer.cluster(points, 3, 10);
            Cluster<T> farthestCluster = null;
            T farthestPoint = null;
            for (Cluster<T> cluster : clusters2) {
                if (cluster.getPoints().isEmpty()) {
                    switch (strategy) {
                        case LARGEST_VARIANCE:
                            assertNotNull(getPointFromLargestVarianceCluster(clusters2));
                            break;
                        case LARGEST_POINTS_NUMBER:
                            assertNotNull(getPointFromLargestNumberCluster(clusters2));
                            break;
                        case FARTHEST_POINT:
                            farthestCluster = cluster;
                            break;
                        case ERROR:
                            assertThrows(ConvergenceException.class, () -> clusterer.cluster(points, 3, 10));
                            break;
                    }
                }
            }
            if (farthestCluster != null && strategy != EmptyClusterStrategy.ERROR) {
                T farthest = getFarthestPoint(farthestCluster.getPoints());
                assertEquals(farthest, getPointFromLargestNumberCluster(clusters2).getPoint());
                assertEquals(farthest, getPointFromLargestVarianceCluster(clusters2).getPoint());
            }
        }
    }

    @Test
    public void testBasicFunctionality() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(createPoint(i, i));
        }
        List<Cluster<T>> clusters = clusterer.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }

        for (int i = 0; i < NUM_POINTS; i++) {
            T point = points.get(i);
            Cluster<T> cluster = getNearestCluster(clusters, point);
            assertEquals(point.getClusterID(), cluster.getID());
        }

        for (Cluster<T> cluster1 : clusters) {
            for (Cluster<T> cluster2 : clusters) {
                if (cluster1 != cluster2) {
                    assertEquals(0, cluster1.getPoints().size() + cluster2.getPoints().size());
                    List<Pair<T, T>> pairs = new ArrayList<>();
                    for (T point1 : cluster1.getPoints()) {
                        T nearest2 = getNearestPoint(cluster2.getPoints(), point1);
                        pairs.add(new Pair<>(point1, nearest2));
                    }
                    for (T point2 : cluster2.getPoints()) {
                        T nearest1 = getNearestPoint(cluster1.getPoints(), point2);
                        pairs.add(new Pair<>(point2, nearest1));
                    }
                    for (Pair<T, T> pair : pairs) {
                        assertEquals(pair.getFirst().getClusterID(), pair.getSecond().getClusterID());
                        assertEquals(pair.getFirst().distanceFrom(pair.getSecond()), Precision.abs(cluster1.getCenter().distanceFrom(cluster2.getCenter())), EPSILON);
                    }
                }
            }
        }
    }

    @Test
    public void testMaxIterations() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(createPoint(i, i));
        }
        List<Cluster<T>> clusters = clusterer.cluster(points, 3, -1);
        assertEquals(3, clusters.size());

        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }

        for (int i = 0; i < NUM_POINTS; i++) {
            T point = points.get(i);
            Cluster<T> cluster = getNearestCluster(clusters, point);
            assertEquals(point.getClusterID(), cluster.getID());
        }

        for (int i = 0; i < NUM_POINTS; i++) {
            T point1 = points.get(i);
            T point2 = points.get((i + 1) % NUM_POINTS);
            Cluster<T> cluster1 = getNearestCluster(clusters, point1);
            Cluster<T> cluster2 = getNearestCluster(clusters, point2);
            assertEquals(point1.getClusterID(), cluster1.getID());
            assertEquals(point2.getClusterID(), cluster2.getID());
            assertEquals(point1.distanceFrom(point2), Precision.abs(cluster1.getCenter().distanceFrom(cluster2.getCenter())), EPSILON);
        }
    }

    private T createPoint(double x, double y) {
        return new Point(x, y);
    }

    private Cluster<T> getNearestCluster(Collection<Cluster<T>> clusters, T point) {
        Cluster<T> nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = cluster;
            }
        }
        return nearest;
    }

    private T getFarthestPoint(Collection<T> points) {
        T farthest = points.get(0);
        for (T point : points) {
            if (point.distanceFrom(farthest) > farthest.distanceFrom(farthest)) {
                farthest = point;
            }
        }
        return farthest;
    }

    private T getPointFromLargestNumberCluster(Collection<Cluster<T>> clusters) {
        Cluster<T> largestCluster = Collections.max(clusters, (a, b) -> Integer.compare(a.getPoints().size(), b.getPoints().size()));
        return largestCluster.getPoints().get(0);
    }

    private T getPointFromLargestVarianceCluster(Collection<Cluster<T>> clusters) {
        Cluster<T> largestVarianceCluster = null;
        double maxVariance = Double.NEGATIVE_INFINITY;
        for (Cluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                Variance stat = new Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(point.distanceFrom(cluster.getCenter()));
                }
                double variance = stat.getResult();
                if (variance > maxVariance) {
                    maxVariance = variance;
                    largestVarianceCluster = cluster;
                }
            }
        }
        return largestVarianceCluster.getPoints().get(0);
    }

    private T getNearestPoint(Collection<T> points, T point) {
        T nearest = point;
        double minDistance = Double.MAX_VALUE;
        for (T p : points) {
            double distance = p.distanceFrom(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = p;
            }
        }
        return nearest;
    }

    private static class Point implements Clusterable<Point> {
        private final int id;
        private final double x;
        private final double y;

        public Point(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getClusterID() {
            return id;
        }

        public double distanceFrom(Clusterable<?> other) {
            if (other instanceof Point) {
                Point p = (Point) other;
                return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
            }
            throw new UnsupportedOperationException();
        }
    }
}