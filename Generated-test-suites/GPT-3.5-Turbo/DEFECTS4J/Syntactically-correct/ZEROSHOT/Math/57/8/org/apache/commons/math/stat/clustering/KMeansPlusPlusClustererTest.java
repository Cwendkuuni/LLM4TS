import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testChooseInitialCenters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = clusterer.chooseInitialCenters(points, 2, random);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(2, 2)));
        clusterer.assignPointsToClusters(clusters, points);
        assertEquals(1, clusters.get(0).getPoints().size());
        assertEquals(2, clusters.get(1).getPoints().size());
    }

    @Test
    public void testGetNearestCluster() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(2, 2)));
        Point testPoint = new Point(1, 1);
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, testPoint);
        assertEquals(new Point(1, 1), nearestCluster.getCenter());
    }

    @Test
    public void testCluster() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
    }

    private static class Point implements Clusterable<Point> {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(Point point) {
            double dx = x - point.getX();
            double dy = y - point.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(Collection<Point> points) {
            double centroidX = 0;
            double centroidY = 0;
            int size = points.size();
            for (Point p : points) {
                centroidX += p.getX();
                centroidY += p.getY();
            }
            return new Point(centroidX / size, centroidY / size);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}