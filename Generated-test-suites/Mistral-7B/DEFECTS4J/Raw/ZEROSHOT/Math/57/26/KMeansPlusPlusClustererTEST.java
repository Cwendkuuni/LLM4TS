import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        MyPoint[] points = {new MyPoint(1.0, 1.0), new MyPoint(2.0, 2.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0)};

        List<Cluster<MyPoint>> clusters = clusterer.cluster(Arrays.asList(points), 3, 10);

        Assert.assertEquals(3, clusters.size());
        // Check that each cluster has at least one point
        for (Cluster<MyPoint> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
            // Check that the largest variance cluster has been split
            Cluster<MyPoint> largestVarianceCluster = null;
            double largestVariance = 0.0;
            for (Cluster<MyPoint> c : clusters) {
                if (c != cluster) {
                    double variance = new Variance().increment(c.stream().map(p -> p.distanceFrom(c.getCenter())).mapToDouble(d -> d * d).sum()).getResult();
                    if (variance > largestVariance) {
                        largestVarianceCluster = c;
                        largestVariance = variance;
                    }
                }
            }
            Assert.assertNotNull(largestVarianceCluster);
            Cluster<MyPoint> newCluster = cluster;
            List<Cluster<MyPoint>> newClusters = new ArrayList<>();
            newClusters.add(newCluster);
            for (Cluster<MyPoint> c : clusters) {
                if (c != largestVarianceCluster) {
                    newClusters.add(c);
                }
            }
            List<Cluster<MyPoint>> newClustersWithNewCluster = clusterer.cluster(Arrays.asList(points), 4, 10, newClusters.toArray(new Cluster[0]));
            Assert.assertEquals(4, newClustersWithNewCluster.size());
            Assert.assertTrue(newClustersWithNewCluster.contains(newCluster));
            Assert.assertEquals(1, newCluster.getPoints().size());
            MyPoint point = newCluster.getPoints().iterator().next();
            Assert.assertFalse(largestVarianceCluster.getPoints().contains(point));
            Assert.assertTrue(newCluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testEmptyClusterStrategyLargestNumber() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        MyPoint[] points = {new MyPoint(1.0, 1.0), new MyPoint(2.0, 2.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0)};

        List<Cluster<MyPoint>> clusters = clusterer.cluster(Arrays.asList(points), 3, 10);

        Assert.assertEquals(3, clusters.size());
        // Check that each cluster has at least one point
        for (Cluster<MyPoint> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
            // Check that the largest number cluster has been split
            Cluster<MyPoint> largestNumberCluster = null;
            int largestNumber = 0;
            for (Cluster<MyPoint> c : clusters) {
                int size = c.getPoints().size();
                if (size > largestNumber) {
                    largestNumberCluster = c;
                    largestNumber = size;
                }
            }
            Assert.assertNotNull(largestNumberCluster);
            Cluster<MyPoint> newCluster = cluster;
            List<Cluster<MyPoint>> newClusters = new ArrayList<>();
            newClusters.add(newCluster);
            for (Cluster<MyPoint> c : clusters) {
                if (c != largestNumberCluster) {
                    newClusters.add(c);
                }
            }
            List<Cluster<MyPoint>> newClustersWithNewCluster = clusterer.cluster(Arrays.asList(points), 4, 10, newClusters.toArray(new Cluster[0]));
            Assert.assertEquals(4, newClustersWithNewCluster.size());
            Assert.assertTrue(newClustersWithNewCluster.contains(newCluster));
            Assert.assertEquals(1, newCluster.getPoints().size());
            MyPoint point = newCluster.getPoints().iterator().next();
            Assert.assertFalse(largestNumberCluster.getPoints().contains(point));
            Assert.assertTrue(newCluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        MyPoint[] points = {new MyPoint(1.0, 1.0), new MyPoint(2.0, 2.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0), new MyPoint(3.0, 3.0)};

        List<Cluster<MyPoint>> clusters = clusterer.cluster(Arrays.asList(points), 3, 10);

        Assert.assertEquals(3, clusters.size());
        // Check that each cluster has at least one point
        for (Cluster<MyPoint> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
            // Simulate an error
            throw new RuntimeException();
        }
    }

    /**
     * Custom point class for testing.
     */
    public static class MyPoint implements Clusterable<MyPoint> {
        private final double x;
        private final double y;

        public MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<MyPoint> other) {
            MyPoint otherPoint = (MyPoint) other;
            return Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(y - otherPoint.y, 2));
        }

        @Override
        public double distanceFrom(Cluster<MyPoint> cluster) {
            MyPoint center = cluster.getCenter();
            return Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(y - center.getY(), 2));
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public String toString() {
            return "MyPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}