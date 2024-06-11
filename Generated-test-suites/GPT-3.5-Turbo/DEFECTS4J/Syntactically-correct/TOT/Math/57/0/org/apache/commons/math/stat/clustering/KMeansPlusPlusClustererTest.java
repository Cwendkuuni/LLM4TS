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
    public void testCluster_typicalCase() {
        List<Point> points = new ArrayList<>();
        // Add some points to the list
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 12));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, Integer.MAX_VALUE);

        // Check that there are exactly 2 clusters
        Assert.assertEquals(2, clusters.size());
    }

    @Test
    public void testCluster_edgeCase1() {
        List<Point> points = new ArrayList<>();
        // Add some points to the list
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.add(new Point(2, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        points.add(new Point(4, 5));
        points.add(new Point(5, 4));
        points.add(new Point(5, 5));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, Integer.MAX_VALUE);

        // Check that there are exactly 3 clusters
        Assert.assertEquals(3, clusters.size());
    }

    @Test
    public void testCluster_edgeCase2() {
        List<Point> points = new ArrayList<>();
        // Add some points to the list
        points.add(new Point(1, 1));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 1, Integer.MAX_VALUE);

        // Check that there is exactly 1 cluster
        Assert.assertEquals(1, clusters.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_errorCase() {
        List<Point> points = new ArrayList<>();
        // Add some points to the list
        points.add(new Point(1, 1));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        clusterer.cluster(points, 1, Integer.MAX_VALUE);
    }

    private static class Point implements Clusterable<Point> {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point p : points) {
                sumX += p.x;
                sumY += p.y;
            }
            return new Point(sumX / points.size(), sumY / points.size());
        }
    }

}