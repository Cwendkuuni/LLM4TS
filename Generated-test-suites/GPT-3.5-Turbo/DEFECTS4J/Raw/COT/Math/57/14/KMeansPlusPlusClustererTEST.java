import org.apache.commons.math.stat.clustering.Cluster;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create test data
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2)); // Cluster 1
        points.add(new Point(2, 3)); // Cluster 2
        points.add(new Point(8, 9)); // Cluster 1
        points.add(new Point(9, 10)); // Cluster 2
        points.add(new Point(15, 16)); // Cluster 1
        points.add(new Point(16, 17)); // Cluster 2

        // Cluster the points
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        // Check the number of clusters
        assertEquals(2, clusters.size());

        // Check that all points are assigned to a cluster
        int totalPoints = 0;
        for (Cluster<Point> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);

        // Check the centers of the clusters
        List<Point> centerPoints = new ArrayList<>();
        centerPoints.add(new Point(8, 9)); // Center for Cluster 1
        centerPoints.add(new Point(9, 10)); // Center for Cluster 2
        for (int i = 0; i < clusters.size(); i++) {
            assertEquals(centerPoints.get(i), clusters.get(i).getCenter());
        }
    }

    @Test
    public void testClusterWithEmptyPoints() {
        List<Point> points = new ArrayList<>();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        try {
            clusterer.cluster(points, 2, 100);
            fail("IllegalArgumentException should be thrown for empty points");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("points must not be empty"));
        }
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(2, 3));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, -1);
        assertEquals(2, clusters.size());
    }

    static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;

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
            double sumX = x;
            double sumY = y;
            for (Point p : points) {
                sumX += p.x;
                sumY += p.y;
            }
            int totalPoints = points.size() + 1;
            return new Point(sumX / totalPoints, sumY / totalPoints);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Point point = (Point) obj;
            return Double.compare(point.x, x) == 0 &&
                    Double.compare(point.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}

