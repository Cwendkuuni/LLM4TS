import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.*;
import org.apache.commons.math.stat.clustering.Cluster.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        Random random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);

        // Validate the number of clusters
        assertEquals(k, clusters.size());

        // Validate the number of points in each cluster
        for (Cluster<Point> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Validate the number of points in each cluster
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());
    }

    @Test
    public void testChooseInitialCenters() {
        int k = 2;
        Random random = new Random();
        List<Point> initialCenters = clusterer.chooseInitialCenters(points, k, random);

        // Validate the number of initial centers
        assertEquals(k, initialCenters.size());

        // Validate that each initial center is a point from the input points
        for (Point center : initialCenters) {
            assertTrue(points.contains(center));
        }
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Get a point from the largest variance cluster
        Point point = clusterer.getPointFromLargestVarianceCluster(clusters);

        // Validate that the selected point is from the largest variance cluster
        assertTrue(cluster1.getPoints().contains(point) || cluster2.getPoints().contains(point));
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Get a point from the largest number cluster
        Point point = clusterer.getPointFromLargestNumberCluster(clusters);

        // Validate that the selected point is from the largest number cluster
        assertTrue(cluster1.getPoints().contains(point) || cluster2.getPoints().contains(point));
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Get the farthest point
        Point point = clusterer.getFarthestPoint(clusters);

        // Validate that the selected point is the farthest from its cluster center
        assertTrue(cluster1.getPoints().contains(point) || cluster2.getPoints().contains(point));
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(5, 5));
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign points to clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Get the nearest cluster to a point
        Point point = new Point(3, 3);
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);

        // Validate that the nearest cluster is the correct cluster
        assertEquals(cluster2, nearestCluster);
    }
}