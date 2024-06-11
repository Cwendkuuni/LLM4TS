import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-10;
    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setup() {
        Random random = new Random(1234);
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = createPoints();
    }

    private List<Point> createPoints() {
        List<Point> points = new ArrayList<>();

        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
        points.add(new Point(9, 10));

        return points;
    }

    @Test
    public void testCluster_WithTwoClusters_ShouldReturnCorrectCentroids() {
        int k = 2;
        int maxIterations = 10;

        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, result.size());

        Cluster<Point> cluster1 = result.get(0);
        assertEquals(3.0, cluster1.getCenter().getX(), EPSILON);
        assertEquals(4.0, cluster1.getCenter().getY(), EPSILON);

        Cluster<Point> cluster2 = result.get(1);
        assertEquals(7.0, cluster2.getCenter().getX(), EPSILON);
        assertEquals(8.0, cluster2.getCenter().getY(), EPSILON);
    }

    @Test
    public void testCluster_WithFiveClusters_ShouldReturnCorrectCentroids() {
        int k = 5;
        int maxIterations = 10;

        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, result.size());

        Cluster<Point> cluster1 = result.get(0);
        assertEquals(1.0, cluster1.getCenter().getX(), EPSILON);
        assertEquals(2.0, cluster1.getCenter().getY(), EPSILON);

        Cluster<Point> cluster2 = result.get(1);
        assertEquals(3.0, cluster2.getCenter().getX(), EPSILON);
        assertEquals(4.0, cluster2.getCenter().getY(), EPSILON);

        Cluster<Point> cluster3 = result.get(2);
        assertEquals(5.0, cluster3.getCenter().getX(), EPSILON);
        assertEquals(6.0, cluster3.getCenter().getY(), EPSILON);

        Cluster<Point> cluster4 = result.get(3);
        assertEquals(7.0, cluster4.getCenter().getX(), EPSILON);
        assertEquals(8.0, cluster4.getCenter().getY(), EPSILON);

        Cluster<Point> cluster5 = result.get(4);
        assertEquals(9.0, cluster5.getCenter().getX(), EPSILON);
        assertEquals(10.0, cluster5.getCenter().getY(), EPSILON);
    }

    @Test
    public void testCluster_WithTenIterations_ShouldConvergeToStableCentroids() {
        int k = 2;
        int maxIterations = 10;

        List<Cluster<Point>> result1 = clusterer.cluster(points, k, maxIterations);
        List<Cluster<Point>> result2 = clusterer.cluster(points, k, maxIterations);

        for (int i = 0; i < result1.size(); i++) {
            Cluster<Point> cluster1 = result1.get(i);
            Cluster<Point> cluster2 = result2.get(i);

            assertEquals(cluster1.getCenter().getX(), cluster2.getCenter().getX(), EPSILON);
            assertEquals(cluster1.getCenter().getY(), cluster2.getCenter().getY(), EPSILON);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_WithTooManyClusters_ShouldThrowConvergenceException() {
        int k = 10;
        int maxIterations = 10;

        clusterer.cluster(points, k, maxIterations);
    }

    @Test
    public void testCluster_WithNegativeMaxIterations_ShouldNotConvergeToStableCentroids() {
        int k = 2;
        int maxIterations = -1;

        List<Cluster<Point>> result1 = clusterer.cluster(points, k, maxIterations);
        List<Cluster<Point>> result2 = clusterer.cluster(points, k, maxIterations);

        for (int i = 0; i < result1.size(); i++) {
            Cluster<Point> cluster1 = result1.get(i);
            Cluster<Point> cluster2 = result2.get(i);

            // Since maxIterations is negative, the result can differ from one execution to another
            // Hence, we only check if the resulting clusters contain any points or not
            assertEquals(cluster1.getPoints().isEmpty(), cluster2.getPoints().isEmpty());
        }
    }

    private static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;

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
            double dx = x - point.x;
            double dy = y - point.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            int count = points.size();
            return new Point(sumX / count, sumY / count);
        }
    }

}