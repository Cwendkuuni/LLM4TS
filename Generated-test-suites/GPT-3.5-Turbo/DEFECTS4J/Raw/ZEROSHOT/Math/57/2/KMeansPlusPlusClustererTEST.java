import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<ClusterableTest> clusterer;
    private List<ClusterableTest> points;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = createPoints();
    }

    @Test
    public void testCluster() {
        int k = 2;
        int maxIterations = 10;
        List<Cluster<ClusterableTest>> clusters =
                clusterer.cluster(points, k, maxIterations);

        // Check that number of clusters is k
        Assert.assertEquals(k, clusters.size());

        // Check that all points are assigned to a cluster
        int totalPoints = 0;
        for (Cluster<ClusterableTest> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        Assert.assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        int k = 3;
        int maxIterations = -1;
        List<Cluster<ClusterableTest>> clusters =
                clusterer.cluster(points, k, maxIterations);

        // Check that number of clusters is k
        Assert.assertEquals(k, clusters.size());

        // Check that all points are assigned to a cluster
        int totalPoints = 0;
        for (Cluster<ClusterableTest> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        Assert.assertEquals(points.size(), totalPoints);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterError() {
        // Create a clusterer that uses EMPTY strategy
        KMeansPlusPlusClusterer<ClusterableTest> errorClusterer =
                new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        int k = 3;
        int maxIterations = 10;
        // Create points with 1 cluster
        List<ClusterableTest> points = createPointsWithOneCluster();
        errorClusterer.cluster(points, k, maxIterations);
    }

    private List<ClusterableTest> createPoints() {
        List<ClusterableTest> points = new ArrayList<>();
        points.add(new ClusterableTest(1, 1));
        points.add(new ClusterableTest(2, 2));
        points.add(new ClusterableTest(3, 3));
        points.add(new ClusterableTest(4, 4));
        points.add(new ClusterableTest(5, 5));
        return points;
    }

    private List<ClusterableTest> createPointsWithOneCluster() {
        List<ClusterableTest> points = new ArrayList<>();
        points.add(new ClusterableTest(1, 1));
        points.add(new ClusterableTest(1, 2));
        points.add(new ClusterableTest(1, 3));
        points.add(new ClusterableTest(1, 4));
        points.add(new ClusterableTest(1, 5));
        return points;
    }

}

class ClusterableTest implements Clusterable<ClusterableTest> {

    private double x;
    private double y;

    public ClusterableTest(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(ClusterableTest other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}