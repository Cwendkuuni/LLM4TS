import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private final KMeansPlusPlusClusterer<TestPoint> clusterer;
    private final Random random;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(Random random, EmptyClusterStrategy emptyStrategy) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
        this.clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
    }

    @Parameterized.Parameters(name = "{0} emptyStrategy")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new Random(), EmptyClusterStrategy.LARGEST_VARIANCE},
                new Object[]{new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                new Object[]{new Random(), EmptyClusterStrategy.FARTHEST_POINT},
                new Object[]{new Random(1), EmptyClusterStrategy.ERROR},
        });
    }

    @Test
    public void testCluster() {
        Collection<TestPoint> points = Arrays.asList(
                new TestPoint(0, 0), new TestPoint(1, 1), new TestPoint(2, 2),
                new TestPoint(3, 3), new TestPoint(4, 4), new TestPoint(5, 5)
        );

        List<Cluster<TestPoint>> result = clusterer.cluster(points, 3, 10);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertFalse(cluster.isEmpty());
            assertEquals(2, cluster.size());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Collection<TestPoint> points = Arrays.asList(new TestPoint(0, 0));
        List<Cluster<TestPoint>> result = clusterer.cluster(points, 3, 10);
    }

    @Test
    public void testChooseInitialCenters() {
        Collection<TestPoint> points = Arrays.asList(
                new TestPoint(0, 0), new TestPoint(1, 1), new TestPoint(2, 2),
                new TestPoint(3, 3), new TestPoint(4, 4), new TestPoint(5, 5)
        );

        List<Cluster<TestPoint>> result = KMeansPlusPlusClusterer.chooseInitialCenters(points, 3, random);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertFalse(cluster.isEmpty());
            assertEquals(1, cluster.size());
        }
    }

    @Test
    public void testGetNearestCluster() {
        Collection<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0, 0)),
                new Cluster<TestPoint>(new TestPoint(2, 2)),
                new Cluster<TestPoint>(new TestPoint(4, 4))
        );
        TestPoint point = new TestPoint(1, 1);

        Cluster<TestPoint> result = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        assertEquals(clusters.get(1), result);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Collection<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(0, 0), new TestPoint(1, 1),
                        new TestPoint(2, 2), new TestPoint(3, 3)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(4, 4), new TestPoint(5, 5)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>())
        );

        TestPoint result = clusterer.getPointFromLargestVarianceCluster(clusters);

        assertNotNull(result);
        assertEquals(clusters.get(1).getCenter(), result.getPosition());
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Collection<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(0, 0), new TestPoint(1, 1),
                        new TestPoint(2, 2), new TestPoint(3, 3)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(4, 4), new TestPoint(5, 5)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>())
        );

        TestPoint result = clusterer.getPointFromLargestNumberCluster(clusters);

        assertNotNull(result);
        assertEquals(clusters.get(1).getCenter(), result.getPosition());
    }

    @Test
    public void testGetFarthestPoint() {
        Collection<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(0, 0), new TestPoint(1, 1),
                        new TestPoint(2, 2), new TestPoint(3, 3)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>(Arrays.asList(
                        new TestPoint(4, 4), new TestPoint(5, 5)
                ))),
                new Cluster<TestPoint>(new ArrayList<TestPoint>())
        );

        TestPoint result = clusterer.getFarthestPoint(clusters);

        assertNotNull(result);
        assertEquals(clusters.get(2).getCenter(), result.getPosition());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<TestPoint>(new ArrayList<>()));
        clusters.add(new Cluster<TestPoint>(new ArrayList<>()));

        Collection<TestPoint> points = Arrays.asList(
                new TestPoint(0, 0), new TestPoint(1, 1), new TestPoint(2, 2)
        );

        clusterer.assignPointsToClusters(clusters, points);

        for (Cluster<TestPoint> cluster : clusters) {
            assertEquals(1, cluster.size());
        }
    }

    static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(TestPoint other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }

        public double[] getPosition() {
            return new double[]{x, y};
        }
    }

}