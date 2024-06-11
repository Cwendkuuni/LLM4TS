import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.impl.SimpleCluster;
import org.apache.commons.math.stat.clustering.distance.SquaredEuclideanDistance;
import org.apache.commons.math.stat.descriptive.moment.Variance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testKMeansPlusPlusClustererEmptyInput() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<SimpleCluster> emptyList = Collections.emptyList();
        Assert.assertThrows(IllegalArgumentException.class, () -> clustering.cluster(emptyList, 1, -1));
    }

    @Test
    public void testKMeansPlusPlusClustererNegativeMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        List<SimpleCluster> points = new ArrayList<>();
        points.add(new SimpleCluster(new double[]{1.0, 1.0}));
        points.add(new SimpleCluster(new double[]{2.0, 2.0}));
        Assert.assertThrows(IllegalArgumentException.class, () -> clustering.cluster(points, 2, -1));
    }

    @Test
    public void testKMeansPlusPlusClustererOnePointCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        List<SimpleCluster> points = new ArrayList<>();
        SimpleCluster point = new SimpleCluster(new double[]{0.0, 0.0});
        points.add(point);
        List<Cluster<SimpleCluster>> clusters = clustering.cluster(points, 1, 100);
        Assert.assertEquals(1, clusters.size());
        Assert.assertEquals(point, clusters.get(0).getCenter());
        Assert.assertEquals(1, clusters.get(0).getPoints().size());
        Assert.assertEquals(point, clusters.get(0).getPoints().get(0));
    }

    @Test
    public void testKMeansPlusPlusClustererTwoPointsCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        List<SimpleCluster> points = new ArrayList<>();
        SimpleCluster point1 = new SimpleCluster(new double[]{0.0, 0.0});
        SimpleCluster point2 = new SimpleCluster(new double[]{1.0, 1.0});
        points.add(point1);
        points.add(point2);
        List<Cluster<SimpleCluster>> clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<SimpleCluster> cluster : clusters) {
            double epsilon = 0.01;
            Cluster<SimpleCluster> getCenter = cluster.getCenter();
            Assert.assertEquals(cluster.getPoints().size(), 1);
            if (cluster.getPoints().get(0).equals(point1)) {
                Assert.assertArrayEquals(new double[]{0.0, 0.0}, getCenter.getCenter().getPoints(), epsilon);
            } else {
                Assert.assertArrayEquals(new double[]{1.0, 1.0}, getCenter.getCenter().getPoints(), epsilon);
            }
        }
    }

    @Test
    public void testKMeansPlusPlusClustererManyPointsCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        List<SimpleCluster> points = new ArrayList<>();
        int numberOfPoints = 10;
        for (int i = 0; i < numberOfPoints; i++) {
            double x = Math.random() * 10;
            double y = Math.random() * 10;
            SimpleCluster point = new SimpleCluster(new double[]{x, y});
            points.add(point);
        }
        List<Cluster<SimpleCluster>> clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        for (Cluster<SimpleCluster> cluster : clusters) {
            double epsilon = 0.01;
            Cluster<SimpleCluster> getCenter = cluster.getCenter();
            List<SimpleCluster> getPoints = cluster.getPoints();
            Assert.assertEquals(getPoints.size(), 3);
            for (SimpleCluster point : getPoints) {
                double distance = new SquaredEuclideanDistance().distance(point, getCenter);
                Assert.assertTrue("Cluster point and its center should be close: " + distance, Precision.isClose(distance, 0.0, 0.1));
            }
        }
    }

    @Test
    public void testKMeansPlusPlusClustererEmptyClusterHandling() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<SimpleCluster> points = new ArrayList<>();
        SimpleCluster point1 = new SimpleCluster(new double[]{0.0, 0.0});
        SimpleCluster point2 = new SimpleCluster(new double[]{1.0, 1.0});
        SimpleCluster emptyCluster = new SimpleCluster(new double[]{0.0, 0.0});
        points.add(point1);
        points.add(point2);
        points.add(emptyCluster);
        List<Cluster<SimpleCluster>> clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Cluster<SimpleCluster> centerOfLargeVarClust = null;
        // The largest variance cluster should not be empty
        for (Cluster<SimpleCluster> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                Assert.fail("Expected non-empty cluster found");
            } else if (centerOfLargeVarClust == null ||
                       new Variance().variance(cluster.getPoints()) > new Variance().variance(points)) {
                centerOfLargeVarClust = cluster;
            }
        }
        SimpleCluster newCenter = null;
        // Get new center of the largest variance cluster
        for (SimpleCluster point : centerOfLargeVarClust.getPoints()) {
            if (newCenter == null || new SquaredEuclideanDistance().distance(point, centerOfLargeVarClust.getCenter()) > new SquaredEuclideanDistance().distance(point, newCenter)) {
                newCenter = point;
            }
        }
        // Check that new center is added to the new cluster
        List<Cluster<SimpleCluster>> newClusters = new ArrayList<>();
        for (Cluster<SimpleCluster> cluster : clusters) {
            if (cluster.getPoints().contains(emptyCluster)) {
                newClusters.add(cluster);
                continue;
            }
            List<SimpleCluster> newClusterPoints = new ArrayList<>();
            for (SimpleCluster point : cluster.getPoints()) {
                if (!point.equals(emptyCluster)) {
                    newClusterPoints.add(point);
                }
            }
            newClusters.add(new Cluster<SimpleCluster>(new SimpleCluster(newCenter))).addAll(newClusterPoints);
        }
        Assert.assertEquals(4, newClusters.size());
        Assert.assertEquals(newCenter, newClusters.get(3).getCenter().getPoints()[0]);
        Assert.assertEquals(3, newClusters.get(3).getPoints().size());
        Assert.assertFalse(newClusters.get(3).getPoints().contains(emptyCluster));
    }

    @Test
    public void testKMeansPlusPlusClustererConvergence() {
        Random random = new Random();
        KMeansPlusPlusClusterer<SimpleCluster> clustering = new KMeansPlusPlusClusterer<>(random);
        List<SimpleCluster> points = new ArrayList<>();
        int numberOfPoints = 1000;
        for (int i = 0; i < numberOfPoints; i++) {
            double x = Math.random() * 100;
            double y = Math.random() * 100;
            SimpleCluster point = new SimpleCluster(new double[]{x, y});
            points.add(point);
        }
        List<Cluster<SimpleCluster>> oldClusters = null;
        List<Cluster<SimpleCluster>> clusters = null;
        try {
            int maxIterations = 10;
            do {
                oldClusters = clusters;
                clusters = clustering.cluster(points, 10, maxIterations);
                maxIterations--;
            } while (maxIterations > 0 && oldClusters.equals(clusters));
        } catch (ConvergenceException e) {
            clusters = clustering.cluster(points, 10, 10);
        }
        Assert.assertEquals(10, clusters.size());
        // Check that no clusters are empty
        for (Cluster<SimpleCluster> cluster : clusters) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }
}