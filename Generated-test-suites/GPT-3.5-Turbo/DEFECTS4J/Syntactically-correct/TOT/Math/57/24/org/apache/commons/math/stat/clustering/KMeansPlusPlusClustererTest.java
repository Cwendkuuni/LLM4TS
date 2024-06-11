import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KMeansPlusPlusClustererTest {

    private List<Point> points;
    private KMeansPlusPlusClusterer<Point> clusterer;
    private int k;
    private int maxIterations;

    @Before
    public void setup() {
        points = new ArrayList<Point>();

        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
        points.add(new Point(9, 10));

        clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        k = 2;
        maxIterations = 10;
    }

    @Test
    public void testClusterTypical() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);

        assertNotNull(clusters);
        assertEquals(k, clusters.size());

        for (Cluster<Point> cluster : clusters) {
            assertNotNull(cluster.getPoints());
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testClusterEdgeEmpty() {
        points.clear();

        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            assertNotNull(clusters);
            assertEquals(0, clusters.size());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testClusterEdgeSinglePoint() {
        points.clear();
        points.add(new Point(1, 2));

        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            assertNotNull(clusters);
            assertEquals(1, clusters.size());
            assertEquals(1, clusters.get(0).getPoints().size());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testClusterEdgeSmallK() {
        k = 0;

        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testClusterEdgeLargeK() {
        k = points.size() + 1;

        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testClusterError() {
        maxIterations = -1;

        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            assertNotNull(clusters);
            assertEquals(k, clusters.size());

            for (Cluster<Point> cluster : clusters) {
                assertNotNull(cluster.getPoints());
                assertTrue(cluster.getPoints().size() > 0);
            }
        } catch (ConvergenceException e) {
            fail("Exception occurred during clustering");
        } catch (Exception e) {
            fail("Unexpected exception");
        }
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
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(Collection<Point> points) {
            double sumX = 0.0;
            double sumY = 0.0;

            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }

            return new Point(sumX / points.size(), sumY / points.size());
        }
    }

}