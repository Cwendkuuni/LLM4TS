import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Point>> result = clusterer.cluster(points, 2, 10);
        assertEquals(2, result.size());
    }

    @Test
    public void testChooseInitialCenters() {
        Random random = new Random();
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3));
        List<Cluster<Point>> centers = KMeansPlusPlusClusterer.chooseInitialCenters(points, 2, random);
        assertEquals(2, centers.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        Cluster<Point> cluster1 = new Cluster<>(new Point(1, 1));
        Cluster<Point> cluster2 = new Cluster<>(new Point(2, 2));
        List<Cluster<Point>> clusters = new ArrayList<>();
        clusters.add(cluster1);
        clusters.add(cluster2);

        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, Arrays.asList(p1, p2));

        assertEquals(1, cluster1.getPoints().size());
        assertTrue(cluster1.getPoints().contains(p1));
        assertEquals(1, cluster2.getPoints().size());
        assertTrue(cluster2.getPoints().contains(p2));
    }

    @Test
    public void testEmptyClusterException() {
        Random random = new Random();
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(random);
        try {
            clusterer.cluster(points, 2, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    // Add more test cases for other methods as needed

}