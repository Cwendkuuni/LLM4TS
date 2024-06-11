import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private Collection<Point> points;

    @Before
    public void setUp() {
        Random random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testChooseInitialCenters() {
        int k = 2;
        Random random = new Random();
        List<Cluster<Point>> centers = KMeansPlusPlusClusterer.chooseInitialCenters(points, k, random);
        assertNotNull(centers);
        assertEquals(k, centers.size());
        for (Cluster<Point> cluster : centers) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        clusters.add(cluster1);
        clusters.add(cluster2);
        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(2, cluster2.getPoints().size());
        assertTrue(cluster1.getPoints().contains(new Point(1, 2)));
        assertTrue(cluster1.getPoints().contains(new Point(3, 4)));
        assertTrue(cluster2.getPoints().contains(new Point(5, 6)));
        assertTrue(cluster2.getPoints().contains(new Point(7, 8)));
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        clusters.add(cluster1);
        clusters.add(cluster2);
        Point point1 = new Point(5, 6);
        Point point2 = new Point(7, 8);
        cluster1.addPoint(point1);
        cluster1.addPoint(point2);
        Point selectedPoint = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertNotNull(selectedPoint);
        assertTrue(selectedPoint.equals(point1) || selectedPoint.equals(point2));
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        clusters.add(cluster1);
        clusters.add(cluster2);
        Point point1 = new Point(5, 6);
        Point point2 = new Point(7, 8);
        cluster1.addPoint(point1);
        cluster1.addPoint(point2);
        Point selectedPoint = clusterer.getPointFromLargestNumberCluster(clusters);
        assertNotNull(selectedPoint);
        assertTrue(selectedPoint.equals(point1) || selectedPoint.equals(point2));
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        clusters.add(cluster1);
        clusters.add(cluster2);
        Point point1 = new Point(5, 6);
        Point point2 = new Point(7, 8);
        cluster1.addPoint(point1);
        cluster1.addPoint(point2);
        Point farthestPoint = clusterer.getFarthestPoint(clusters);
        assertNotNull(farthestPoint);
        assertTrue(farthestPoint.equals(point1) || farthestPoint.equals(point2));
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        clusters.add(cluster1);
        clusters.add(cluster2);
        Point point = new Point(5, 6);
        Cluster<Point> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
        assertTrue(nearestCluster.equals(cluster2));
    }
}

class Point implements Clusterable<Point> {

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
        double totalX = 0;
        double totalY = 0;
        int count = 0;
        for (Point p : points) {
            totalX += p.getX();
            totalY += p.getY();
            count++;
        }
        double centerX = totalX / count;
        double centerY = totalY / count;
        return new Point(centerX, centerY);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Point other = (Point) obj;
        return x == other.getX() && y == other.getY();
    }
}