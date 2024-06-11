
import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testChooseInitialCenters() {
        // Test the chooseInitialCenters method
        
        // Test case with empty points
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        List<Cluster> initialCenters = clusterer.chooseInitialCenters(new ArrayList<Point>(), 5, new Random());
        assertEquals(0, initialCenters.size());
        
        // Test case with non-empty points
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        points.add(new Point(5, 5));
        
        initialCenters = clusterer.chooseInitialCenters(points, 3, new Random());
        assertEquals(3, initialCenters.size());
    }
    
    @Test
    public void testAssignPointsToClusters() {
        // Test the assignPointsToClusters method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        Cluster cluster1 = new Cluster(new Point(1, 1));
        Cluster cluster2 = new Cluster(new Point(2, 2));
        List<Cluster> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        
        clusterer.assignPointsToClusters(clusters, points);
        
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(2, cluster2.getPoints().size());
    }
    
    @Test
    public void testCluster() {
        // Test the cluster method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        
        // Test case with empty points
        List<Cluster> clusters = clusterer.cluster(new ArrayList<Point>(), 5, 10);
        assertEquals(0, clusters.size());
        
        // Test case with non-empty points
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.add(new Point(2, 2));
        points.add(new Point(2, 3));
        points.add(new Point(3, 3));
        
        clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
    }
    
    @Test
    public void testGetPointFromLargestVarianceCluster() {
        // Test the getPointFromLargestVarianceCluster method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(new Point(1, 1));
        cluster1.addPoint(new Point(1, 1));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 3));
        clusters.add(cluster1);
        Cluster cluster2 = new Cluster(new Point(2, 2));
        cluster2.addPoint(new Point(2, 2));
        cluster2.addPoint(new Point(2, 3));
        clusters.add(cluster2);
        
        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(2, cluster2.getPoints().size());
    }
    
    @Test
    public void testGetPointFromLargestNumberCluster() {
        // Test the getPointFromLargestNumberCluster method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(new Point(1, 1));
        cluster1.addPoint(new Point(1, 1));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 3));
        clusters.add(cluster1);
        Cluster cluster2 = new Cluster(new Point(2, 2));
        cluster2.addPoint(new Point(2, 2));
        cluster2.addPoint(new Point(2, 3));
        clusters.add(cluster2);
        
        Point point = clusterer.getPointFromLargestNumberCluster(clusters);
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());
    }
    
    @Test
    public void testGetFarthestPoint() {
        // Test the getFarthestPoint method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(new Point(1, 1));
        cluster1.addPoint(new Point(1, 1));
        cluster1.addPoint(new Point(1, 2));
        cluster1.addPoint(new Point(1, 3));
        clusters.add(cluster1);
        Cluster cluster2 = new Cluster(new Point(2, 2));
        cluster2.addPoint(new Point(2, 2));
        cluster2.addPoint(new Point(2, 3));
        clusters.add(cluster2);
        
        Point point = clusterer.getFarthestPoint(clusters);
        assertEquals(2, point.getX());
        assertEquals(2, point.getY());
    }
    
    @Test
    public void testGetNearestCluster() {
        // Test the getNearestCluster method
        
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer();
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(new Point(1, 1));
        Cluster cluster2 = new Cluster(new Point(2, 2));
        clusters.add(cluster1);
        clusters.add(cluster2);
        
        Point point = new Point(1.5, 1.5);
        Cluster nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertEquals(cluster1, nearestCluster);
    }
}
