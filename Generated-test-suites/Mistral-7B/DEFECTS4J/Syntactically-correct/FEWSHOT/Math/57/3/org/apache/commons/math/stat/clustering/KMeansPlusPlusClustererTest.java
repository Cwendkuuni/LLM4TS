package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        List<Double> data = new ArrayList<>();
        data.add(1.0);
        data.add(1.0);
        data.add(2.0);
        data.add(2.0);
        KMeansPlusPlusClusterer<Clusterable<Double>> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<Double>> clusters = kmeans.cluster(data, 2, -1);
        assertEquals(1, clusters.size());
        assertTrue(clusters.get(0).getPoints().isEmpty());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        List<Double> data = new ArrayList<>();
        data.add(1.0);
        data.add(1.0);
        data.add(2.0);
        data.add(2.0);
        KMeansPlusPlusClusterer<Clusterable<Double>> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<Double>> clusters = kmeans.cluster(data, 2, 1);
    }

    @Test
    public void testLargestVarianceStrategy() {
        Random random = new Random();
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new NormalDistribution(0, 1).inverseCumulativeProbability(random.nextDouble()));
        }
        KMeansPlusPlusClusterer<Clusterable<Double>> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Double>> clusters = kmeans.cluster(data, 3, -1);
        for (Cluster<Double> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        double largestVariance = Double.MIN_VALUE;
        Cluster<Double> largestVarianceCluster = null;
        for (Cluster<Double> cluster : clusters) {
            Variance variance = new Variance();
            for (Double point : cluster.getPoints()) {
                variance.increment(point.doubleValue() - cluster.getCenter().getCenter().doubleValue());
            }
            double clusterVariance = variance.getResult();
            if (clusterVariance > largestVariance) {
                largestVariance = clusterVariance;
                largestVarianceCluster = cluster;
            }
        }
        assertEquals(largestVarianceCluster.getCenter().getCenter().doubleValue(),
                getNearestCluster(data, largestVarianceCluster.getCenter()).getCenter().getCenter().doubleValue(), 0.001);
    }

    @Test
    public void testLargestNumberStrategy() {
        Random random = new Random();
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new NormalDistribution(i % 2 == 0 ? 0 : 1, 1).inverseCumulativeProbability(random.nextDouble()));
        }
        KMeansPlusPlusClusterer<Clusterable<Double>> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Double>> clusters = kmeans.cluster(data, 3, -1);
        for (Cluster<Double> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        int largestNumber = Integer.MIN_VALUE;
        Cluster<Double> largestNumberCluster = null;
        for (Cluster<Double> cluster : clusters) {
            if (cluster.getPoints().size() > largestNumber) {
                largestNumber = cluster.getPoints().size();
                largestNumberCluster = cluster;
            }
        }
        assertEquals(largestNumberCluster.getCenter().getCenter().doubleValue(),
                getNearestCluster(data, largestNumberCluster.getCenter()).getCenter().getCenter().doubleValue(), 0.001);
    }

    @Test
    public void testFarthestPointStrategy() {
        Random random = new Random();
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new NormalDistribution(0, i % 2 == 0 ? 0.1 : 1).inverseCumulativeProbability(random.nextDouble()));
        }
        KMeansPlusPlusClusterer<Clusterable<Double>> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Double>> clusters = kmeans.cluster(data, 3, -1);
        for (Cluster<Double> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        double maxDistance = Double.MIN_VALUE;
        Cluster<Double> maxDistanceCluster = null;
        for (Cluster<Double> cluster : clusters) {
            Double farthestPoint = null;
            double farthestDistance = Double.MIN_VALUE;
            for (Double point : cluster.getPoints()) {
                if (farthestPoint == null || point.doubleValue()
                        .distanceFrom(cluster.getCenter()) > farthestDistance) {
                    farthestPoint = point;
                    farthestDistance = point.distanceFrom(cluster.getCenter());
                }
            }
            if (farthestDistance > maxDistance) {
                maxDistance = farthestDistance;
                maxDistanceCluster = cluster;
            }
        }
        assertEquals(maxDistanceCluster.getCenter().getCenter().doubleValue(),
                getNearestCluster(data, maxDistanceCluster.getCenter()).getCenter().getCenter().doubleValue(), 0.001);
    }

    private Double getNearestCluster(List<Double> data, Cluster<Double> center) {
        double minDistance = Double.MAX_VALUE;
        Cluster<Double> nearestCluster = null;
        for (Double point : data) {
            double distance = point.distanceFrom(center);
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = getNearestCluster(Collections.singletonList(center), point);
            }
        }
        return nearestCluster.getCenter();
    }

    private <T extends Clusterable<T>> Cluster<T> getNearestCluster(Collection<Cluster<T>> clusters, T point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<T> nearestCluster = null;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }
}