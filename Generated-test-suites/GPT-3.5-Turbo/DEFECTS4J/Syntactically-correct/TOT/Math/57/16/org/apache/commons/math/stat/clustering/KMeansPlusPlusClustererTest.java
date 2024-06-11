import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KMeansPlusPlusClustererTest {

    private static final double DELTA = 1e-15;

    private Random random = new Random();

    @Test
    public void testCluster() {

        // Create test points
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));
        points.add(new TestPoint(20, 20));
        points.add(new TestPoint(21, 21));
        points.add(new TestPoint(22, 22));
        points.add(new TestPoint(100, 100));

        // Create expected clusters
        List<Cluster<TestPoint>> expectedClusters = new ArrayList<>();
        Cluster<TestPoint> firstCluster = new Cluster<>(new TestPoint(1, 1));
        firstCluster.addPoint(new TestPoint(2, 2));
        firstCluster.addPoint(new TestPoint(3, 3));
        expectedClusters.add(firstCluster);
        Cluster<TestPoint> secondCluster = new Cluster<>(new TestPoint(20, 20));
        secondCluster.addPoint(new TestPoint(21, 21));
        secondCluster.addPoint(new TestPoint(22, 22));
        expectedClusters.add(secondCluster);
        Cluster<TestPoint> thirdCluster = new Cluster<>(new TestPoint(100, 100));
        expectedClusters.add(thirdCluster);

        // Run the KMeansPlusPlus clustering algorithm
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);

        // Assert the expected clusters
        assertEquals(expectedClusters.size(), clusters.size());
        for (int i = 0; i < expectedClusters.size(); i++) {
            Cluster<TestPoint> expectedCluster = expectedClusters.get(i);
            Cluster<TestPoint> actualCluster = clusters.get(i);

            assertEquals(expectedCluster.getCenter().x, actualCluster.getCenter().x, DELTA);
            assertEquals(expectedCluster.getCenter().y, actualCluster.getCenter().y, DELTA);

            assertEquals(expectedCluster.getPoints().size(), actualCluster.getPoints().size());
            for (int j = 0; j < expectedCluster.getPoints().size(); j++) {
                TestPoint expectedPoint = expectedCluster.getPoints().get(j);
                TestPoint actualPoint = actualCluster.getPoints().get(j);
                assertEquals(expectedPoint.x, actualPoint.x, DELTA);
                assertEquals(expectedPoint.y, actualPoint.y, DELTA);
            }
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        // Create an empty list of points
        List<TestPoint> points = new ArrayList<>();

        // Run the KMeansPlusPlus clustering algorithm
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        clusterer.cluster(points, 3, 100);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyK() {
        // Create test points
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));

        // Run the KMeansPlusPlus clustering algorithm with empty value for k
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        clusterer.cluster(points, 0, 100);
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private double x;
        private double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(TestPoint point) {
            double dx = x - point.x;
            double dy = y - point.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public TestPoint centroidOf(Collection<TestPoint> points) {
            double sumX = 0;
            double sumY = 0;
            int total = points.size();
            for (TestPoint point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            return new TestPoint(sumX / total, sumY / total);
        }
    }
}