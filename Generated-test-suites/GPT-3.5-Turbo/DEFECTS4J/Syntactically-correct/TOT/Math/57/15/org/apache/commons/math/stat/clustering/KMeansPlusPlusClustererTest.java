import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random());
        points = Arrays.asList(
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(10, 10),
                new Point(20, 20),
                new Point(30, 30),
                new Point(50, 50),
                new Point(-1, -1),
                new Point(-10, -10),
                new Point(-20, -20),
                new Point(-50, -50)
        );
    }

    @Test
    public void testCluster_NumClusters() {
        int k = 4;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());
    }

    @Test
    public void testCluster_EmptyInput() {
        int k = 4;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = clusterer.cluster(new ArrayList<>(), k, maxIterations);
        assertTrue(clusters.isEmpty());
    }

    @Test
    public void testCluster_NumIterations() {
        int k = 4;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertTrue(clusters.size() <= maxIterations);
    }

    @Test
    public void testCluster_AllPointsAssigned() {
        int k = 4;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        int totalPointsAssigned = 0;
        for (Cluster<Point> cluster : clusters) {
            totalPointsAssigned += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPointsAssigned);
    }

    @Test
    public void testCluster_InvalidNumClusters() {
        int k = 0;
        int maxIterations = 100;
        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCluster_NegativeNumIterations() {
        int k = 4;
        int maxIterations = -100;
        try {
            List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCluster_InvalidEmptyStrategy() {
        int k = 4;
        int maxIterations = 100;
        KMeansPlusPlusClusterer<Point> invalidClusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        try {
            List<Cluster<Point>> clusters = invalidClusterer.cluster(points, k, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // success
        }
    }

    @Test
    public void testCluster_LargeInput() {
        int k = 4;
        int maxIterations = 100;
        List<Point> largeInput = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            double x = random.nextDouble() * 100;
            double y = random.nextDouble() * 100;
            largeInput.add(new Point(x, y));
        }
        List<Cluster<Point>> clusters = clusterer.cluster(largeInput, k, maxIterations);
        assertEquals(k, clusters.size());
    }

}