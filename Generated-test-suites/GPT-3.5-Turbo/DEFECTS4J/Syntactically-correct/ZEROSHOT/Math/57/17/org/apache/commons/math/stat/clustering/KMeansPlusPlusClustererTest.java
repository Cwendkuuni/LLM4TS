import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;
    private List<Point> points;

    @Before
    public void setup() {
        Random random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random);
        points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
    }

    @Test
    public void testCluster() {
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 10);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
        for (Cluster<Point> cluster : clusters) {
            Collection<Point> clusterPoints = cluster.getPoints();
            assertTrue(clusterPoints.size() <= 2);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyCluster() {
        points.clear();
        clusterer.cluster(points, 2, 10);
    }

    @Test
    public void testChooseInitialCenters() {
        List<Cluster<Point>> centers = KMeansPlusPlusClusterer.chooseInitialCenters(points, 2, new Random());

        assertNotNull(centers);
        assertEquals(2, centers.size());
        for (Cluster<Point> center : centers) {
            Collection<Point> centerPoints = center.getPoints();
            assertEquals(1, centerPoints.size());
            assertTrue(points.containsAll(centerPoints));
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Point>> centers = new ArrayList<>();
        Cluster<Point> center1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> center2 = new Cluster<>(new Point(3, 4));
        centers.add(center1);
        centers.add(center2);

        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));

        KMeansPlusPlusClusterer.assignPointsToClusters(centers, points);

        assertEquals(2, centers.size());
        assertEquals(2, center1.getPoints().size());
        assertTrue(center1.getPoints().contains(new Point(1, 2)));
        assertTrue(center1.getPoints().contains(new Point(3, 4)));
        assertEquals(1, center2.getPoints().size());
        assertTrue(center2.getPoints().contains(new Point(5, 6)));
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 2));
        Cluster<Point> cluster2 = new Cluster<>(new Point(3, 4));
        Cluster<Point> cluster3 = new Cluster<>(new Point(5, 6));
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);

        Point point = new Point(1, 2);

        Cluster<Point> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        assertNotNull(nearestCluster);
        assertTrue(clusters.contains(nearestCluster));
    }

    private static class Point implements Clusterable<Point> {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
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
            int count = 0;

            for (Point point : points) {
                sumX += point.x;
                sumY += point.y;
                count++;
            }

            return new Point(sumX / count, sumY / count);
        }
    }
}