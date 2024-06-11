package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final int dimension;
    private final int numberOfPoints;
    private final int numberOfClusters;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int dimension, int numberOfPoints, int numberOfClusters, EmptyClusterStrategy emptyStrategy) {
        this.dimension = dimension;
        this.numberOfPoints = numberOfPoints;
        this.numberOfClusters = numberOfClusters;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameterized.Parameters(name = "dimension={0}, numberOfPoints={1}, numberOfClusters={2}, emptyStrategy={3}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {2, 10, 3, EmptyClusterStrategy.LARGEST_VARIANCE},
                {3, 20, 5, EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                {4, 50, 10, EmptyClusterStrategy.FARTHEST_POINT},
                {5, 100, 20, EmptyClusterStrategy.ERROR},
        });
    }

    @Test
    public void testCluster() {
        Collection<T> points = createRandomPoints();
        KMeansPlusPlusClusterer<T> clustering = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        List<Cluster<T>> clusters = clustering.cluster(points, numberOfClusters, -1);

        // check that the number of clusters match the expected number
        assertEquals(numberOfClusters, clusters.size());

        // check that each cluster has at least one point
        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }

        // check that the points are correctly assigned to their clusters
        for (T point : points) {
            Cluster<T> cluster = getNearestCluster(clusters, point);
            assertEquals(cluster, point.getCluster());
        }

        // check that the centroids have converged
        for (int i = 0; i < numberOfClusters; i++) {
            Cluster<T> cluster = clusters.get(i);
            T centroid = cluster.getCenter();
            for (T point : cluster.getPoints()) {
                assertEquals(Precision.equals(point.distanceFrom(centroid), 0.0, 1e-6), true);
            }
        }
    }

    private Collection<T> createRandomPoints() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            T point = new RandomPoint(dimension, RandomUtils.nextDouble(0.0, 100.0));
            points.add(point);
        }
        return points;
    }

    private Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    private static class RandomPoint implements Clusterable<RandomPoint> {
        private final double[] values;

        public RandomPoint(int dimension, double maxValue) {
            this.values = new double[dimension];
            for (int i = 0; i < dimension; i++) {
                this.values[i] = RandomUtils.nextDouble(0.0, maxValue);
            }
        }

        @Override
        public double distanceFrom(T other) {
            RandomPoint otherPoint = (RandomPoint) other;
            double distance = 0.0;
            for (int i = 0; i < dimension; i++) {
                distance += Math.pow(this.values[i] - otherPoint.values[i], 2);
            }
            return Math.sqrt(distance);
        }

        @Override
        public Cluster<RandomPoint> getCluster() {
            return null;
        }

        @Override
        public T getCenter() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < dimension; i++) {
                sb.append(values[i]);
                if (i < dimension - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

}