import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster_typicalCase() {
        // Typical Case: Test clustering with typical input values

        // Create some dummy points
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(3, 3);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(5, 5);

        // Create the clusterer and add the points
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>();
        clusterer.addPoint(p1);
        clusterer.addPoint(p2);
        clusterer.addPoint(p3);
        clusterer.addPoint(p4);
        clusterer.addPoint(p5);

        // Cluster the points into 2 clusters
        List<Cluster<Point>> clusters = clusterer.cluster(2);

        // Check the number of clusters
        assertEquals(2, clusters.size());

        // Check the number of points in each cluster
        assertEquals(3, clusters.get(0).getPoints().size());
        assertEquals(2, clusters.get(1).getPoints().size());

        // Check the center of each cluster
        Point center1 = clusters.get(0).getCenter();
        Point center2 = clusters.get(1).getCenter();
        
        // Check the distance between each point and its cluster center
        for (Point point : clusters.get(0).getPoints()) {
            double distance = point.distanceFrom(center1);
            assertTrue(distance < 0.1); // Check that the distance is within a threshold
        }
        for (Point point : clusters.get(1).getPoints()) {
            double distance = point.distanceFrom(center2);
            assertTrue(distance < 0.1); // Check that the distance is within a threshold
        }
    }

    @Test
    public void testCluster_edgeCase() {
        // Edge Case: Test clustering with edge input values

        // Create an empty clusterer
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>();

        // Cluster the points into 0 clusters
        List<Cluster<Point>> clusters = clusterer.cluster(0);

        // Check that there are no clusters
        assertTrue(clusters.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCluster_errorCase() {
        // Error Case: Test clustering with negative number of clusters
        // Expecting an IllegalArgumentException to be thrown

        // Create a clusterer
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>();

        // Cluster the points into a negative number of clusters
        clusterer.cluster(-1);
    }

}