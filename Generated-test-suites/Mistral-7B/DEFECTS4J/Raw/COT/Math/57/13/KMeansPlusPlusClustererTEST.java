package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;

/**
 * Test class for KMeansPlusPlusClusterer
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test the constructor with default strategy
     */
    @Test
    public void testDefaultConstructor() {
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        assertNotNull(clustering);
    }

    /**
     * Test the constructor with custom strategy
     */
    @Test
    public void testCustomConstructor() {
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        assertNotNull(clustering);
    }

    /**
     * Test the cluster method with simple data
     */
    @Test
    public void testClusterSimpleData() {
        List<TestPoint> data = Arrays.asList(
                new TestPoint(1.0),
                new TestPoint(1.5),
                new TestPoint(2.0),
                new TestPoint(2.5)
        );
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<TestPoint>> result = clustering.cluster(data, 2, 10);
        List<Cluster<TestPoint>> expected = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(1.0)),
                new Cluster<TestPoint>(new TestPoint(2.0)),
                new Cluster<TestPoint>(new TestPoint(1.5), new TestPoint(2.5))
        );
        assertEquals(expected, result);
    }

    /**
     * Test the cluster method with empty data
     */
    @Test(expected = ConvergenceException.class)
    public void testClusterEmptyData() {
        List<TestPoint> data = Collections.emptyList();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        clustering.cluster(data, 2, 10);
    }

    /**
     * Test the getPointFromLargestVarianceCluster method
     */
    @Test
    public void testGetPointFromLargestVarianceCluster() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.0), Arrays.asList(new TestPoint(0.5), new TestPoint(1.0))),
                new Cluster<TestPoint>(new TestPoint(1.5), Arrays.asList(new TestPoint(2.0), new TestPoint(2.5))),
                new Cluster<TestPoint>(new TestPoint(3.0), Arrays.asList(new TestPoint(3.5), new TestPoint(4.0)))
        );
        TestPoint result = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);
        TestPoint expected = new TestPoint(3.0);
        assertEquals(expected, result);
    }

    /**
     * Test the getPointFromLargestNumberCluster method
     */
    @Test
    public void testGetPointFromLargestNumberCluster() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.0), Arrays.asList(new TestPoint(0.5), new TestPoint(1.0))),
                new Cluster<TestPoint>(new TestPoint(1.5), Arrays.asList(new TestPoint(2.0), new TestPoint(2.5), new TestPoint(3.0))),
                new Cluster<TestPoint>(new TestPoint(3.0), Arrays.asList(new TestPoint(3.5), new TestPoint(4.0)))
        );
        TestPoint result = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);
        TestPoint expected = new TestPoint(1.5);
        assertEquals(expected, result);
    }

    /**
     * Test the getFarthestPoint method
     */
    @Test
    public void testGetFarthestPoint() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.0), Arrays.asList(new TestPoint(0.5), new TestPoint(1.0))),
                new Cluster<TestPoint>(new TestPoint(1.5), Arrays.asList(new TestPoint(2.0), new TestPoint(2.5), new TestPoint(3.0))),
                new Cluster<TestPoint>(new TestPoint(3.0), Arrays.asList(new TestPoint(3.5), new TestPoint(4.0)))
        );
        TestPoint result = KMeansPlusPlusClusterer.getFarthestPoint(clusters);
        TestPoint expected = new TestPoint(3.5);
        assertEquals(expected, result);
    }

    /**
     * Test the getNearestCluster method with simple data
     */
    @Test
    public void testGetNearestClusterSimpleData() {
        List<TestPoint> data = Arrays.asList(
                new TestPoint(1.0),
                new TestPoint(1.5),
                new TestPoint(2.0),
                new TestPoint(2.5)
        );
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.5), Arrays.asList(new TestPoint(0.5))),
                new Cluster<TestPoint>(new TestPoint(2.5), Arrays.asList(new TestPoint(2.5))),
                new Cluster<TestPoint>(new TestPoint(2.0), Arrays.asList(new TestPoint(2.0), new TestPoint(3.0)))
        );
        Cluster<TestPoint> result = KMeansPlusPlusClusterer.getNearestCluster(clusters, new TestPoint(1.5));
        Cluster<TestPoint> expected = clusters.get(1);
        assertEquals(expected, result);
    }

    /**
     * Test the getNearestCluster method with edge cases
     */
    @Test
    public void testGetNearestClusterEdgeCases() {
        List<TestPoint> data = Arrays.asList(
                new TestPoint(0.0),
                new TestPoint(Double.MAX_VALUE)
        );
        List<Cluster<TestPoint>> clusters = Arrays.asList(
                new Cluster<TestPoint>(new TestPoint(0.1), Arrays.asList(new TestPoint(0.1))),
                new Cluster<TestPoint>(new TestPoint(0.2), Arrays.asList(new TestPoint(0.2))),
                new Cluster<TestPoint>(new TestPoint(Double.MAX_VALUE - 0.1), Arrays.asList(new TestPoint(Double.MAX_VALUE - 0.1)))
        );
        Cluster<TestPoint> result = KMeansPlusPlusClusterer.getNearestCluster(clusters, new TestPoint(Double.MAX_VALUE));
        Cluster<TestPoint> expected = clusters.get(2);
        assertEquals(expected, result);

        result = KMeansPlusPlusClusterer.getNearestCluster(clusters, new TestPoint(0.0));
        expected = clusters.get(0);
        assertEquals(expected, result);
    }

    /**
     * Test the cluster method with large data
     */
    @Test
    public void testClusterLargeData() {
        List<TestPoint> data = new ArrayList<>();
        NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
        for (int i = 0; i < 10000; i++) {
            data.add(new TestPoint(distribution.sample()));
        }
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<TestPoint>> result = clustering.cluster(data, 50, 100);
        // since we don't have information about the expected result, we just check that the result is not empty
        assertFalse(result.isEmpty());
    }

}

class TestPoint implements Clusterable<TestPoint> {

    private double value;

    public TestPoint(double value) {
        this.value = value;
    }

    @Override
    public double distanceFrom(Clusterable<?> other) {
        if (other instanceof TestPoint) {
            TestPoint point = (TestPoint) other;
            return Math.abs(value - point.value);
        }
        throw new UnsupportedOperationException();
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