import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.empty.EmptyClusterStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyConstructor() {
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>();
        assertNotNull(clustering);
    }

    @Test
    public void testKMeansPlusPlusConstructor() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        assertNotNull(clustering);
    }

    @Test
    public void testKMeansPlusPlusConstructorWithStrategy() {
        Random random = new Random();
        EmptyClusterStrategy strategy = EmptyClusterStrategy.FARTHEST_POINT;
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, strategy);
        assertNotNull(clustering);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeK() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, 2);
        List<Cluster<TestPoint>> result = clustering.cluster(testData, 3, 100);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollection() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        clustering.cluster(null, 2, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        clustering.cluster(Collections.emptyList(), 2, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<TestPoint>> result = clustering.cluster(testData, 2, 100);
        Assert.assertNotNull(result);
    }

    @Test
    public void testClusterMethod() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> result = clustering.cluster(testData, 2, 100);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        for (Cluster<TestPoint> cluster : result) {
            Assert.assertFalse(cluster.isEmpty());
        }
        for (int i = 0; i < testData.size(); i++) {
            TestPoint point = testData.get(i);
            Cluster<TestPoint> cluster = result.get(point.getClusterIndex());
            Assert.assertTrue(cluster.contains(point));
        }
    }

    @Test
    public void testChooseInitialCentersMethod() {
        List<TestPoint> points = testData;
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> initialCenters = clustering.chooseInitialCenters(points, 2, random);
        Assert.assertEquals(2, initialCenters.size());
        for (Cluster<TestPoint> cluster : initialCenters) {
            Assert.assertFalse(cluster.isEmpty());
        }
    }

    @Test
    public void testGetNearestClusterMethod() {
        List<TestPoint> points = testData;
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 2, 100);
        TestPoint point = testData.get(0);
        Cluster<TestPoint> expectedCluster = clusters.get(point.getClusterIndex());
        Cluster<TestPoint> nearestCluster = clustering.getNearestCluster(clusters, point);
        Assert.assertEquals(expectedCluster, nearestCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceClusterMethod() {
        List<TestPoint> points = testData;
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 2, 100);
        Cluster<TestPoint> largestVarianceCluster = findLargestVarianceCluster(clusters);
        TestPoint point = largestVarianceCluster.getPoints().get(0);
        TestPoint resultPoint = clustering.getPointFromLargestVarianceCluster(clusters);
        Assert.assertEquals(point, resultPoint);
    }

    @Test
    public void testGetPointFromLargestNumberClusterMethod() {
        List<TestPoint> points = testData;
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 2, 100);
        Cluster<TestPoint> largestNumberCluster = findLargestNumberCluster(clusters);
        TestPoint point = largestNumberCluster.getPoints().get(0);
        TestPoint resultPoint = clustering.getPointFromLargestNumberCluster(clusters);
        Assert.assertEquals(point, resultPoint);
    }

    @Test
    public void testGetFarthestPointMethod() {
        List<TestPoint> points = testData;
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 2, 100);
        Cluster<TestPoint> farthestCluster = findFarthestCluster(clusters);
        TestPoint point = farthestCluster.getPoints().get(0);
        TestPoint resultPoint = clustering.getFarthestPoint(clusters);
        Assert.assertEquals(point, resultPoint);
    }

    private List<Cluster<TestPoint>> testData = new ArrayList<>();
    static {
        for (int i = 0; i < 10; i++) {
            testData.add(new TestPoint(i, i % 2 == 0 ? 1.0 : 2.0));
        }
    }

    private Cluster<TestPoint> findLargestVarianceCluster(List<Cluster<TestPoint>> clusters) {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<TestPoint> maxVarianceCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (!cluster.isEmpty()) {
                Variance stat = new Variance();
                for (TestPoint point : cluster.getPoints()) {
                    stat.increment(point.getX() - cluster.getCenter().getX());
                }
                double variance = stat.getResult();
                if (variance > maxVariance) {
                    maxVariance = variance;
                    maxVarianceCluster = cluster;
                }
            }
        }
        return maxVarianceCluster;
    }

    private Cluster<TestPoint> findLargestNumberCluster(List<Cluster<TestPoint>> clusters) {
        int maxNumber = 0;
        Cluster<TestPoint> maxNumberCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (!cluster.isEmpty()) {
                int number = cluster.getPoints().size();
                if (number > maxNumber) {
                    maxNumber = number;
                    maxNumberCluster = cluster;
                }
            }
        }
        return maxNumberCluster;
    }

    private Cluster<TestPoint> findFarthestCluster(List<Cluster<TestPoint>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<TestPoint> maxDistanceCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (!cluster.isEmpty()) {
                TestPoint center = cluster.getCenter();
                double distance = Double.MAX_VALUE;
                for (TestPoint point : cluster.getPoints()) {
                    distance = Math.min(distance, point.distanceFrom(center));
                }
                if (distance > maxDistance) {
                    maxDistance = distance;
                    maxDistanceCluster = cluster;
                }
            }
        }
        return maxDistanceCluster;
    }

    public static class TestPoint implements Clusterable<TestPoint> {

        private int id;
        private double x;

        public TestPoint(int id, double x) {
            this.id = id;
            this.x = x;
        }

        @Override
        public int getClusterIndex() {
            return id % 2;
        }

        @Override
        public double distanceFrom(Cluster<TestPoint> cluster) {
            return Math.abs(cluster.getCenter().getX() - x);
        }

        public double getX() {
            return x;
        }

        @Override
        public Cluster<TestPoint> getCenter() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TestPoint> getPoints() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addPoint(TestPoint point) {
            throw new UnsupportedOperationException();
        }

    }

}