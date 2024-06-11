import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {
    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        
        // Create a collection of points
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(11, 11));
        points.add(new Point(12, 12));

        // Perform clustering
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        // Validate the result
        assertEquals(2, clusters.size());
        assertEquals(3, clusters.get(0).getPoints().size());
        assertEquals(3, clusters.get(1).getPoints().size());
    }

    @Test
    public void testClusterWithEmptyCollection() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        
        // Create an empty collection of points
        List<Point> points = new ArrayList<>();

        // Perform clustering
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        // Validate the result
        assertEquals(2, clusters.size());
        assertEquals(0, clusters.get(0).getPoints().size());
        assertEquals(0, clusters.get(1).getPoints().size());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyInput() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        
        // Create a collection of points
        List<Point> points = new ArrayList<>();

        // Perform clustering
        clusterer.cluster(points, 2, 10);
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
            double dx = getX() - other.getX();
            double dy = getY() - other.getY();
            return Math.sqrt(dx*dx + dy*dy);
        }

        public Point centroidOf(Collection<Point> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            int count = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
                count++;
            }
            return new Point(sumX/count, sumY/count);
        }
    }
}