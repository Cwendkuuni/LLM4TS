package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.MemoryBasedDistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    private static final double EPS = 1e-6;
    private static final int N = 25;
    private static final double RADIUS = 3.5;
    private static final int K = 4;

    @org.junit.Before
    public void setUp() {
        // setup the random number generator
        KMeansPlusPlusClusterer.setRandom(new Random());
    }

    @org.junit.Test
    public void testChooseInitialCenters() {
        final List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            final Double[] p = new Double[2];
            p[0] = Math.random() * 2.0 * RADIUS - RADIUS;
            p[1] = Math.random() * 2.0 * RADIUS - RADIUS;
            points.add(new ClusterableDoubleArray(p));
        }

        final List<Cluster<Clusterable<Double[]>>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, K, new Random());
        assertNotNull(initialCenters);
        assertEquals(K, initialCenters.size());

        final DistanceMeasure distanceMeasure = new MemoryBasedDistanceMeasure();
        for (final Cluster<Clusterable<Double[]>> cluster : initialCenters) {
            assertNotNull(cluster);
            assertEquals(1, cluster.getPoints().size());
        }
    }

    @org.junit.Test
    public void testCluster() throws ConvergenceException {
        final List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            final Double[] p = new Double[2];
            p[0] = Math.random() * 2.0 * RADIUS - RADIUS;
            p[1] = Math.random() * 2.0 * RADIUS - RADIUS;
            points.add(new ClusterableDoubleArray(p));
        }
        final List<Cluster<Clusterable<Double[]>>> clusters = KMeansPlusPlusClusterer.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());

        final DistanceMeasure distanceMeasure = new MemoryBasedDistanceMeasure();
        for (final Cluster<Clusterable<Double[]>> cluster : clusters) {
            assertNotNull(cluster);
            assertEquals(0, cluster.getPoints().size() % 5, "Number of points in each cluster should be close to equal.");
            final Double[] center = cluster.getCenter().toArray();
            final NormalDistribution normal = new NormalDistribution(center[0], center[1]);
            double sum = 0.0;
            for (final Double[] point : cluster.getPoints()) {
                sum += distanceMeasure.distance(point, center);
            }
            final Variance variance = new Variance();
            variance.increment(sum);
            final double expectedVariance = variance.getResult();
            assertEquals(expectedVariance, cluster.getVariance(), EPS);
        }
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() throws ConvergenceException {
        final List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < N - 1; i++) {
            final Double[] p = new Double[2];
            p[0] = Math.random() * 2.0 * RADIUS - RADIUS;
            p[1] = Math.random() * 2.0 * RADIUS - RADIUS;
            points.add(new ClusterableDoubleArray(p));
        }
        final KMeansPlusPlusClusterer<Clusterable<Double[]>> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        clusterer.cluster(points, K, 10);
    }

    @org.junit.Test
    public void testEmptyClusterStrategy() throws ConvergenceException {
        final List<Clusterable<Double[]>> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            final Double[] p = new Double[2];
            p[0] = Math.random() * 2.0 * RADIUS - RADIUS;
            p[1] = Math.random() * 2.0 * RADIUS - RADIUS;
            points.add(new ClusterableDoubleArray(p));
        }
        final List<Cluster<Clusterable<Double[]>>> clusters = KMeansPlusPlusClusterer.cluster(points, K, 10);
        assertNotNull(clusters);
        assertEquals(K, clusters.size());

        final boolean largestVariance = KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE.equals(KMeansPlusPlusClusterer.getDefaultEmptyClusterStrategy());
        final boolean largestPointsNumber = KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER.equals(KMeansPlusPlusClusterer.getDefaultEmptyClusterStrategy());
        final boolean farthestPoint = KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT.equals(KMeansPlusPlusClusterer.getDefaultEmptyClusterStrategy());
        final boolean error = KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR.equals(KMeansPlusPlusClusterer.getDefaultEmptyClusterStrategy());

        if (largestVariance) {
            for (int i = 0; i < clusters.size(); i++) {
                final Cluster<Clusterable<Double[]>> cluster = clusters.get(i);
                if (cluster.getPoints().isEmpty()) {
                    final Cluster<Clusterable<Double[]>> clusterWithLargestVariance = findClusterWithLargestVariance(clusters, new MemoryBasedDistanceMeasure());
                    assertNotNull(clusterWithLargestVariance);
                    final Double[] center = clusterWithLargestVariance.getCenter().toArray();
                    final Clusterable<Double[]> point = clusters.get(i).getPoints().get(0);
                    assertEquals(point.getDimension(), 2);
                    final double[] p = point.toArray();
                    assertEquals(center[0], p[0], EPS);
                    assertEquals(center[1], p[1], EPS);
                    break;
                }
            }
        } else if (largestPointsNumber) {
            for (int i = 0; i < clusters.size(); i++) {
                final Cluster<Clusterable<Double[]>> cluster = clusters.get(i);
                if (cluster.getPoints().isEmpty()) {
                    final Cluster<Clusterable<Double[]>> clusterWithLargestNumberOfPoints = findClusterWithLargestNumberOfPoints(clusters, new MemoryBasedDistanceMeasure());
                    assertNotNull(clusterWithLargestNumberOfPoints);
                    final Double[] center = clusterWithLargestNumberOfPoints.getCenter().toArray();
                    final Clusterable<Double[]> point = clusters.get(i).getPoints().get(0);
                    assertEquals(point.getDimension(), 2);
                    final double[] p = point.toArray();
                    assertEquals(center[0], p[0], EPS);
                    assertEquals(center[1], p[1], EPS);
                    break;
                }
            }
        } else if (farthestPoint) {
            for (int i = 0; i < clusters.size(); i++) {
                final Cluster<Clusterable<Double[]>> cluster = clusters.get(i);
                if (cluster.getPoints().isEmpty()) {
                    final Cluster<Clusterable<Double[]>> clusterWithFarthestPoint = findClusterWithFarthestPoint(clusters, new MemoryBasedDistanceMeasure());
                    assertNotNull(clusterWithFarthestPoint);
                    final Double[] center = clusterWithFarthestPoint.getCenter().toArray();
                    final Clusterable<Double[]> point = clusters.get(i).getPoints().get(0);
                    assertEquals(point.getDimension(), 2);
                    final double[] p = point.toArray();
                    assertEquals(center[0], p[0], EPS);
                    assertEquals(center[1], p[1], EPS);
                    break;
                }
            }
        } else {
            // Should not reach this point
            fail("Unexpected empty cluster strategy.");
        }
    }

    private Cluster<Clusterable<Double[]>> findClusterWithLargestVariance(List<Cluster<Clusterable<Double[]>>> clusters, DistanceMeasure distanceMeasure) {
        Cluster<Clusterable<Double[]>> largestVarianceCluster = null;
        double largestVariance = 0.0;
        for (final Cluster<Clusterable<Double[]>> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                final Variance variance = new Variance();
                for (final Clusterable<Double[]> point : cluster.getPoints()) {
                    variance.increment(distanceMeasure.distance(point.toArray(), cluster.getCenter().toArray()));
                }
                final double currentVariance = variance.getResult();
                if (currentVariance > largestVariance) {
                    largestVariance = currentVariance;
                    largestVarianceCluster = cluster;
                }
            }
        }
        return largestVarianceCluster;
    }

    private Cluster<Clusterable<Double[]>> findClusterWithLargestNumberOfPoints(List<Cluster<Clusterable<Double[]>>> clusters, DistanceMeasure distanceMeasure) {
        Cluster<Clusterable<Double[]>> largestNumberCluster = null;
        int largestNumber = 0;
        for (final Cluster<Clusterable<Double[]>> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                final int currentNumber = cluster.getPoints().size();
                if (currentNumber > largestNumber) {
                    largestNumber = currentNumber;
                    largestNumberCluster = cluster;
                }
            }
        }
        return largestNumberCluster;
    }

    private Cluster<Clusterable<Double[]>> findClusterWithFarthestPoint(List<Cluster<Clusterable<Double[]>>> clusters, DistanceMeasure distanceMeasure) {
        Cluster<Clusterable<Double[]>> farthestCluster = null;
        Double farthestDistance = Double.MAX_VALUE;
        for (final Cluster<Clusterable<Double[]>> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                final Double[] center = cluster.getCenter().toArray();
                for (final Clusterable<Double[]> point : cluster.getPoints()) {
                    final double distance = distanceMeasure.distance(point.toArray(), center);
                    if (distance < farthestDistance) {
                        farthestCluster = cluster;
                        farthestDistance = distance;
                    }
                }
            }
        }
        return farthestCluster;
    }

}

class ClusterableDoubleArray implements Clusterable<Double[]> {

    private final Double[] data;

    public ClusterableDoubleArray(Double[] data) {
        this.data = data;
    }

    @Override
    public Double[] toArray() {
        return data;
    }

    @Override
    public int getDimension() {
        return data.length;
    }

    @Override
    public double distanceFrom(Clusterable<Double[]> other) {
        final Double[] otherData = other.toArray();
        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i] - otherData[i], 2);
        }
        return Math.sqrt(sum);
    }
}