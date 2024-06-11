import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, i));
        }
        List<Cluster<TestPoint>> result = clusterer.cluster(points, 3, -1);
        assertEquals(3, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<TestPoint> points = new ArrayList<>();
        List<Cluster<TestPoint>> result = clusterer.cluster(points, 3, -1);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, i));
        }
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, -1);
        Cluster<TestPoint> largestVarianceCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                largestVarianceCluster = cluster;
                break;
            }
        }
        TestPoint point = clusterer.getPointFromLargestVarianceCluster(clusters);
        List<TestPoint> largestVarianceClusterPoints = largestVarianceCluster.getPoints();
        assertEquals(largestVarianceClusterPoints.get(0), point);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, i));
        }
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, -1);
        Cluster<TestPoint> largestNumberCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                largestNumberCluster = cluster;
                break;
            }
        }
        TestPoint point = clusterer.getPointFromLargestNumberCluster(clusters);
        List<TestPoint> largestNumberClusterPoints = largestNumberCluster.getPoints();
        assertEquals(largestNumberClusterPoints.get(0), point);
    }

    @Test
    public void testGetFarthestPoint() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, i));
        }
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, -1);
        Cluster<TestPoint> farthestPointCluster = null;
        for (Cluster<TestPoint> cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                farthestPointCluster = cluster;
                break;
            }
        }
        TestPoint point = clusterer.getFarthestPoint(clusters);
        List<TestPoint> farthestPointClusterPoints = farthestPointCluster.getPoints();
        TestPoint expectedPoint = null;
        for (TestPoint testPoint : points) {
            if (!farthestPointCluster.getPoints().contains(testPoint)) {
                expectedPoint = testPoint;
                break;
            }
        }
        assertEquals(expectedPoint, point);
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new Random(1);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, i));
        }
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, -1);
        TestPoint point = new TestPoint(5, 5);
        Cluster<TestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
        TestPoint nearestClusterCenter = nearestCluster.getCenter();
        TestPoint expectedClusterCenter = clusters.get(0).getCenter();
        assertEquals(expectedClusterCenter, nearestClusterCenter);
    }

    private static class TestPoint implements Clusterable<TestPoint> {

        private int id;

        public TestPoint(int id, double value) {
            this.id = id;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            TestPoint otherPoint = (TestPoint) other;
            return Math.abs(this.id - otherPoint.getId());
        }

        @Override
        public double distanceFrom(Cluster<TestPoint> cluster) {
            TestPoint clusterCenter = cluster.getCenter();
            return Math.abs(this.id - clusterCenter.getId());
        }

        @Override
        public double distanceFrom(Cluster<TestPoint> cluster, double weight) {
            return this.distanceFrom(cluster);
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Cluster<TestPoint> getCluster() {
            return null;
        }

        @Override
        public void setCluster(Cluster<TestPoint> cluster) {
            //Do nothing
        }
    }

}