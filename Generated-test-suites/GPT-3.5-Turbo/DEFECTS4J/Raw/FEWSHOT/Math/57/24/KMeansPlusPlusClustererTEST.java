import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterWithValidData() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));

        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithEmptyData() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();

        try {
            clusterer.cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testClusterWithInvalidKValue() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));

        try {
            clusterer.cluster(points, 0, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));

        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, -1);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithPositiveMaxIterations() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));

        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    private static class Point implements Clusterable<Point> {
        private double x;
        private double y;

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
            double deltaX = x - other.getX();
            double deltaY = y - other.getY();
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }

        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            double centerX = sumX / points.size();
            double centerY = sumY / points.size();
            return new Point(centerX, centerY);
        }
    }
}