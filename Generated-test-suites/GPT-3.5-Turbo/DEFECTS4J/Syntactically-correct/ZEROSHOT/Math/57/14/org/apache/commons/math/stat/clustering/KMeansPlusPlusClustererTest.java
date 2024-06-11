import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = clusterer.cluster(points, 2, 100);

        assertEquals(2, clusters.size());
        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(2, clusters.get(1).getPoints().size());
    }

    @Test
    public void testAssignPointsToClusters() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(new Point(0, 0)));
        clusters.add(new Cluster(new Point(5, 5)));

        clusterer.assignPointsToClusters(clusters, points);

        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(2, clusters.get(1).getPoints().size());
    }

    @Test
    public void testChooseInitialCenters() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = clusterer.chooseInitialCenters(points, 2, new Random());

        assertEquals(2, clusters.size());
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(new Point(0, 0)));
        clusters.add(new Cluster(new Point(5, 5)));

        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(new Point(0, 0)));
        clusters.add(new Cluster(new Point(5, 5)));

        Point point = clusterer.getPointFromLargestNumberCluster(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetFarthestPoint() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(new Point(0, 0)));
        clusters.add(new Cluster(new Point(5, 5)));

        Point point = clusterer.getFarthestPoint(clusters);

        assertNotNull(point);
    }

    @Test
    public void testGetNearestCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        
        ArrayList<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster(new Point(0, 0)));
        clusters.add(new Cluster(new Point(5, 5)));

        Cluster<Point> cluster = clusterer.getNearestCluster(clusters, new Point(2, 2));

        assertNotNull(cluster);
    }
}

class Point implements Clusterable<Point> {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceFrom(Point p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public Point centroidOf(Collection<Point> points) {
        double sumX = 0;
        double sumY = 0;
        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
        }
        int size = points.size();
        return new Point(sumX / size, sumY / size);
    }
}