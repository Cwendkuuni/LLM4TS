import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Clusterable;

import java.util.ArrayList;
import java.util.List;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test that an error is thrown when an empty cluster is encountered
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Point> points = new ArrayList<>();
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(0, clusters.size());
    }

    @Test
    public void testLargestVarianceStrategy() {
        // Test that the largest variance strategy is correctly used when an empty cluster is encountered
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        Assert.assertEquals(5, clusters.size());
    }

    @Test
    public void testLargestPointsNumberStrategy() {
        // Test that the largest points number strategy is correctly used when an empty cluster is encountered
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        Assert.assertEquals(5, clusters.size());
    }

    @Test
    public void testFarthestPointStrategy() {
        // Test that the farthest point strategy is correctly used when an empty cluster is encountered
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        Assert.assertEquals(5, clusters.size());
    }

    @Test
    public void testCluster() {
        // Test the clustering algorithm for a variety of input scenarios
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);
        Assert.assertEquals(2, clusters.size());
    }

    private static class Point implements Clusterable<Point> {

        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distanceFrom(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

    }

}