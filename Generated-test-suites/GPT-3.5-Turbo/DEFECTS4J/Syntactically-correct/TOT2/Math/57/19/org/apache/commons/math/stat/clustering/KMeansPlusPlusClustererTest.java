import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster_Success() {
        // Create some dummy data points for testing
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));

        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Perform clustering
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        // Assert that clustering was successful
        assertEquals(2, clusters.size());
    }

    @Test
    public void testChooseInitialCenters_Success() {
        // Create some dummy data points for testing
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));

        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Choose initial centers
        List<Cluster<Point>> initialCenters = clusterer.chooseInitialCenters(points, 2, new Random());

        // Assert that the number of initial centers is correct
        assertEquals(2, initialCenters.size());
    }

    @Test
    public void testAssignPointsToClusters_Success() {
        // Create some dummy data points for testing
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));

        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Create clusters
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(0, 0)));
        clusters.add(new Cluster<>(new Point(4, 4)));

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Assert that each point is assigned to a cluster
        for (Cluster<Point> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testGetNearestCluster_Success() {
        // Create some dummy data points for testing
        Point point = new Point(1, 1);

        // Create clusters
        Cluster<Point> cluster1 = new Cluster<>(new Point(0, 0));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Get the nearest cluster to the point
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);

        // Assert that the correct nearest cluster is returned
        assertEquals(cluster2, nearestCluster);
    }

}