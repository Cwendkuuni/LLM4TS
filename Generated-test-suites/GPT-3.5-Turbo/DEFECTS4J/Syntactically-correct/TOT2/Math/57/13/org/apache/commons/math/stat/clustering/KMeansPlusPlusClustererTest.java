import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<MyPoint> points = new ArrayList<>();
        points.add(new MyPoint(1, 1));
        points.add(new MyPoint(2, 2));
        points.add(new MyPoint(3, 3));

        List<Cluster<MyPoint>> clusters = clusterer.cluster(points, 2, 10);

        assertEquals(2, clusters.size());

        for (Cluster<MyPoint> cluster : clusters) {
            assertNotNull(cluster);
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        List<MyPoint> points = new ArrayList<>();
        List<Cluster<MyPoint>> clusters = clusterer.cluster(points, 2, 10);

        assertNull(clusters);
    }

    @Test
    public void testChooseInitialCenters() {
        Random random = new Random();
        List<MyPoint> points = new ArrayList<>();
        points.add(new MyPoint(1, 1));
        points.add(new MyPoint(2, 2));
        points.add(new MyPoint(3, 3));

        List<Cluster<MyPoint>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, 2, random);

        assertEquals(2, initialCenters.size());
        for (Cluster<MyPoint> center : initialCenters) {
            assertNotNull(center);
            assertNotNull(center.getCenter());
        }
    }

    // Add more test cases as needed for other methods

}