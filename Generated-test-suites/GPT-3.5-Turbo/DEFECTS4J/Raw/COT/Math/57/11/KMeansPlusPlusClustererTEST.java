import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>();
        points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());
    }

    @Test
    public void testChooseInitialCenters() {
        int k = 2;
        Random random = new Random();
        List<Cluster<Point>> clusters = clusterer.chooseInitialCenters(points, k, random);
        assertEquals(k, clusters.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(2, 2)));
        clusterer.assignPointsToClusters(clusters, points);
        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(1, clusters.get(1).getPoints().size());
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.get(0).addPoint(new Point(2, 2));
        clusters.get(0).addPoint(new Point(3, 3));
        clusters.add(new Cluster<>(new Point(10, 10)));
        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.get(0).addPoint(new Point(2, 2));
        clusters.get(0).addPoint(new Point(3, 3));
        clusters.add(new Cluster<>(new Point(10, 10)));
        Point point = clusterer.getPointFromLargestNumberCluster(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.get(0).addPoint(new Point(2, 2));
        clusters.add(new Cluster<>(new Point(10, 10)));
        Point point = clusterer.getFarthestPoint(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(1, 1)));
        clusters.add(new Cluster<>(new Point(10, 10)));
        Point point = new Point(2, 2);
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
    }
}

class Point implements Clusterable<Point> {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceFrom(Point other) {
        double dx = x - other.getX();
        double dy = y - other.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

interface Clusterable<T> {
    double distanceFrom(T other);
}