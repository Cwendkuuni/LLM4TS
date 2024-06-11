import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final Random random;
    private final int k;
    private final int maxIterations;
    private final List<T> points;
    private final KMeansPlusPlusClusterer<T> clustering;
    private final List<Cluster<T>> expectedClusters;

    public KMeansPlusPlusClustererTest(List<T> points, int k, int maxIterations) {
        this.points = points;
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random();
        this.clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        this.expectedClusters = clustering.cluster(points, k, maxIterations);
    }

    @Parameterized.Parameters(name = "{0} with {1} iterations and {2} clusters")
    public static List<Object[]> testData() {
        // Add your test data here
        List<Object[]> testData = new ArrayList<>();
        // Test case 1: Empty collection
        testData.add(new Object[]{Collections.emptyList(), 2, -1});
        // Test case 2: Simple test with 3 points and 2 clusters
        List<T> points2 = new ArrayList<>();
        points2.add(new DummyPoint(1.0, 1.0));
        points2.add(new DummyPoint(2.0, 2.0));
        points2.add(new DummyPoint(3.0, 3.0));
        testData.add(new Object[]{points2, 2, 100});
        return testData;
    }

    @Test
    public void testCluster() {
        assertEquals(expectedClusters.size(), k);
        for (int i = 0; i < k; i++) {
            Cluster<T> cluster = expectedClusters.get(i);
            assertNotNull(cluster);
            assertFalse(cluster.isEmpty());
            assertEquals(1, cluster.getPoints().size());
        }
    }

    // Add test cases for the edge cases and exception handling scenarios identified

    // Basic test case for chooseInitialCenters method
    @Test
    public void testChooseInitialCenters() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new DummyPoint(i, i));
        }
        List<Cluster<T>> initialCenters = clustering.chooseInitialCenters(points, 3, random);
        assertEquals(3, initialCenters.size());
        for (Cluster<T> cluster : initialCenters) {
            assertFalse(cluster.isEmpty());
        }
    }

    // Add more test cases here to cover all methods in the KMeansPlusPlusClusterer class

    private static class DummyPoint implements Clusterable<DummyPoint> {
        private final double x;
        private final double y;

        public DummyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            if (other instanceof DummyPoint) {
                DummyPoint dp = (DummyPoint) other;
                return Math.sqrt(Math.pow(x - dp.x, 2) + Math.pow(y - dp.y, 2));
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Cluster<DummyPoint> getCenter() {
            throw new UnsupportedOperationException();
        }
    }

}