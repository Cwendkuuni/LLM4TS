import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster_typicalUseCase() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.add(new Point(2, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(10, 11));
        points.add(new Point(11, 10));
        points.add(new Point(11, 11));
        points.add(new Point(20, 20));
        points.add(new Point(20, 21));
        points.add(new Point(21, 20));
        points.add(new Point(21, 21));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 100);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testCluster_edgeCase() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 1));
        points.add(new Point(0, 2));
        points.add(new Point(1, 0));
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.add(new Point(2, 0));
        points.add(new Point(2, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 100);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_errorScenario() {
        List<Point> points = new ArrayList<>();

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        clusterer.cluster(points, 3, 100);
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
            double dx = x - other.getX();
            double dy = y - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public Point centroidOf(List<Point> points) {
            double sumX = 0;
            double sumY = 0;
            int count = points.size();
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            return new Point(sumX / count, sumY / count);
        }
    }
}