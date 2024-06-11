import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private Random random;
    private List<Point> points;
    
    @Before
    public void setUp() {
        random = new Random();
        points = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            points.add(new Point(x, y));
        }
    }

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        assertNotNull(clusters);
        assertEquals(5, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 10);
        fail("Exception not thrown");
    }
    
    @Test
    public void testClusterWithEmptyClusterStrategyLargestVariance() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 10);
        assertNotNull(clusters);
        assertEquals(10, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test
    public void testClusterWithEmptyClusterStrategyLargestPointsNumber() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 10);
        assertNotNull(clusters);
        assertEquals(10, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test
    public void testClusterWithEmptyClusterStrategyFarthestPoint() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 10);
        assertNotNull(clusters);
        assertEquals(10, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterStrategyError() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 10);
        fail("Exception not thrown");
    }
    
    @Test
    public void testChooseInitialCenters() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = clusterer.cluster(points, 5, 10);
        assertNotNull(clusters);
        assertEquals(5, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }
    
    @Test
    public void testAssignPointsToClusters() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(0.0, 0.0)));
        clusters.add(new Cluster<>(new Point(1.0, 1.0)));
        clusters.add(new Cluster<>(new Point(2.0, 2.0)));
        clusterer.assignPointsToClusters(clusters, points);
        for (Cluster<Point> cluster : clusters) {
            Collection<Point> clusterPoints = cluster.getPoints();
            assertNotNull(clusterPoints);
            assertTrue(clusterPoints.size() > 0);
        }
    }
    
    @Test
    public void testGetNearestCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point(0.0, 0.0)));
        clusters.add(new Cluster<>(new Point(1.0, 1.0)));
        clusters.add(new Cluster<>(new Point(2.0, 2.0)));
        Point point = new Point(0.5, 0.5);
        Cluster<Point> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
        assertTrue(nearestCluster.getCenter().equals(new Point(0.0, 0.0)));
    }
    
    private class Point implements Clusterable<Point> {
        
        private double x;
        private double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Point centroidOf(Collection<Point> points) {
            double cx = x;
            double cy = y;
            for (Point point : points) {
                cx += point.getX();
                cy += point.getY();
            }
            int size = points.size() + 1;
            return new Point(cx / size, cy / size);
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
    }
}