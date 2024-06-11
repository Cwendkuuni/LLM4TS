import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;
    private Random random;
    
    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        points = new ArrayList<>();
        random = new Random();
    }
    
    @Test
    public void testCluster_EmptyPoints() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 100);
        assertTrue(clusters.isEmpty());
    }
    
    @Test
    public void testCluster_OnePoint() {
        Point point = new Point(1, 1);
        points.add(point);
        
        List<Cluster<Point>> clusters = clusterer.cluster(points, 1, 100);
        assertEquals(1, clusters.size());
        Cluster<Point> cluster = clusters.get(0);
        assertEquals(point, cluster.getCenter());
        assertEquals(1, cluster.getPoints().size());
        assertEquals(point, cluster.getPoints().get(0));
    }
    
    @Test
    public void testCluster_TwoPoints() {
        Point point1 = new Point(1, 1);
        Point point2 = new Point(2, 2);
        points.add(point1);
        points.add(point2);
        
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);
        assertEquals(2, clusters.size());
        
        Cluster<Point> cluster1 = clusters.get(0);
        assertEquals(point1, cluster1.getCenter());
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(point1, cluster1.getPoints().get(0));
        
        Cluster<Point> cluster2 = clusters.get(1);
        assertEquals(point2, cluster2.getCenter());
        assertEquals(1, cluster2.getPoints().size());
        assertEquals(point2, cluster2.getPoints().get(0));
    }
    
    @Test
    public void testCluster_MultiplePoints() {
        Point point1 = new Point(1, 1);
        Point point2 = new Point(2, 2);
        Point point3 = new Point(3, 3);
        Point point4 = new Point(10, 10);
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);
        assertEquals(2, clusters.size());
        
        Cluster<Point> cluster1 = clusters.get(0);
        assertEquals((point1.getX() + point2.getX() + point3.getX()) / 3, cluster1.getCenter().getX(), 0);
        assertEquals((point1.getY() + point2.getY() + point3.getY()) / 3, cluster1.getCenter().getY(), 0);
        assertEquals(3, cluster1.getPoints().size());
        assertTrue(cluster1.getPoints().contains(point1));
        assertTrue(cluster1.getPoints().contains(point2));
        assertTrue(cluster1.getPoints().contains(point3));
        
        Cluster<Point> cluster2 = clusters.get(1);
        assertEquals(point4, cluster2.getCenter());
        assertEquals(1, cluster2.getPoints().size());
        assertEquals(point4, cluster2.getPoints().get(0));
    }
    
    @Test
    public void testCluster_MaxIterations() {
        for (int i = 0; i < 1000; i++) {
            Point point = new Point(random.nextInt(10), random.nextInt(10));
            points.add(point);
        }
        
        List<Cluster<Point>> clusters = clusterer.cluster(points, 10, 100);
        assertEquals(10, clusters.size());
        
        for (Cluster<Point> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }
    
    private class Point implements Clusterable<Point> {
        
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
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            int count = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
                count++;
            }
            return new Point(sumX / count, sumY / count);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }
        
        @Override
        public int hashCode() {
            int result = Double.hashCode(x);
            result = 31 * result + Double.hashCode(y);
            return result;
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
        
    }
    
}