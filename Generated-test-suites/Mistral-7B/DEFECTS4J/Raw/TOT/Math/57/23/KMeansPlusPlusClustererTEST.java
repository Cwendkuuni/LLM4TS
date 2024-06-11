package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.junit.Before;
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

    private static final double EPSILON = 1e-6;
    private static final int K = 3;
    private static final double RADIUS = 50.0;
    private static final double MEAN = 100.0;
    private static final double STDDEV = 10.0;
    private static final int MAX_ITERATIONS = 10;

    private final Random random;
    private KMeansPlusPlusClusterer<T> clustering;

    @Parameterized.Parameters(name = "{0}, EmptyStrategy={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE},
                {new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                {new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT},
                {new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR}
        });
    }

    public KMeansPlusPlusClustererTest(Random random) {
        this.random = random;
    }

    @Before
    public void setUp() {
        this.clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testClusterEmptyCollection() {
        Collection<T> points = new ArrayList<T>();
        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(0, clusters.size());
    }

    @Test
    public void testClusterOnePoint() {
        T point = new SimplePoint(MEAN);
        Collection<T> points = new ArrayList<T>();
        points.add(point);
        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(1, clusters.size());
        Cluster<T> cluster = clusters.get(0);
        assertNotNull(cluster);
        assertEquals(point, cluster.getCenter());
    }

    @Test
    public void testClusterTwoPoints() {
        T point1 = new SimplePoint(MEAN - STDDEV);
        T point2 = new SimplePoint(MEAN + STDDEV);
        Collection<T> points = new ArrayList<T>();
        points.add(point1);
        points.add(point2);
        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(2, clusters.size());

        Cluster<T> cluster1 = clusters.get(0);
        Cluster<T> cluster2 = clusters.get(1);
        assertNotNull(cluster1);
        assertNotNull(cluster2);

        assertEquals(point1, cluster1.getCenter());
        assertEquals(point2, cluster2.getCenter());

        assertEquals(point1, cluster1.getPoints().get(0));
        assertEquals(point2, cluster2.getPoints().get(0));
    }

    @Test(expected = ConvergenceException.class)
    public void testErrorEmptyCluster() {
        T point1 = new SimplePoint(MEAN - STDDEV);
        T point2 = new SimplePoint(MEAN);
        T point3 = new SimplePoint(MEAN + STDDEV);
        Collection<T> points = new ArrayList<T>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);

        // Empty cluster strategy is set to ERROR
        assertEquals(3, clusters.size());

        List<T> cluster1Points = clusters.get(0).getPoints();
        List<T> cluster2Points = clusters.get(1).getPoints();
        List<T> cluster3Points = clusters.get(2).getPoints();

        // There should be only one point in each cluster
        assertEquals(1, cluster1Points.size());
        assertEquals(1, cluster2Points.size());
        assertEquals(1, cluster3Points.size());

        // Check that each cluster contains the correct point
        for (int i = 0; i < 3; i++) {
            int index = i;
            if (i == 1 && clustering.emptyStrategy == KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR) {
                index = 2;
            }
            assertEquals(points.get(index), clusters.get(0).getPoints().get(0));
            assertEquals(points.get(index), clusters.get(1).getPoints().get(0));
            assertEquals(points.get(index), clusters.get(2).getPoints().get(0));
        }

        // Check that the centroids match the points
        for (int i = 0; i < 3; i++) {
            int index = i;
            if (i == 1 && clustering.emptyStrategy == KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR) {
                index = 2;
            }
            T center = clusters.get(i).getCenter();
            assertEquals(points.get(index), center);
        }

        // Test error scenario
        List<Cluster<T>> newClusters = new ArrayList<Cluster<T>>();
        for (final Cluster<T> cluster : clusters) {
            T newCenter;
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            } else {
                newCenter = cluster.getCenter().centroidOf(cluster.getPoints());
                if (!newCenter.equals(cluster.getCenter())) {
                    throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
                }
            }
            newClusters.add(new Cluster<T>(newCenter));
        }

        List<Cluster<T>> newClusters2 = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(3, newClusters2.size());

        for (int i = 0; i < 3; i++) {
            int index = i;
            if (i == 1 && clustering.emptyStrategy == KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR) {
                index = 2;
            }
            assertEquals(points.get(index), newClusters2.get(i).getPoints().get(0));
            assertEquals(points.get(index), newClusters2.get(i).getCenter());
        }
    }

    @Test
    public void testClusterRandomlyGeneratedPoints() {
        int dim = 2;
        int numPoints = 100;
        NormalDistribution distribution = new NormalDistribution(MEAN, STDDEV);
        Collection<T> points = new ArrayList<T>();
        for (int i = 0; i < numPoints; i++) {
            double[] coordinates = new double[dim];
            for (int j = 0; j < dim; j++) {
                coordinates[j] = distribution.inverseCumulativeProbability(random.nextDouble()) * RADIUS + MEAN;
            }
            points.add(new SimplePoint(coordinates));
        }

        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(K, clusters.size());

        for (Cluster<T> cluster : clusters) {
            assertEquals(cluster.getPoints().size(), 0);
            T center = cluster.getCenter();
            double variance = new Variance().inverse(new EuclideanDistance().vector(cluster.getPoints().toArray(new T[0]), center));
            assertEquals(variance, new Variance().estimate(cluster.getPoints().toArray(new T[0])));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNegativeMaxIterations() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(new ArrayList<T>(), K, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentZeroK() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(new ArrayList<T>(), 0, MAX_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNegativeK() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(new ArrayList<T>(), -1, MAX_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNullCollection() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(null, K, MAX_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNullCollectionPoints() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<T>> clusters = clustering.cluster(null, K, MAX_ITERATIONS);
        assertEquals(0, clusters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentEmptyCollection() {
        clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<T>> clusters = clustering.cluster(new ArrayList<T>(), K, MAX_ITERATIONS);
        assertEquals(0, clusters.size());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsReached() {
        int dim = 2;
        int numPoints = 100;
        Collection<T> points = new ArrayList<T>();
        for (int i = 0; i < numPoints; i++) {
            double[] coordinates = new double[dim];
            for (int j = 0; j < dim; j++) {
                coordinates[j] = random.nextDouble() * RADIUS * 2.0 - RADIUS;
            }
            points.add(new SimplePoint(coordinates));
        }
        List<Cluster<T>> clusters = clustering.cluster(points, K, MAX_ITERATIONS);
        assertEquals(K, clusters.size());

        for (Cluster<T> cluster : clusters) {
            assertEquals(cluster.getPoints().size(), 0);
        }
    }
}

class SimplePoint implements Clusterable<SimplePoint> {

    private double[] coordinates;

    public SimplePoint(double... coordinates) {
        this.coordinates = coordinates;
    }

    public SimplePoint(double mean, double stddev) {
        this(new double[]{mean, mean + stddev, mean - stddev});
    }

    @Override
    public double distanceFrom(Clusterable<SimplePoint> point) {
        SimplePoint otherPoint = (SimplePoint) point;
        double sum = 0;
        for (int i = 0; i < coordinates.length; i++) {
            sum += Math.pow(coordinates[i] - otherPoint.coordinates[i], 2);
        }
        return Math.sqrt(sum);
    }

    @Override
    public Cluster<SimplePoint> getCenter() {
        return new Cluster<SimplePoint>(new SimplePoint(Arrays.stream(coordinates).sum() / coordinates.length));
    }
}