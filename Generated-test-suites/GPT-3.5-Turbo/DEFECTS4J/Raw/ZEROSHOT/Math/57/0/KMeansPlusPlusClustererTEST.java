import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random());
        points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
        points.add(new Point(9, 10));
    }

    @Test
    public void testCluster() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);
        assertEquals(2, clusters.size());

        int numPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            numPoints += cluster.getPoints().size();
        }

        assertEquals(points.size(), numPoints);
    }

    @Test
    public void testChooseInitialCenters() {
        List<Cluster<Point>> clusters = clusterer.chooseInitialCenters(points, 2, new Random());
        assertEquals(2, clusters.size());

        int numPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            numPoints += cluster.getPoints().size();
        }

        assertEquals(points.size(), numPoints);
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Point> clusterPoints = new ArrayList<>();
        clusterPoints.add(new Point(1, 2));
        clusterPoints.add(new Point(1, 2));
        clusterPoints.add(new Point(3, 4));
        clusterPoints.add(new Point(3, 4));
        clusterPoints.add(new Point(5, 6));

        Cluster<Point> cluster = new Cluster<>(new Point(1, 2));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster);

        clusterer.assignPointsToClusters(clusters, clusterPoints);

        List<Point> assignedPoints = cluster.getPoints();
        assertEquals(3, assignedPoints.size());
        assertEquals(new Point(1, 2), assignedPoints.get(0));
        assertEquals(new Point(1, 2), assignedPoints.get(1));
        assertEquals(new Point(5, 6), assignedPoints.get(2));
    }

    @Test
    public void testGetNearestCluster() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));

        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point point = new Point(2, 3);

        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertEquals(cluster1, nearestCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));

        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));

        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertEquals(new Point(3, 4), point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));

        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));

        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point point = clusterer.getPointFromLargestNumberCluster(clusters);
        assertEquals(new Point(1, 2), point);
    }

    @Test
    public void testGetFarthestPoint() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 2));

        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));
        cluster2.addPoint(new Point(3, 4));

        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point point = clusterer.getFarthestPoint(clusters);
        assertEquals(new Point(1, 2), point);
    }

}