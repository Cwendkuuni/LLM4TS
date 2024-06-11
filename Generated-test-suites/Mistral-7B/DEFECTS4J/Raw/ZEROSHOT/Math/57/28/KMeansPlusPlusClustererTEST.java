package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link KMeansPlusPlusClusterer}.
 */
public class KMeansPlusPlusClustererTest {

    /** The random generator. */
    private static final Random RANDOM = new Random();

    /** The strategy for handling empty clusters. */
    private static final EmptyClusterStrategy EMPTY_STRATEGY = EmptyClusterStrategy.LARGEST_VARIANCE;

    /** The clusterer. */
    private KMeansPlusPlusClusterer<MyPoint> clustering;

    /** The data points. */
    private List<MyPoint> data;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, EMPTY_STRATEGY);
        data = new ArrayList<MyPoint>();
        for (int i = 0; i < 10; i++) {
            data.add(new MyPoint(i));
        }
    }

    /**
     * Tests the clustering algorithm with 3 clusters.
     */
    @Test
    public void testClustering3Clusters() {
        List<Cluster<MyPoint>> clusters = clustering.cluster(data, 3, 100);
        assertEquals(3, clusters.size());
        for (Cluster<MyPoint> cluster : clusters) {
            assertEquals(3, cluster.size());
        }
    }

    /**
     * Tests the clustering algorithm with 4 clusters.
     */
    @Test(expected = ConvergenceException.class)
    public void testClustering4Clusters() {
        List<Cluster<MyPoint>> clusters = clustering.cluster(data, 4, 100);
        assertEquals(4, clusters.size());
        for (Cluster<MyPoint> cluster : clusters) {
            assertEquals(3, cluster.size());
        }

        // The test should fail as there is no way to form 4 clusters
        // with the 10 given data points
    }

    /**
     * Tests the clustering algorithm with a negative number of iterations.
     */
    @Test
    public void testClusteringNegativeIterations() {
        List<Cluster<MyPoint>> clusters = clustering.cluster(data, 3, -1);
        assertEquals(3, clusters.size());
        for (Cluster<MyPoint> cluster : clusters) {
            assertEquals(3, cluster.size());
        }
    }

    /**
     * Tests the {@link #chooseInitialCenters(Collection, int, Random)} method.
     */
    @Test
    public void testChooseInitialCenters() {
        List<Cluster<MyPoint>> initialCenters = clustering.chooseInitialCenters(data, 3, RANDOM);
        assertEquals(3, initialCenters.size());
        for (Cluster<MyPoint> cluster : initialCenters) {
            assertNotNull(cluster.getCenter());
            assertEquals(1, cluster.size());
        }
    }

    /**
     * Tests the {@link #assignPointsToClusters(Collection, Collection)} method.
     */
    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<MyPoint>> clusters = new ArrayList<Cluster<MyPoint>>(3);
        for (int i = 0; i < 3; i++) {
            clusters.add(new Cluster<MyPoint>(new MyPoint(i * 3)));
        }
        assignPointsToClusters(clusters, data);
        for (Cluster<MyPoint> cluster : clusters) {
            assertEquals(3, cluster.size());
        }
    }

    /**
     * Tests the {@link #getNearestCluster(Collection, Clusterable)} method.
     */
    @Test
    public void testGetNearestCluster() {
        List<Cluster<MyPoint>> clusters = new ArrayList<Cluster<MyPoint>>(3);
        for (int i = 0; i < 3; i++) {
            clusters.add(new Cluster<MyPoint>(new MyPoint(i * 3)));
        }
        Collections.shuffle(clusters);

        MyPoint point = new MyPoint(5);
        Cluster<MyPoint> nearestCluster = getNearestCluster(clusters, point);
        assertNotNull(nearestCluster);
        assertEquals(Collections.min(clusters.stream().map(Cluster::getCenter).map(MyPoint::getX)),
                     nearestCluster.getCenter().getX(), 0.001);
    }

}

/**
 * A {@link Clusterable} point with an x coordinate.
 */
class MyPoint implements Clusterable<MyPoint> {

    /** The x coordinate. */
    private final double x;

    /**
     * Creates a new {@link MyPoint}.
     *
     * @param x the x coordinate
     */
    public MyPoint(double x) {
        this.x = x;
    }

    /**
     * Returns the x coordinate.
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the distance from this point to the given point.
     *
     * @param other the point
     * @return the distance between this point and the given point
     */
    public double distanceFrom(MyPoint other) {
        return Math.abs(x - other.x);
    }

    @Override
    public String toString() {
        return "MyPoint{" + x + '}';
    }

}