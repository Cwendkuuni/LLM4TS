import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyCluster() {
        KMeansPlusPlusClusterer.EmptyClusterStrategy strategy = KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR;
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random(), strategy);
        List<String> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 10;

        try {
            clusterer.cluster(points, k, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, e.getPattern());
        }
    }

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<String> points = new ArrayList<>();
        points.add("point1");
        points.add("point2");
        points.add("point3");
        points.add("point4");
        int k = 2;
        int maxIterations = 100;

        List<Cluster<String>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        for (Cluster<String> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (String point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

    @Test
    public void testClusterWithMaxIterationsNegative() {
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<String> points = new ArrayList<>();
        points.add("point1");
        points.add("point2");
        points.add("point3");
        int k = 2;
        int maxIterations = -1;

        List<Cluster<String>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        for (Cluster<String> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (String point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

    @Test
    public void testClusterWithEmptyStrategyLargestVariance() {
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<String> points = new ArrayList<>();
        points.add("point1");
        points.add("point2");
        points.add("point3");
        points.add("point4");
        int k = 2;
        int maxIterations = 100;

        List<Cluster<String>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        for (Cluster<String> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (String point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

    @Test
    public void testClusterWithEmptyStrategyLargestPointsNumber() {
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<String> points = new ArrayList<>();
        points.add("point1");
        points.add("point2");
        points.add("point3");
        points.add("point4");
        int k = 2;
        int maxIterations = 100;

        List<Cluster<String>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        for (Cluster<String> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (String point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

    @Test
    public void testClusterWithEmptyStrategyFarthestPoint() {
        KMeansPlusPlusClusterer<String> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<String> points = new ArrayList<>();
        points.add("point1");
        points.add("point2");
        points.add("point3");
        points.add("point4");
        int k = 2;
        int maxIterations = 100;

        List<Cluster<String>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        for (Cluster<String> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            assertNotNull(cluster.getCenter());
            for (String point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

}