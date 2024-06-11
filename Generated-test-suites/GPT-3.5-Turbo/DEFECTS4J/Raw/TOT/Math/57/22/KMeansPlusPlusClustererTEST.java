import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class KMeansPlusPlusClustererTest {

    // Test case for the cluster method

    // Typical Use Case
    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);

        // Create some test data
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(2, 3));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 11));

        int k = 2;
        int maxIterations = 100;

        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);

        // Test that the number of clusters is correct
        assertEquals(k, clusters.size());

        // Test that all points have been assigned to a cluster
        int totalPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    // Edge Case
    @Test
    public void testClusterWithEmptyPoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<Point> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 100;

        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);

        // Test that the number of clusters is 0
        assertEquals(0, clusters.size());
    }

    // Error Scenario
    @Test(expected = IllegalArgumentException.class)
    public void testClusterWithNegativeMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        int k = 2;
        int maxIterations = -1;

        clusterer.cluster(points, k, maxIterations);
    }

    // Test case for the getNearestCluster method

    // Typical Use Case
    @Test
    public void testGetNearestCluster() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        Cluster<Point> cluster3 = new Cluster<>(new Point(3, 3));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);

        Point point = new Point(0, 0);

        Cluster<Point> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        // Test that the nearest cluster is cluster1
        assertEquals(cluster1, nearestCluster);
    }

    // Edge Case
    @Test
    public void testGetNearestClusterWithEmptyClusters() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Point point = new Point(0, 0);

        Cluster<Point> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        // Test that the nearest cluster is null
        assertNull(nearestCluster);
    }

    // Test case for the assignPointsToClusters method

    // Typical Use Case
    @Test
    public void testAssignPointsToClusters() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);

        // Test that all points have been assigned to a cluster
        int totalPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    // Edge Case
    @Test
    public void testAssignPointsToClustersWithEmptyPoints() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        List<Point> points = new ArrayList<>();

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);

        // Test that no points have been assigned to any cluster
        int totalPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(0, totalPoints);
    }

    // ### ADD ADDITIONAL TEST CASES HERE ###

}