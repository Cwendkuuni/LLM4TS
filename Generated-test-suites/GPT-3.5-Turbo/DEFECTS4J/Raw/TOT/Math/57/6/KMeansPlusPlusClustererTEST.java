import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<Point>(random);
    }

    @Test
    public void testCluster_withValidInput_shouldReturnCorrectNumberOfClusters() {
        // Arrange
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(1, 0));
        points.add(new Point(0, 1));
        points.add(new Point(4, 4));
        points.add(new Point(10, 10));
        int k = 2;
        int maxIterations = 100;

        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        assertEquals(k, result.size());
    }

    @Test
    public void testCluster_withValidInput_shouldAssignAllPointsToClusters() {
        // Arrange
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(1, 0));
        points.add(new Point(0, 1));
        points.add(new Point(4, 4));
        points.add(new Point(10, 10));
        int k = 2;
        int maxIterations = 100;

        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        int totalAssignedPoints = 0;
        for (Cluster<Point> cluster : result) {
            totalAssignedPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalAssignedPoints);
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_withEmptyInput_shouldThrowConvergenceException() {
        // Arrange
        List<Point> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 100;

        // Act
        clusterer.cluster(points, k, maxIterations);

        // Assert (expected exception)
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_withInvalidKValue_shouldThrowConvergenceException() {
        // Arrange
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        int k = -1;
        int maxIterations = 100;

        // Act
        clusterer.cluster(points, k, maxIterations);

        // Assert (expected exception)
    }

    @Test
    public void testCluster_withZeroMaxIterations_shouldNotStopUntilResultConverges() {
        // Arrange
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(1, 0));
        points.add(new Point(0, 1));
        points.add(new Point(4, 4));
        points.add(new Point(10, 10));
        int k = 2;
        int maxIterations = 0;

        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        assertEquals(k, result.size());
    }

    class Point implements Clusterable<Point> {

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

        public double distanceFrom(Point p) {
            double dx = p.getX() - x;
            double dy = p.getY() - y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(List<Point> points) {
            double sumX = 0;
            double sumY = 0;
            int count = points.size();
            for (Point p : points) {
                sumX += p.getX();
                sumY += p.getY();
            }
            if (count > 0) {
                return new Point(sumX / count, sumY / count);
            } else {
                return this;
            }
        }
    }
}