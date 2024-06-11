import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Clusterable>> clusters = clusterer.cluster(createTestData(), 2, 10);
        validateClusters(clusters);
    }

    private List<Clusterable> createTestData() {
        List<Clusterable> testData = new ArrayList<>();
        testData.add(new Clusterable(1, 1));
        testData.add(new Clusterable(2, 2));
        testData.add(new Clusterable(10, 10));
        testData.add(new Clusterable(11, 11));
        testData.add(new Clusterable(20, 20));
        testData.add(new Clusterable(21, 21));
        return testData;
    }

    private void validateClusters(List<Cluster<Clusterable>> clusters) {
        assertEquals(2, clusters.size());

        Clusterable center1 = clusters.get(0).getCenter();
        Clusterable center2 = clusters.get(1).getCenter();

        for (Cluster<Clusterable> cluster : clusters) {
            for (Clusterable point : cluster.getPoints()) {
                assertTrue(point.distanceFrom(center1) < point.distanceFrom(center2));
            }
        }
    }
}

class Clusterable {
    private double x;
    private double y;

    Clusterable(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceFrom(Clusterable other) {
        double dx = x - other.getX();
        double dy = y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
}