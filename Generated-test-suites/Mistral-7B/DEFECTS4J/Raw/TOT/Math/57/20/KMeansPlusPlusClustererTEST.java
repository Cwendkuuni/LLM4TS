package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test error strategy
        KMeansPlusPlusClusterer<Vector2D> kmeans = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Vector2D>> result = kmeans.cluster(Collections.singletonList(new Vector2D(0.0, 0.0)), 2, 1);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(0, result.get(0).getPoints().size());
        try {
            kmeans.cluster(Collections.singletonList(new Vector2D(0.0, 0.0)), 2, 1);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test largest variance strategy
        kmeans = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        result = kmeans.cluster(new ArrayList<>(List.of(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0))), 2, 10);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(1).getPoints().size());
        Cluster<Vector2D> largestCluster = result.get(0);
        Cluster<Vector2D> emptyCluster = result.get(1);
        Assert.assertFalse(largestCluster.getPoints().isEmpty());
        Assert.assertEquals(3, largestCluster.getPoints().size());
        Assert.assertEquals(3.0, largestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(3.0, largestCluster.getCenter().getY(), 0.001);
        Assert.assertEquals(0.33333, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(0))).getResult(), 0.001);
        Assert.assertEquals(0.66667, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(1))).getResult(), 0.001);
        Assert.assertEquals(1.0, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(2))).getResult(), 0.001);
        Assert.assertEquals(0.0, new Variance().increment(emptyCluster.getCenter().distanceFrom(emptyCluster.getPoints().get(0))).getResult(), 0.001);

        // Test largest number strategy
        kmeans = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        result = kmeans.cluster(new ArrayList<>(List.of(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0))), 2, 10);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(1).getPoints().size());
        largestCluster = result.get(0);
        emptyCluster = result.get(1);
        Assert.assertEquals(2, largestCluster.getPoints().size());
        Assert.assertEquals(2.0, largestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(2.0, largestCluster.getCenter().getY(), 0.001);
        Assert.assertEquals(1.5, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(0))).getResult(), 0.001);
        Assert.assertEquals(1.5, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(1))).getResult(), 0.001);
        Assert.assertEquals(0.0, new Variance().increment(emptyCluster.getCenter().distanceFrom(emptyCluster.getPoints().get(0))).getResult(), 0.001);

        // Test farthest point strategy
        kmeans = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        result = kmeans.cluster(new ArrayList<>(List.of(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0))), 2, 10);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(1).getPoints().size());
        largestCluster = result.get(0);
        emptyCluster = result.get(1);
        Assert.assertEquals(2, largestCluster.getPoints().size());
        Assert.assertEquals(3.0, largestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(3.0, largestCluster.getCenter().getY(), 0.001);
        Assert.assertEquals(3.5, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(0))).getResult(), 0.001);
        Assert.assertEquals(3.5, new Variance().increment(largestCluster.getCenter().distanceFrom(largestCluster.getPoints().get(1))).getResult(), 0.001);
        Assert.assertEquals(0.0, new Variance().increment(emptyCluster.getCenter().distanceFrom(emptyCluster.getPoints().get(0))).getResult(), 0.001);
    }

    @Test
    public void testInitializeClusters() {
        Random random = new Random(1234L);
        KMeansPlusPlusClusterer<Vector2D> kmeans = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Vector2D>> result = kmeans.cluster(new ArrayList<>(List.of(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0))), 2, 10);
        Assert.assertEquals(2, result.size());
        for (Cluster<Vector2D> cluster : result) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
        Vector2D[] expectedCentroids = {new Vector2D(1.5, 1.5), new Vector2D(3.5, 3.5)};
        for (int i = 0; i < result.size(); i++) {
            Cluster<Vector2D> cluster = result.get(i);
            Assert.assertEquals(expectedCentroids[i].getX(), cluster.getCenter().getX(), 0.001);
            Assert.assertEquals(expectedCentroids[i].getY(), cluster.getCenter().getY(), 0.001);
        }
    }

    @Test
    public void testClusterAssignment() {
        Random random = new Random(1234L);
        KMeansPlusPlusClusterer<Vector2D> kmeans = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Vector2D>> result = kmeans.cluster(new ArrayList<>(List.of(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0))), 2, 10);
        List<Vector2D> points = new ArrayList<>(List.of(new Vector2D(1.5, 1.5), new Vector2D(1.8, 1.8), new Vector2D(2.5, 2.5), new Vector2D(2.7, 2.7), new Vector2D(3.2, 3.2), new Vector2D(3.5, 3.5), new Vector2D(3.8, 3.8), new Vector2D(4.0, 4.0)));
        for (Vector2D point : points) {
            Cluster<Vector2D> cluster = getNearestCluster(result, point);
            Assert.assertEquals(point.distanceFrom(cluster.getCenter()), cluster.getPoints().get(0).distanceFrom(cluster.getCenter()), 0.001);
        }
    }

    private Cluster<Vector2D> getNearestCluster(List<Cluster<Vector2D>> clusters, Vector2D point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<Vector2D> minCluster = null;
        for (Cluster<Vector2D> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }
}