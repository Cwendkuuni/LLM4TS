import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random());
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        points.add(new Point(5, 5));
    }

    @Test
    public void testChooseInitialCenters() {
        int k = 2;
        List<Cluster<Point>> initialCenters = clusterer.chooseInitialCenters(points, k, new Random());
        assertEquals(k, initialCenters.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(5, 5)));
        clusterer.assignPointsToClusters(clusters, points);
        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(3, clusters.get(1).getPoints().size());
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        int k = 2;
        int maxIterations = 10;
        clusterer.cluster(new ArrayList<>(), k, maxIterations);
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(5, 5)));
        Point point = new Point(2, 2);
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertEquals(clusters.get(0), nearestCluster);
    }

    private class Point implements Clusterable<Point> {

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

        @Override
        public double distanceFrom(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Point centroidOf(Collection<Point> points) {
            double totalX = x;
            double totalY = y;
            for (Point point : points) {
                totalX += point.x;
                totalY += point.y;
            }
            double centerX = totalX / (points.size() + 1);
            double centerY = totalY / (points.size() + 1);
            return new Point(centerX, centerY);
        }

    }

}