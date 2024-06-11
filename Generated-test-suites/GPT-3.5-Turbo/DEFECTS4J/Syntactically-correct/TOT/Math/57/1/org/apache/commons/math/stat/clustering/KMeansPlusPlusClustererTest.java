import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create a list of Clusterable objects for testing
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));
        points.add(new TestPoint(10, 10));
        points.add(new TestPoint(20, 20));
        points.add(new TestPoint(30, 30));

        // Create a KMeansPlusPlusClusterer object
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Cluster the points into 2 clusters
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 100);

        // Assert that the clusters have the expected number of points
        Assert.assertEquals(3, clusters.get(0).getPoints().size());
        Assert.assertEquals(3, clusters.get(1).getPoints().size());

        // Assert that the points are correctly assigned to clusters
        Assert.assertTrue(clusters.get(0).getPoints().contains(points.get(0)));
        Assert.assertTrue(clusters.get(0).getPoints().contains(points.get(1)));
        Assert.assertTrue(clusters.get(0).getPoints().contains(points.get(2)));
        Assert.assertTrue(clusters.get(1).getPoints().contains(points.get(3)));
        Assert.assertTrue(clusters.get(1).getPoints().contains(points.get(4)));
        Assert.assertTrue(clusters.get(1).getPoints().contains(points.get(5)));
    }

    @Test
    public void testClusterWithEmptyPoints() {
        // Create an empty list of Clusterable objects for testing
        List<TestPoint> points = new ArrayList<>();

        // Create a KMeansPlusPlusClusterer object
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Cluster the empty list of points into 2 clusters
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 100);

        // Assert that no clusters are created
        Assert.assertTrue(clusters.isEmpty());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithNegativeMaxIterations() {
        // Create a list of Clusterable objects for testing
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));

        // Create a KMeansPlusPlusClusterer object
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Attempt to cluster the points with a negative maxIterations value
        clusterer.cluster(points, 2, -1);
    }

    private static class TestPoint implements Clusterable<TestPoint> {

        private double x;
        private double y;

        TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(TestPoint other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public TestPoint centroidOf(Collection<TestPoint> points) {
            double sumX = 0;
            double sumY = 0;
            int count = 0;
            for (TestPoint point : points) {
                sumX += point.x;
                sumY += point.y;
                count++;
            }
            return new TestPoint(sumX / count, sumY / count);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TestPoint other = (TestPoint) obj;
            return Double.compare(other.x, x) == 0 && Double.compare(other.y, y) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}