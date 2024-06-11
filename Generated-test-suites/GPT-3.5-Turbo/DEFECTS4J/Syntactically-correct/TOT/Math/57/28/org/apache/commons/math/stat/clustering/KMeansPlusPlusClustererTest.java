import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.util.LocalizedFormats;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random());
        points = new ArrayList<>();
        points.add(new Point(0.0, 0.0));
        points.add(new Point(0.5, 0.5));
        points.add(new Point(1.0, 1.0));
        points.add(new Point(2.0, 2.0));
        points.add(new Point(3.0, 3.0));
        points.add(new Point(4.0, 4.0));
        points.add(new Point(5.0, 5.0));
    }

    @Test
    public void testCluster_ValidPointsAndK_ShouldReturnCorrectClusters() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        Assert.assertEquals(2, clusters.size());

        Cluster<Point> cluster1 = clusters.get(0);
        Assert.assertEquals(4, cluster1.getPoints().size());

        Cluster<Point> cluster2 = clusters.get(1);
        Assert.assertEquals(3, cluster2.getPoints().size());
    }

    @Test
    public void testCluster_InvalidPoints_ShouldThrowConvergenceException() {
        points.clear();
        try {
            clusterer.cluster(points, 2, 10);
            Assert.fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            Assert.assertEquals(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, e.getPattern());
        }
    }

    @Test
    public void testCluster_NegativeMaxIterations_ShouldNotHaveMaxIterations() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, -1);

        Assert.assertEquals(2, clusters.size());
    }

    @Test
    public void testCluster_NegativeK_ShouldReturnEmptyClusters() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, -1, 10);

        Assert.assertTrue(clusters.isEmpty());
    }

    @Test
    public void testCluster_ZeroK_ShouldReturnEmptyClusters() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 0, 10);

        Assert.assertTrue(clusters.isEmpty());
    }

    @Test
    public void testCluster_ZeroPoints_ShouldReturnEmptyClusters() {
        points.clear();
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        Assert.assertTrue(clusters.isEmpty());
    }
}