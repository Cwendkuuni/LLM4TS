import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KMeansPlusPlusClustererTest {

    private static final double DELTA = 1e-15;

    private Random random;
    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;
    private int k;
    private int maxIterations;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = createPoints();
        k = 3;
        maxIterations = 100;
    }

    @Test
    public void testCluster() {
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Check that the number of clusters is correct
        assertEquals(k, result.size());

        // Check that the number of points in each cluster add up to the total number of points
        int totalPoints = 0;
        for (Cluster<Point> cluster : result) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);

        // Check that the center of each cluster is close to the actual center
        for (Cluster<Point> cluster : result) {
            double centerX = cluster.getCenter().getX();
            double centerY = cluster.getCenter().getY();
            double sumX = 0;
            double sumY = 0;
            for (Point point : cluster.getPoints()) {
                sumX += point.getX();
                sumY += point.getY();
            }
            double avgX = sumX / cluster.getPoints().size();
            double avgY = sumY / cluster.getPoints().size();
            assertEquals(avgX, centerX, DELTA);
            assertEquals(avgY, centerY, DELTA);
        }
    }

    private List<Point> createPoints() {
        List<Point> points = new ArrayList<>();
        
        // Typical use cases
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        points.add(new Point(4, 4));
        points.add(new Point(5, 5));
        points.add(new Point(6, 6));
        
        // Edge cases
        points.add(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        points.add(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE));
        points.add(new Point(0, 0));
        
        // Error scenarios
        points.add(null);
        
        return points;
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

        @Override
        public double distanceFrom(Point point) {
            double dx = x - point.getX();
            double dy = y - point.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Point centroidOf(List<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            return new Point(sumX / points.size(), sumY / points.size());
        }
    }

}