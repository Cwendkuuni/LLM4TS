import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<Point>(random);
    }

    @Test
    public void testCluster_EmptyPoints_ReturnsEmptyCluster() {
        int k = 3;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(new ArrayList<Point>(), k, maxIterations);
        assertNotNull(clusters);
        assertTrue(clusters.isEmpty());
    }

    @Test
    public void testCluster_LessPointsThanK_ReturnsSameNumberOfClusters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        int k = 3;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertNotNull(clusters);
        assertEquals(points.size(), clusters.size());
    }

    @Test
    public void testCluster_ZeroK_ReturnsEmptyClusterList() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        int k = 0;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertNotNull(clusters);
        assertTrue(clusters.isEmpty());
    }

    @Test
    public void testCluster_MaxIterationsNegative_ReturnsClusters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        int k = 2;
        int maxIterations = -1;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
    }

    @Test
    public void testCluster_ValidPoints_ReturnsClusters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(10, 10));
        int k = 2;
        int maxIterations = 10;
        List<Cluster<Point>> clusters = clusterer.cluster(points, k, maxIterations);
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCluster_NullPoints_ThrowsIllegalArgumentException() {
        int k = 2;
        int maxIterations = 10;
        clusterer.cluster(null, k, maxIterations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCluster_NullCenter_ThrowsIllegalArgumentException() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(null);
        int k = 2;
        int maxIterations = 10;
        clusterer.cluster(points, k, maxIterations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCluster_NullRandom_ThrowsIllegalArgumentException() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        int k = 2;
        int maxIterations = 10;
        new KMeansPlusPlusClusterer<Point>(null).cluster(points, k, maxIterations);
    }

}