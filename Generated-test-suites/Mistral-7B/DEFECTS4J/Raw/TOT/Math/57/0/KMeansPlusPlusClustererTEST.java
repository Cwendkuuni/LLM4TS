package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final int NUMBER_OF_POINTS = 10;
    private static final int NUMBER_OF_CLUSTERS = 3;

    private static final Random RANDOM = new Random();

    private KMeansPlusPlusClusterer<T> clustering;
    private List<T> points;

    public KMeansPlusPlusClustererTest(Class<T> clazz) {
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, EmptyClusterStrategy.LARGEST_VARIANCE);
        points = createRandomPoints(clazz, NUMBER_OF_POINTS);
    }

    @Before
    public void setup() {
        Mockito.spy(clustering);
        Mockito.doAnswer(invocation -> new DistanceMeasure() {
            @Override
            public double distance(T a, T b) {
                return a.distanceFrom(b);
            }
        }).when(clustering).getDistanceMeasure();
    }

    @Test
    public void testClusterWithEmptyList() {
        List<Cluster<T>> result = clustering.cluster(Collections.emptyList(), NUMBER_OF_CLUSTERS, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testClusterWithSinglePoint() {
        List<Cluster<T>> result = clustering.cluster(Arrays.asList(points.get(0)), NUMBER_OF_CLUSTERS, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        Cluster<T> cluster = result.get(0);
        assertEquals(points.get(0), cluster.getCenter());
        assertEquals(Arrays.asList(points.get(0)), cluster.getPoints());
    }

    @Test
    public void testClusterWithPoints() {
        List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 1);
        assertNotNull(result);
        assertEquals(NUMBER_OF_CLUSTERS, result.size());
        List<Cluster<T>> expected = chooseInitialCenters(points, NUMBER_OF_CLUSTERS, RANDOM);
        for (int i = 0; i < NUMBER_OF_CLUSTERS; i++) {
            assertEquals(expected.get(i).getCenter(), result.get(i).getCenter());
            assertEquals(expected.get(i).getPoints(), result.get(i).getPoints());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusterStrategyError() {
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, EmptyClusterStrategy.ERROR);
        try {
            List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testClusterWithMaxIterations() throws InterruptedException {
        List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 100);
        assertEquals(NUMBER_OF_CLUSTERS, result.size());
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, -1);
        assertEquals(NUMBER_OF_CLUSTERS, result.size());
    }

    @Test
    public void testClusterWithPointsTooFarFromCentroid() {
        List<T> pointsTooFar = new ArrayList<>();
        T pointFarthest = points.get(0);
        for (T point : points) {
            if (point.distanceFrom(pointFarthest) > 100.0) {
                pointsTooFar.add(point);
            }
        }
        List<Cluster<T>> result = clustering.cluster(pointsTooFar, NUMBER_OF_CLUSTERS, 100);
        assertNotNull(result);
        assertEquals(NUMBER_OF_CLUSTERS, result.size());
        for (Cluster<T> cluster : result) {
            assertNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            List<T> pointsInCluster = cluster.getPoints();
            assertEquals(1, pointsInCluster.size());
            T pointInCluster = pointsInCluster.get(0);
            assertEquals(pointInCluster, cluster.getCenter());
        }
    }

    @Test
    public void testClusterWithEmptyClusterDuringIteration() throws InterruptedException {
        List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 100);
        List<Cluster<T>> expected = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CLUSTERS - 1; i++) {
            expected.add(createRandomCluster(points, clazz));
        }
        expected.add(createEmptyCluster(clazz));

        for (int i = 0; i < NUMBER_OF_CLUSTERS; i++) {
            Cluster<T> cluster = result.get(i);
            Cluster<T> expectedCluster = expected.get(i);
            assertEquals(expectedCluster.getCenter(), cluster.getCenter());
            assertEquals(expectedCluster.getPoints(), cluster.getPoints());
        }
    }

    @Test
    public void testClusterWithMaxIterationsAndEmptyCluster() throws InterruptedException {
        List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 1000);
        List<Cluster<T>> expected = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CLUSTERS - 1; i++) {
            expected.add(createRandomCluster(points, clazz));
        }
        expected.add(createEmptyCluster(clazz));

        for (int i = 0; i < NUMBER_OF_CLUSTERS; i++) {
            Cluster<T> cluster = result.get(i);
            Cluster<T> expectedCluster = expected.get(i);
            assertEquals(expectedCluster.getCenter(), cluster.getCenter());
            assertEquals(expectedCluster.getPoints(), cluster.getPoints());
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testClusterWithMaxIterationsAndEmptyClusterStrategyLargestNumber() {
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        try {
            List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 1000);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test(expected = NoSolutionException.class)
    public void testClusterWithMaxIterationsAndEmptyClusterStrategyFarthestPoint() {
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, EmptyClusterStrategy.FARTHEST_POINT);
        try {
            List<Cluster<T>> result = clustering.cluster(points, NUMBER_OF_CLUSTERS, 1000);
            fail("Expected NoSolutionException");
        } catch (NoSolutionException e) {
            // Expected
        }
    }

    private List<Cluster<T>> chooseInitialCenters(Collection<T> points, int k, Random random) {
        return clustering.chooseInitialCenters(points, k, random);
    }

    private List<T> createEmptyCluster(Class<T> clazz) {
        List<T> emptyList = new ArrayList<>();
        return Arrays.asList(Mockito.spy(clazz.newInstance()));
    }

    private List<T> createRandomCluster(Collection<T> points, Class<T> clazz) {
        List<T> cluster = new ArrayList<>();
        T firstPoint = points.remove(RANDOM.nextInt(points.size()));
        cluster.add(firstPoint);
        int sum = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            T point = points.get(RANDOM.nextInt(points.size()));
            cluster.add(point);
            sum += point.distanceFrom(firstPoint) * point.distanceFrom(firstPoint);
            points.remove(point);
        }
        Random random = new Random();
        double r = random.nextDouble() * sum;
        for (int i = 0; i < points.size(); i++) {
            T point = points.get(i);
            double distance = point.distanceFrom(firstPoint);
            if (distance * distance >= r) {
                cluster.add(point);
                break;
            }
        }
        T center = cluster.get(0).centroidOf(cluster);
        return Arrays.asList(Mockito.spy(clazz.newInstance()));
    }

    private List<T> createRandomPoints(Class<T> clazz, int numberOfPoints) {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(clazz.newInstance());
        }
        return points;
    }

}