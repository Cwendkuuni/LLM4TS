import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterWithValidInput() {
        List<TestCluster> testClusters = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<TestCluster> clusterer = new KMeansPlusPlusClusterer<>(random);

        TestCluster point1 = new TestCluster(1.0, 1.0);
        TestCluster point2 = new TestCluster(2.0, 2.0);
        List<TestCluster> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);

        List<Cluster<TestCluster>> clusters = clusterer.cluster(points, 2, 100);

        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        List<TestCluster> testClusters = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<TestCluster> clusterer = new KMeansPlusPlusClusterer<>(random);

        TestCluster point1 = new TestCluster(1.0, 1.0);
        TestCluster point2 = new TestCluster(2.0, 2.0);
        List<TestCluster> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);

        List<Cluster<TestCluster>> clusters = clusterer.cluster(points, 2, -1);

        assertEquals(2, clusters.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterError() {
        List<TestCluster> testClusters = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<TestCluster> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        TestCluster point1 = new TestCluster(1.0, 1.0);
        TestCluster point2 = new TestCluster(2.0, 2.0);
        List<TestCluster> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);

        clusterer.cluster(points, 5, 100);
    }

    private class TestCluster implements Clusterable<TestCluster> {
        private double x;
        private double y;

        public TestCluster(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(TestCluster other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public TestCluster centroidOf(List<TestCluster> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            for (TestCluster point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            double avgX = sumX / points.size();
            double avgY = sumY / points.size();
            return new TestCluster(avgX, avgY);
        }
    }

}