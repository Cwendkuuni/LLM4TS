import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create a collection of points and instantiate KMeansPlusPlusClusterer
        Collection<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());

        // Run the cluster method
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        // Verify the results
        assertEquals(2, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(points.containsAll(cluster.getPoints()));
        }
    }

    @Test
    public void testClusterWithZeroPoints() {
        // Create an empty collection of points and instantiate KMeansPlusPlusClusterer
        Collection<Point> points = new ArrayList<>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());

        // Run the cluster method
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        // Verify the results
        assertTrue(clusters.isEmpty());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterErrorStrategy() {
        // Create a collection of points containing only one point and instantiate KMeansPlusPlusClusterer with EMPTY strategy
        Collection<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        // Run the cluster method
        clusterer.cluster(points, 2, 10);
    }

    @Test
    public void testClusterWithLargestVarianceStrategy() {
        // Create a collection of points containing 3 points and instantiate KMeansPlusPlusClusterer with LARGEST_VARIANCE strategy
        Collection<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        // Run the cluster method
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        // Verify the results
        assertEquals(2, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(points.containsAll(cluster.getPoints()));
        }
    }
}