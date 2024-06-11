import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.clustering.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        clusters = clusterer.cluster(points, 3, 100);

        assertEquals(clusters.size(), 3);
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        clusters = clusterer.chooseInitialCenters(points, 2, new Random());
        clusterer.assignPointsToClusters(clusters, points);

        for(Cluster<Point> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testChooseInitialCenters() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
        clusters = clusterer.chooseInitialCenters(points, 2, new Random());

        assertEquals(clusters.size(), 2);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        clusters.add(new Cluster<Point>(new Point(1, 2)));
        clusters.add(new Cluster<Point>(new Point(3, 4)));
        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        clusters.add(new Cluster<Point>(new Point(1, 2)));
        clusters.add(new Cluster<Point>(new Point(3, 4)));
        Point point = clusterer.getPointFromLargestNumberCluster(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        clusters.add(new Cluster<Point>(new Point(1, 2)));
        clusters.add(new Cluster<Point>(new Point(3, 4)));
        Point point = clusterer.getFarthestPoint(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        Point point = new Point(1, 2);
        Cluster<Point> cluster = clusterer.getNearestCluster(clusters, point);

        assertNotNull(cluster);
    }

    private class Point implements Clusterable {

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

        public double distanceFrom(Point point) {
            double dx = point.getX() - x;
            double dy = point.getY() - y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(Collection points) {
            double sumX = x;
            double sumY = y;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            int n = 1 + points.size();
            return new Point(sumX / n, sumY / n);
        }

        public Clusterable centroidOf(Collection cluster, Collection collection) {
            return centroidOf(cluster);
        }
    }

}