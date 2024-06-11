import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.point.BasicPoint;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private Random random;
    private double epsilon = 0.001;
    private KMeansPlusPlusClusterer<BasicPoint> kmeans;

    @Before
    public void setUp() {
        random = new Random();
        kmeans = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testEmptyClusterStrategy() {
        List<BasicPoint> points = new ArrayList<>();
        assertEquals(0, kmeans.cluster(points, NUM_CLUSTERS, -1).size());
    }

    @Test
    public void testChooseInitialCenters() {
        List<BasicPoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new BasicPoint(i + 1, i + 1));
        }
        List<Cluster<BasicPoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, 1);
        assertEquals(NUM_CLUSTERS, clusters.size());
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            assertNotNull(clusters.get(i).getCenter());
            assertEquals(1, clusters.get(i).getPoints().size());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        List<BasicPoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new BasicPoint(i + 1, i + 1));
        }
        List<BasicPoint> initialPoints = new ArrayList<>(points);
        List<Cluster<BasicPoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, 1);
        assignPointsToClusters(clusters, points);
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            int expectedSize = (int) Math.ceil(NUM_POINTS / (double) NUM_CLUSTERS);
            assertEquals(expectedSize, clusters.get(i).getPoints().size());
            for (BasicPoint p : clusters.get(i).getPoints()) {
                assertTrue(initialPoints.remove(p));
            }
        }
        assertEquals(0, initialPoints.size());
    }

    @Test
    public void testIterations() {
        List<BasicPoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new BasicPoint(i + 1, i + 1));
        }
        List<Cluster<BasicPoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, 5);
        for (Cluster<BasicPoint> cluster : clusters) {
            Cluster<BasicPoint> nearest = getNearestCluster(clusters, cluster.getCenter());
            double distance = cluster.getCenter().distanceFrom(nearest.getCenter());
            assertEquals(0, distance, epsilon);
        }
    }

    private void assignPointsToClusters(List<Cluster<BasicPoint>> clusters, List<BasicPoint> points) {
        for (int i = 0; i < points.size(); i++) {
            Cluster<BasicPoint> cluster = getNearestCluster(clusters, points.get(i));
            cluster.addPoint(points.get(i));
        }
    }

    private Cluster<BasicPoint> getNearestCluster(List<Cluster<BasicPoint>> clusters, BasicPoint point) {
        Cluster<BasicPoint> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<BasicPoint> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }
}