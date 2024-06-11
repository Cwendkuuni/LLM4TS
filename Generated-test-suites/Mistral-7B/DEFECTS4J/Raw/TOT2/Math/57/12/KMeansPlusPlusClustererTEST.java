package org.apache.commons.math.stat.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing {@link KMeansPlusPlusClusterer} class.<br>
 * Each method in the class is tested with different input data.<br>
 * Edge cases and exception handling scenarios are also covered.<br>
 */
public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyConstructor() {
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        assertNotNull(clustering);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        final Random random = new Random();
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>(random);
        assertNotNull(clustering);
        assertSame(random, clustering.getRandom());
    }

    @Test
    public void testConstructorWithRandomGeneratorAndStrategy() {
        final Random random = new Random();
        final KMeansPlusPlusClusterer.EmptyClusterStrategy strategy = KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE;
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>(random, strategy);
        assertNotNull(clustering);
        assertSame(random, clustering.getRandom());
        assertEquals(strategy, clustering.getEmptyStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollection() {
        new KMeansPlusPlusClusterer<>(new ArrayList<>(), 3, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        new KMeansPlusPlusClusterer<>(Collections.emptyList(), 3, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfClusters() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0));
        new KMeansPlusPlusClusterer<>(points, -3, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0));
        new KMeansPlusPlusClusterer<>(points, 3, -1);
    }

    @Test
    public void testClusterMethodWithNullCollection() {
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        assertNull(clustering.cluster(null, 3, 10));
    }

    @Test
    public void testClusterMethodWithEmptyCollection() {
        final List<SimplePoint> points = Collections.emptyList();
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        assertEquals(Collections.emptyList(), clustering.cluster(points, 3, 10));
    }

    @Test
    public void testClusterMethodWithTwoPoints() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        for (final Cluster<SimplePoint> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }
    }

    @Test
    public void testClusterMethodWithFivePoints() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0),
                new SimplePoint(3.0), new SimplePoint(4.0), new SimplePoint(5.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 3, 10);
        assertEquals(3, clusters.size());
        for (final Cluster<SimplePoint> cluster : clusters) {
            assertEquals(2, cluster.getPoints().size());
        }
    }

    @Test
    public void testClusterMethodWithSamePoints() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(1.0),
                new SimplePoint(1.0), new SimplePoint(1.0), new SimplePoint(1.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            final int size = clusters.get(i).getPoints().size();
            assertEquals(size == 2 ? 2 : 3, size);
        }
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
        final Cluster<SimplePoint> emptyCluster = clusters.get(1);
        assertTrue(emptyCluster.getPoints().isEmpty());
        final Cluster<SimplePoint> nonEmptyCluster = clusters.get(0);
        assertFalse(nonEmptyCluster.getPoints().isEmpty());
        assertEquals(EmptyClusterStrategy.LARGEST_VARIANCE, clustering.getEmptyStrategy());
        final T pointToAdd = new SimplePoint(Double.MAX_VALUE);
        clusters.get(0).addPoint(pointToAdd);
        final Cluster<SimplePoint> updatedNonEmptyCluster = clusters.get(0);
        assertFalse(updatedNonEmptyCluster.getPoints().isEmpty());
        assertEquals(1, updatedNonEmptyCluster.getPoints().size());
        assertEquals(updatedNonEmptyCluster.getCenter().getX(), pointToAdd.getX(), 0.001);
        final Cluster<SimplePoint> updatedEmptyCluster = clusters.get(1);
        assertFalse(updatedEmptyCluster.getPoints().isEmpty());
        assertEquals(1, updatedEmptyCluster.getPoints().size());
        assertEquals(updatedEmptyCluster.getCenter().getX(), pointToAdd.getX(), 0.001);
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0),
                new SimplePoint(1.0), new SimplePoint(3.0), new SimplePoint(1.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>(EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
        final Cluster<SimplePoint> emptyCluster = clusters.get(1);
        assertTrue(emptyCluster.getPoints().isEmpty());
        final Cluster<SimplePoint> nonEmptyCluster = clusters.get(0);
        assertFalse(nonEmptyCluster.getPoints().isEmpty());
        assertEquals(EmptyClusterStrategy.LARGEST_POINTS_NUMBER, clustering.getEmptyStrategy());
        final T pointToAdd = new SimplePoint(Double.MAX_VALUE);
        clusters.get(0).addPoint(pointToAdd);
        final Cluster<SimplePoint> updatedNonEmptyCluster = clusters.get(0);
        assertFalse(updatedNonEmptyCluster.getPoints().isEmpty());
        assertEquals(2, updatedNonEmptyCluster.getPoints().size());
        for (final SimplePoint point : updatedNonEmptyCluster.getPoints()) {
            if (point.getX() == pointToAdd.getX()) {
                assertEquals(pointToAdd.getX(), point.getX(), 0.001);
                break;
            }
        }
        final Cluster<SimplePoint> updatedEmptyCluster = clusters.get(1);
        assertTrue(updatedEmptyCluster.getPoints().isEmpty());
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0),
                new SimplePoint(1.0), new SimplePoint(3.0), new SimplePoint(1.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>(EmptyClusterStrategy.FARTHEST_POINT);
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
        final Cluster<SimplePoint> emptyCluster = clusters.get(1);
        assertTrue(emptyCluster.getPoints().isEmpty());
        final Cluster<SimplePoint> nonEmptyCluster = clusters.get(0);
        assertFalse(nonEmptyCluster.getPoints().isEmpty());
        assertEquals(EmptyClusterStrategy.FARTHEST_POINT, clustering.getEmptyStrategy());
        final T pointToAdd = new SimplePoint(Double.MAX_VALUE);
        clusters.get(0).addPoint(pointToAdd);
        final Cluster<SimplePoint> updatedNonEmptyCluster = clusters.get(0);
        assertFalse(updatedNonEmptyCluster.getPoints().isEmpty());
        assertEquals(2, updatedNonEmptyCluster.getPoints().size());
        final SimplePoint farthestPoint = updatedNonEmptyCluster.getPoints().get(0);
        for (final SimplePoint point : updatedNonEmptyCluster.getPoints()) {
            if (!point.equals(farthestPoint) && point.distanceFrom(farthestPoint) > 0.001) {
                final double distance = point.distanceFrom(farthestPoint);
                assertTrue(distance > 0.001);
                break;
            }
        }
        final Cluster<SimplePoint> updatedEmptyCluster = clusters.get(1);
        assertTrue(updatedEmptyCluster.getPoints().isEmpty());
    }

    @Test
    public void testEmptyClusterStrategyError() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0),
                new SimplePoint(1.0), new SimplePoint(3.0), new SimplePoint(1.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>(EmptyClusterStrategy.ERROR);
        try {
            final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 2, 10);
            fail("An ConvergenceException should have been thrown.");
        } catch (final ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testGetNearestClusterMethod() {
        final List<SimplePoint> points = Arrays.asList(new SimplePoint(1.0), new SimplePoint(2.0),
                new SimplePoint(3.0), new SimplePoint(4.0), new SimplePoint(5.0));
        final KMeansPlusPlusClusterer<SimplePoint> clustering = new KMeansPlusPlusClusterer<>();
        final List<Cluster<SimplePoint>> clusters = clustering.cluster(points, 3, 10);
        final T testPoint = new SimplePoint(3.5);
        final Cluster<SimplePoint> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, testPoint);
        assertNotNull(nearestCluster);
        assertTrue(nearestCluster.getPoints().size() > 0);
        for (final Cluster<SimplePoint> cluster : clusters) {
            if (cluster.getCenter().distanceFrom(testPoint) < nearestCluster.getCenter().distanceFrom(testPoint)) {
                nearestCluster.setCenter(cluster.getCenter());
            }
        }
        final Cluster<SimplePoint> expectedCluster = clusters.get(0);
        assertEquals(expectedCluster.getCenter().getX(), nearestCluster.getCenter().getX(), 0.001);
        final List<SimplePoint> nearestClusterPoints = nearestCluster.getPoints();
        assertTrue(nearestClusterPoints.size() > 0);
        for (final SimplePoint point : nearestClusterPoints) {
            final double distance = point.distanceFrom(nearestCluster.getCenter());
            assertTrue(distance > 0.0);
        }
    }

}

class SimplePoint implements Clusterable<SimplePoint> {

    private double x;

    public SimplePoint(final double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    @Override
    public double distanceFrom(final Clusterable<?> other) {
        if (other instanceof SimplePoint) {
            return Math.abs(this.x - ((SimplePoint) other).x);
        }
        throw new IllegalArgumentException("Invalid Clusterable object passed.");
    }

    @Override
    public Cluster<SimplePoint> getCenter() {
        return new Cluster<SimplePoint>(new SimplePoint(this.x));
    }

}