import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test class for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with typical usage scenario.
     */
    @Test
    public void testClusterTypicalUsage() {
        // given
        List<Clusterable<Point2D>> dataPoints = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        for (int i = 0; i < 15; i++) {
            dataPoints.add(new Point2D(Math.random(), Math.random()));
        }

        // when
        List<Cluster<Point2D>> clusters = clusterer.cluster(dataPoints, 3, 10);

        // then
        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with empty collection scenario.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterEmptyCollection() {
        // given
        List<Clusterable<Point2D>> dataPoints = Collections.emptyList();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        // when
        List<Cluster<Point2D>> clusters = clusterer.cluster(dataPoints, 3, 10);

        // then
        // An IllegalArgumentException should be thrown because the input data is empty
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with negative maxIterations scenario.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterNegativeMaxIterations() {
        // given
        List<Clusterable<Point2D>> dataPoints = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        for (int i = 0; i < 15; i++) {
            dataPoints.add(new Point2D(Math.random(), Math.random()));
        }

        // when
        List<Cluster<Point2D>> clusters;
        try {
            clusters = clusterer.cluster(dataPoints, 3, -1);
        } catch (IllegalArgumentException e) {
            // expected
            return;
        }

        // then
        // An IllegalArgumentException should be thrown because the maxIterations is negative
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with k larger than the number of data points scenario.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterKLargerThanNumberOfDataPoints() {
        // given
        List<Clusterable<Point2D>> dataPoints = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        for (int i = 0; i < 10; i++) {
            dataPoints.add(new Point2D(Math.random(), Math.random()));
        }

        // when
        List<Cluster<Point2D>> clusters;
        try {
            clusters = clusterer.cluster(dataPoints, 15, 10);
        } catch (IllegalArgumentException e) {
            // expected
            return;
        }

        // then
        // An IllegalArgumentException should be thrown because k is larger than the number of data points
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with maxIterations reached scenario.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testClusterMaxIterationsReached() {
        // given
        List<Clusterable<Point2D>> dataPoints = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        for (int i = 0; i < 20; i++) {
            dataPoints.add(new Point2D(Math.random(), Math.random()));
        }

        // when
        List<Cluster<Point2D>> clusters = clusterer.cluster(dataPoints, 3, 1);

        // then
        // A MaxIterationsExceededException should be thrown because the algorithm didn't converge
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}
     * with empty clusters scenario.
     */
    @Test(expected = NoSolutionException.class)
    public void testClusterEmptyClusters() {
        // given
        List<Clusterable<Point2D>> dataPoints = new ArrayList<>();
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        for (int i = 0; i < 10; i++) {
            dataPoints.add(new Point2D(Math.random(), Math.random()));
        }

        // when
        List<Cluster<Point2D>> clusters = clusterer.cluster(dataPoints, 4, 10);

        // then
        // A NoSolutionException should be thrown because the number of clusters is larger than the number of data points
    }

}