import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy_largestVariance() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // Check that there are two non-empty clusters after running the algorithm
        Assert.assertEquals(2, clusters.size());
        for (final Cluster<Double> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }

        // Then
        // Check that the first cluster has the largest variance
        final double[] firstClusterPoints = new double[points.size()];
        int index = 0;
        for (final Double point : clusters.get(0).getPoints()) {
            firstClusterPoints[index++] = point;
        }
        final double firstClusterVariance = new Variance().apply(firstClusterPoints);

        // Check that the second cluster has the smallest variance
        final double[] secondClusterPoints = new double[points.size()];
        index = 0;
        for (final Double point : clusters.get(1).getPoints()) {
            secondClusterPoints[index++] = point;
        }
        final double secondClusterVariance = new Variance().apply(secondClusterPoints);

        // Check that the first cluster has the largest variance
        Assert.assertTrue(firstClusterVariance > secondClusterVariance);
    }

    @Test
    public void testEmptyClusterStrategy_largestPointsNumber() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // Check that there are two non-empty clusters after running the algorithm
        Assert.assertEquals(2, clusters.size());
        for (final Cluster<Double> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }

        // Then
        // Check that the first cluster has the largest number of points
        final int firstClusterSize = clusters.get(0).getPoints().size();

        // Check that the second cluster has the smallest number of points
        final int secondClusterSize = clusters.get(1).getPoints().size();

        // Check that the first cluster has the largest number of points
        Assert.assertTrue(firstClusterSize > secondClusterSize);
    }

    @Test
    public void testEmptyClusterStrategy_farthestPoint() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // Check that there are two non-empty clusters after running the algorithm
        Assert.assertEquals(2, clusters.size());
        for (final Cluster<Double> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }

        // Then
        // Check that the point farthest to its cluster center is in the selected cluster
        final double[] farthestPoint = new double[2];
        int index = 0;
        for (final Double point : points) {
            final Cluster<Double> nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, point);
            if (nearestCluster.getPoints().isEmpty()) {
                farthestPoint[index++] = point.doubleValue();
                break;
            }
        }
        final Cluster<Double> farthestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, new Pair<>(farthestPoint[0], farthestPoint[1]));
        Assert.assertEquals(farthestCluster, clusters.get(1));
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategy_error() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // The test should fail with a ConvergenceException exception
    }

    @Test
    public void testClusterSize() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // Check that the number of points in each cluster is correct
        for (final Cluster<Double> cluster : clusters) {
            final int expectedSize = (int) Math.ceil(points.size() / 2.0);
            final int actualSize = cluster.getPoints().size();
            Assert.assertEquals(expectedSize, actualSize);
        }
    }

    @Test
    public void testCentroid() {
        // Given
        final Random random = new Random();
        final List<Double> points = new ArrayList<>();
        points.add(0.0);
        points.add(0.0);
        points.add(1.0);
        points.add(1.0);
        points.add(2.0);
        points.add(2.0);
        points.add(3.0);
        points.add(3.0);
        Collection<Double> pointCollection = new ArrayList<>(points);

        final KMeansPlusPlusClusterer<Double> clusterer = new KMeansPlusPlusClusterer<>(random);
        final List<Cluster<Double>> clusters = clusterer.cluster(pointCollection, 2, 100);

        // When

        // Check that the centroid of each cluster is correct
        for (final Cluster<Double> cluster : clusters) {
            final Double[] centroid = cluster.getCenter().toArray();
            final List<Double> pointsInCluster = cluster.getPoints();
            double sumX = 0.0;
            double sumY = 0.0;
            for (final Double point : pointsInCluster) {
                sumX += point.get(0);
                sumY += point.get(1);
            }
            final double meanX = sumX / pointsInCluster.size();
            final double meanY = sumY / pointsInCluster.size();
            Assert.assertArrayEquals(new double[]{meanX, meanY}, centroid, 0.001);
        }
    }

}