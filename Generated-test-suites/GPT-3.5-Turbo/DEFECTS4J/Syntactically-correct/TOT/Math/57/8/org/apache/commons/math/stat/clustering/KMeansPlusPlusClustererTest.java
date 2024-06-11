import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

public class KMeansPlusPlusClustererTest {

    private Random random = new Random();
    
    @Test
    public void testCluster_WithTypicalUseCases_ShouldReturnCorrectClusterList() {
        // Arrange
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 3));
        points.add(new Point(4, 2));
        int k = 2;
        int maxIterations = 100;
        
        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        Assert.assertEquals(k, result.size());
        for (Cluster<Point> cluster : result) {
            Assert.assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test
    public void testCluster_WithEmptyPoints_ShouldReturnEmptyClusterList() {
        // Arrange
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 100;
        
        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        Assert.assertEquals(0, result.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testCluster_WithInvalidK_ShouldThrowConvergenceException() {
        // Arrange
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        int k = 0;
        int maxIterations = 100;
        
        // Act
        clusterer.cluster(points, k, maxIterations);
    }
    
    @Test
    public void testCluster_WithLargeIterations_ShouldReturnClusterList() {
        // Arrange
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 3));
        points.add(new Point(4, 2));
        int k = 2;
        int maxIterations = 10000;
        
        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        Assert.assertEquals(k, result.size());
        for (Cluster<Point> cluster : result) {
            Assert.assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test
    public void testCluster_WithNegativeMaxIterations_ShouldReturnClusterList() {
        // Arrange
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 3));
        points.add(new Point(4, 2));
        int k = 2;
        int maxIterations = -1;
        
        // Act
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);

        // Assert
        Assert.assertEquals(k, result.size());
        for (Cluster<Point> cluster : result) {
            Assert.assertTrue(cluster.getPoints().size() > 0);
        }
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
        public double distanceFrom(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx*dx + dy*dy);
        }
        
        @Override
        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            int count = 0;
            for (Point p : points) {
                sumX += p.getX();
                sumY += p.getY();
                count++;
            }
            return new Point(sumX / count, sumY / count);
        }
    }
}