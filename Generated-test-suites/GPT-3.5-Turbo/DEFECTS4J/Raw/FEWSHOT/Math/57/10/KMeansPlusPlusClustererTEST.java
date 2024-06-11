package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private List<Point> points;
    private KMeansPlusPlusClusterer<Point> clusterer;

    @Before
    public void setUp() {
        points = new ArrayList<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(7, 7));
        points.add(new Point(8, 8));
        points.add(new Point(9, 9));

        clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
    }

    @Test
    public void testCluster() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());

        Cluster<Point> cluster1 = clusters.get(0);
        assertNotNull(cluster1.getCenter());
        assertTrue(cluster1.getPoints().containsAll(Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3))));
        assertFalse(cluster1.getPoints().containsAll(Arrays.asList(new Point(7, 7), new Point(8, 8), new Point(9, 9))));

        Cluster<Point> cluster2 = clusters.get(1);
        assertNotNull(cluster2.getCenter());
        assertTrue(cluster2.getPoints().containsAll(Arrays.asList(new Point(7, 7), new Point(8, 8), new Point(9, 9))));
        assertFalse(cluster2.getPoints().containsAll(Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3))));
    }

    private static class Point implements Clusterable<Point> {

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

        public double distanceFrom(Point other) {
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            int count = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
                count++;
            }
            if (count == 0) {
                return null;
            }
            return new Point(sumX / count, sumY / count);
        }

    }

}