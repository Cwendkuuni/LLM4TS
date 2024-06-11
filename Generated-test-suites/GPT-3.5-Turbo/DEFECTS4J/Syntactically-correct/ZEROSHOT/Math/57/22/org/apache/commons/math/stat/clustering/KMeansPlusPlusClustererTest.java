import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testChooseInitialCenters() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<MyPoint> points = new ArrayList<>();
        points.add(new MyPoint(0, 0));
        points.add(new MyPoint(1, 1));
        points.add(new MyPoint(2, 2));
        List<Cluster<MyPoint>> clusters = clusterer.chooseInitialCenters(points, 2, new Random());
        assertEquals(2, clusters.size());
        for (Cluster<MyPoint> cluster : clusters) {
            assertTrue(points.contains(cluster.getCenter()));
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 1);
        MyPoint p3 = new MyPoint(2, 2);
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        Cluster<MyPoint> cluster1 = new Cluster<>(p1);
        Cluster<MyPoint> cluster2 = new Cluster<>(p2);
        clusters.add(cluster1);
        clusters.add(cluster2);
        List<MyPoint> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        clusterer.assignPointsToClusters(clusters, points);
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());
        assertTrue(cluster1.getPoints().contains(p1));
        assertTrue(cluster2.getPoints().contains(p2));
    }

    @Test
    public void testGetNearestCluster() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 1);
        MyPoint p3 = new MyPoint(2, 2);
        Cluster<MyPoint> cluster1 = new Cluster<>(p1);
        Cluster<MyPoint> cluster2 = new Cluster<>(p2);
        Cluster<MyPoint> cluster3 = new Cluster<>(p3);
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        assertEquals(cluster1, clusterer.getNearestCluster(clusters, p1));
        assertEquals(cluster2, clusterer.getNearestCluster(clusters, p2));
        assertEquals(cluster3, clusterer.getNearestCluster(clusters, p3));
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 1);
        MyPoint p3 = new MyPoint(2, 2);
        Cluster<MyPoint> cluster1 = new Cluster<>(p1);
        Cluster<MyPoint> cluster2 = new Cluster<>(p2);
        Cluster<MyPoint> cluster3 = new Cluster<>(p3);
        cluster1.addPoint(p1);
        cluster1.addPoint(p2);
        cluster2.addPoint(p3);
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        MyPoint selectedPoint = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertTrue(cluster1.getPoints().contains(selectedPoint) || cluster2.getPoints().contains(selectedPoint));
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 1);
        MyPoint p3 = new MyPoint(2, 2);
        Cluster<MyPoint> cluster1 = new Cluster<>(p1);
        Cluster<MyPoint> cluster2 = new Cluster<>(p2);
        Cluster<MyPoint> cluster3 = new Cluster<>(p3);
        cluster1.addPoint(p1);
        cluster1.addPoint(p2);
        cluster2.addPoint(p3);
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        MyPoint selectedPoint = clusterer.getPointFromLargestNumberCluster(clusters);
        assertTrue(cluster1.getPoints().contains(selectedPoint) || cluster2.getPoints().contains(selectedPoint));
    }

    @Test
    public void testGetFarthestPoint() {
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 1);
        MyPoint p3 = new MyPoint(2, 2);
        Cluster<MyPoint> cluster1 = new Cluster<>(p1);
        Cluster<MyPoint> cluster2 = new Cluster<>(p2);
        Cluster<MyPoint> cluster3 = new Cluster<>(p3);
        cluster1.addPoint(p1);
        cluster2.addPoint(p2);
        cluster3.addPoint(p3);
        List<Cluster<MyPoint>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);
        MyPoint selectedPoint = clusterer.getFarthestPoint(clusters);
        assertEquals(p3, selectedPoint);
    }

}