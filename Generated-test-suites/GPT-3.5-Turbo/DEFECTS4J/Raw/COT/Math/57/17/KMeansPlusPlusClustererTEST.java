import org.junit.Test;
import org.apache.commons.math.exception.ConvergenceException;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testChooseInitialCenters() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(0);

        // Create some Clusterable points
        List<Clusterable> points = new ArrayList<Clusterable>();
        points.add(new Clusterable(1, 2));
        points.add(new Clusterable(3, 4));
        points.add(new Clusterable(5, 6));
        points.add(new Clusterable(7, 8));
        int k = 2;
        List<Cluster<Clusterable>> initialCenters = clusterer.chooseInitialCenters(points, k);

        // Verify that the initial centers have been chosen correctly
        assertEquals(k, initialCenters.size());
        assertTrue(points.containsAll(initialCenters.get(0).getPoints()));
        assertTrue(points.containsAll(initialCenters.get(1).getPoints()));
    }

    @Test
    public void testAssignPointsToClusters() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(0);

        // Create some Clusterable points and clusters
        List<Clusterable> points = new ArrayList<Clusterable>();
        points.add(new Clusterable(1, 2));
        points.add(new Clusterable(3, 4));
        points.add(new Clusterable(5, 6));
        points.add(new Clusterable(7, 8));
        Cluster<Clusterable> cluster1 = new Cluster<Clusterable>(new Clusterable(1, 2));
        Cluster<Clusterable> cluster2 = new Cluster<Clusterable>(new Clusterable(5, 6));
        List<Cluster<Clusterable>> clusters = new ArrayList<Cluster<Clusterable>>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Assign the points to the clusters
        clusterer.assignPointsToClusters(clusters, points);

        // Verify that the points have been assigned correctly to the clusters
        assertTrue(cluster1.getPoints().containsAll(points));
        assertTrue(cluster2.getPoints().containsAll(points));
    }

    @Test
    public void testGetNearestCluster() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(0);

        // Create some Clusterable points and clusters
        Clusterable point1 = new Clusterable(1, 2);
        Clusterable point2 = new Clusterable(3, 4);
        Cluster<Clusterable> cluster1 = new Cluster<Clusterable>(new Clusterable(1, 2));
        Cluster<Clusterable> cluster2 = new Cluster<Clusterable>(new Clusterable(5, 6));
        List<Cluster<Clusterable>> clusters = new ArrayList<Cluster<Clusterable>>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        // Get the nearest cluster for the point
        Cluster<Clusterable> nearestCluster = clusterer.getNearestCluster(clusters, point1);

        // Verify that the nearest cluster is cluster1
        assertEquals(cluster1, nearestCluster);
    }

    @Test
    public void testCluster() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(0);

        // Create some Clusterable points
        List<Clusterable> points = new ArrayList<Clusterable>();
        points.add(new Clusterable(1, 2));
        points.add(new Clusterable(3, 4));
        points.add(new Clusterable(5, 6));
        points.add(new Clusterable(7, 8));
        int k = 2;
        int maxIterations = 10;

        // Cluster the points
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, k, maxIterations);

        // Verify that the number of clusters is correct
        assertEquals(k, clusters.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() {
        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(0);

        // Create an empty list of points
        List<Clusterable> points = new ArrayList<Clusterable>();

        // Cluster the empty list of points
        clusterer.cluster(points, 2, 10);
    }
}