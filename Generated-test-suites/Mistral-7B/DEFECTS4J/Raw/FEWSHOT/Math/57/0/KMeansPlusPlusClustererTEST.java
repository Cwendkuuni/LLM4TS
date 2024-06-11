package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasures;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCluster() {
        final Random random = new Random(123456789);
        final KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(i, 10.0 * i));
        }

        List<Cluster<TestPoint>> result = clusterer.cluster(points, 3, 10);

        assertNotNull(result);
        assertTrue(result.size() == 3);
        for (Cluster<TestPoint> cluster : result) {
            assertNotNull(cluster);
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check that each point is assigned to the correct cluster
        for (TestPoint point : points) {
            Cluster<TestPoint> expectedCluster = getExpectedCluster(point);
            Cluster<TestPoint> actualCluster = null;
            for (Cluster<TestPoint> cluster : result) {
                if (ClusterUtil.arePointsInSameCluster(cluster.getPoints(), point)) {
                    actualCluster = cluster;
                    break;
                }
            }
            assertNotNull(actualCluster);
            assertSame(expectedCluster, actualCluster);
        }
    }

    @Test
    public void testEmptyClusterStrategy() {
        final Random random = new Random(123456789);
        final KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);

        List<TestPoint> points = Collections.singletonList(new TestPoint(0, 0));
        List<Cluster<TestPoint>> result = clusterer.cluster(points, 1, 1);

        assertNotNull(result);
        assertTrue(result.size() == 1);
        Cluster<TestPoint> cluster = result.get(0);
        assertNotNull(cluster);
        assertTrue(cluster.getPoints().isEmpty());

        try {
            clusterer.cluster(Collections.emptyList(), 1, 1);
            fail();
        } catch (ConvergenceException e) {
            // expected
        }

        // Test other strategies
        for (EmptyClusterStrategy strategy : EmptyClusterStrategy.values()) {
            clusterer = new KMeansPlusPlusClusterer<>(random, strategy);
            points = new ArrayList<>();
            points.add(new TestPoint(0, 0));
            result = clusterer.cluster(points, 1, 1);
            if (strategy == EmptyClusterStrategy.ERROR) {
                assertNotNull(result);
                assertTrue(result.size() == 1);
                cluster = result.get(0);
                assertNotNull(cluster);
                assertTrue(cluster.getPoints().isEmpty());
            } else {
                assertNotNull(result);
                assertTrue(result.size() > 0);
                cluster = result.get(0);
                assertFalse(cluster.getPoints().isEmpty());
            }
        }
    }

    private Cluster<TestPoint> getExpectedCluster(TestPoint point) {
        int index = point.getX() / 3;
        return result.get(index);
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private final int x;
        private final double y;

        public TestPoint(int x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(Clusterable<TestPoint> other) {
            TestPoint otherPoint = (TestPoint) other;
            return DistanceMeasures.EUCLIDEAN_DISTANCE.distance(this, otherPoint);
        }

        public int getX() {
            return x;
        }

        public Cluster<TestPoint> getCenter() {
            return result.get(x / 3);
        }

        @Override
        public String toString() {
            return "TestPoint{" + "x=" + x + ", y=" + y + '}';
        }

        public static List<Cluster<TestPoint>> createClusters(int numClusters, List<TestPoint> points, Random random) {
            List<Cluster<TestPoint>> result = new ArrayList<>();
            for (int i = 0; i < numClusters; i++) {
                Cluster<TestPoint> cluster = new Cluster<TestPoint>() {{
                    setName("Cluster " + i);
                }};
                result.add(cluster);
            }
            for (TestPoint point : points) {
                Cluster<TestPoint> nearestCluster = null;
                double nearestDistance = Double.MAX_VALUE;
                for (Cluster<TestPoint> cluster : result) {
                    double distance = point.distanceFrom(cluster.getCenter());
                    if (distance < nearestDistance) {
                        nearestCluster = cluster;
                        nearestDistance = distance;
                    }
                }
                nearestCluster.addPoint(point);
            }
            return result;
        }

        public static void main(String[] args) {
            List<TestPoint> points = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                points.add(new TestPoint(random.nextInt(10) * 3 + random.nextInt(3), random.nextGaussian() * 20));
            }

            List<Cluster<TestPoint>> result = KMeansPlusPlusClustererTest.createClusters(3, points, new Random(123456789));
            KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(123456789), EmptyClusterStrategy.ERROR);
            result = clusterer.cluster(points, 3, 10);

            System.out.println("Initial clusters:");
            for (Cluster<TestPoint> cluster : result) {
                System.out.println(cluster);
            }

            System.out.println("Clusters after KMeansPlusPlusClusterer:");
            for (Cluster<TestPoint> cluster : result) {
                System.out.println(cluster);
            }
        }
    }

    static {
        result = TestPoint.createClusters(3, Collections.emptyList(), new Random(123456789));
    }
}

class ClusterUtil {
    static boolean arePointsInSameCluster(List<Clusterable<TestPoint>> clusterPoints, TestPoint testPoint) {
        Cluster<TestPoint> nearestCluster = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Clusterable<TestPoint> point : clusterPoints) {
            double distance = testPoint.distanceFrom((TestPoint) point);
            if (distance < nearestDistance) {
                nearestCluster = (Cluster<TestPoint>) point;
                nearestDistance = distance;
            }
        }
        return nearestCluster != null;
    }
}