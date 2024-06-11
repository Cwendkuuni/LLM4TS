import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        List<Cluster<TestPoint>> clusters = new KMeansPlusPlusClusterer<TestPoint>(new Random()).cluster(createPoints(), 2, 10);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<TestPoint>> clusters = createClusters();
        List<TestPoint> points = createPoints();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        clusterer.assignPointsToClusters(clusters, points);
        for (Cluster<TestPoint> cluster : clusters) {
            for (TestPoint point : cluster.getPoints()) {
                assertTrue(points.contains(point));
            }
        }
    }

    @Test
    public void testChooseInitialCenters() {
        List<TestPoint> points = createPoints();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        List<Cluster<TestPoint>> clusters = clusterer.chooseInitialCenters(points, 2, new Random());
        assertEquals(2, clusters.size());
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<TestPoint>> clusters = createClusters();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        TestPoint point = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<TestPoint>> clusters = createClusters();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        TestPoint point = clusterer.getPointFromLargestNumberCluster(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetFarthestPoint() {
        List<Cluster<TestPoint>> clusters = createClusters();
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        TestPoint point = clusterer.getFarthestPoint(clusters);
        assertNotNull(point);
    }

    @Test
    public void testGetNearestCluster() {
        List<Cluster<TestPoint>> clusters = createClusters();
        TestPoint point = new TestPoint(0, 0);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        Cluster<TestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
        assertTrue(nearestCluster.getPoints().isEmpty());
    }

    private List<TestPoint> createPoints() {
        List<TestPoint> points = new ArrayList<TestPoint>();
        points.add(new TestPoint(0, 0));
        points.add(new TestPoint(0, 1));
        points.add(new TestPoint(1, 0));
        points.add(new TestPoint(1, 1));
        return points;
    }

    private List<Cluster<TestPoint>> createClusters() {
        List<Cluster<TestPoint>> clusters = new ArrayList<Cluster<TestPoint>>();
        clusters.add(new Cluster<TestPoint>(new TestPoint(0, 0)));
        clusters.add(new Cluster<TestPoint>(new TestPoint(1, 1)));
        return clusters;
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private double x;
        private double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(TestPoint point) {
            return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
        }

        public TestPoint centroidOf(Collection<TestPoint> points) {
            double totalX = 0;
            double totalY = 0;
            int count = 0;
            for (TestPoint point : points) {
                totalX += point.x;
                totalY += point.y;
                count++;
            }
            return new TestPoint(totalX / count, totalY / count);
        }
    }

}