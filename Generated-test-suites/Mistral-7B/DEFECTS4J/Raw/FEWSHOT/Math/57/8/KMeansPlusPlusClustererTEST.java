package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        Random random = new Random(12345);
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new ActivationPoint(i * 1.0, new Random(i).nextGaussian()));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points, 3, 100);

        // Check that all clusters have at least one point
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }

        // Check that the strategy was used when a cluster became empty
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster.isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);

        Cluster<Clusterable<Double>> splitCluster = null;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster.getPoints().size() > 1) {
                splitCluster = cluster;
                break;
            }
        }
        assertNotNull(splitCluster);

        Clusterable<Double> pointToSplit = splitCluster.getPoints().get(random.nextInt(splitCluster.getPoints().size()));
        List<Cluster<Clusterable<Double>>> newClusters = clusterer.splitCluster(splitCluster, pointToSplit);
        Cluster<Clusterable<Double>> newEmptyCluster = null;

        for (Cluster<Clusterable<Double>> newCluster : newClusters) {
            if (newCluster.isEmpty()) {
                newEmptyCluster = newCluster;
                break;
            }
        }

        // Check that the largest variance cluster was used as the source of the new empty cluster
        Cluster<Clusterable<Double>> largestVarianceCluster = null;
        double maxVariance = Double.NEGATIVE_INFINITY;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster != splitCluster && !cluster.isEmpty()) {
                Variance variance = new Variance();
                for (Clusterable<Double> point : cluster.getPoints()) {
                    variance.increment(point.distanceFrom(cluster.getCenter()));
                }
                double varianceValue = variance.getResult();
                if (varianceValue > maxVariance) {
                    largestVarianceCluster = cluster;
                    maxVariance = varianceValue;
                }
            }
        }

        assertEquals(largestVarianceCluster.getCenter().getPoint(), newEmptyCluster.getCenter().getPoint());
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        Random random = new Random(12345);
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new ActivationPoint(i * 1.0, new Random(i).nextGaussian()));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points, 3, 100);

        // Check that all clusters have at least one point
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }

        // Check that the strategy was used when a cluster became empty
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster.isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);

        Cluster<Clusterable<Double>> largestPointsNumberCluster = null;
        int maxNumber = 0;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster != emptyCluster && !cluster.isEmpty()) {
                if (cluster.getPoints().size() > maxNumber) {
                    largestPointsNumberCluster = cluster;
                    maxNumber = cluster.getPoints().size();
                }
            }
        }

        // Check that the largest points number cluster was used as the source of the new empty cluster
        assertEquals(largestPointsNumberCluster.getCenter().getPoint(), emptyCluster.getCenter().getPoint());
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        Random random = new Random(12345);
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new ActivationPoint(i * 1.0, new Random(i).nextGaussian()));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points, 3, 100);

        // Check that all clusters have at least one point
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }

        // Check that the strategy was used when a cluster became empty
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (cluster.isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);

        Cluster<Clusterable<Double>> farthestPointCluster = null;
        double maxDistance = Double.NEGATIVE_INFINITY;
        for (Cluster<Clusterable<Double>> cluster : clusters) {
            if (!cluster.isEmpty()) {
                double distance = Collections.max(cluster.getPoints().stream().map(point -> point.distanceFrom(cluster.getCenter())).mapToDouble(d -> d).sum());
                if (distance > maxDistance) {
                    farthestPointCluster = cluster;
                    maxDistance = distance;
                }
            }
        }

        // Check that the farthest point cluster was used as the source of the new empty cluster
        Clusterable<Double> farthestPoint = farthestPointCluster.getPoints().get(random.nextInt(farthestPointCluster.getPoints().size()));
        List<Cluster<Clusterable<Double>>> newClusters = clusterer.splitCluster(farthestPointCluster, farthestPoint);
        Cluster<Clusterable<Double>> newEmptyCluster = null;

        for (Cluster<Clusterable<Double>> newCluster : newClusters) {
            if (newCluster.isEmpty()) {
                newEmptyCluster = newCluster;
                break;
            }
        }

        // Check that the farthest point from the cluster center was used as the new empty cluster center
        assertEquals(farthestPoint, newEmptyCluster.getCenter().getPoint());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Random random = new Random(12345);
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new ActivationPoint(i * 1.0, new Random(i).nextGaussian()));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points, 3, 100);
    }

}

class ActivationPoint implements Clusterable<ActivationPoint> {
    private final double x;
    private final double y;

    public ActivationPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceFrom(Clusterable<ActivationPoint> other) {
        ActivationPoint otherPoint = (ActivationPoint) other;
        return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
    }

    public double getPoint() {
        return x;
    }

    public Cluster<ActivationPoint> getCenter() {
        throw new UnsupportedOperationException();
    }
}

class Cluster<T extends Clusterable<T>> {
    private final List<T> points;
    private final T center;

    public Cluster(T center) {
        this.points = new ArrayList<>();
        this.center = center;
    }

    public void addPoint(T point) {
        points.add(point);
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public T getCenter() {
        return center;
    }

    public List<T> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public List<Cluster<T>> splitCluster(T pointToSplit, T newCenter) {
        List<Cluster<T>> newClusters = new ArrayList<>();
        newClusters.add(new Cluster<T>(newCenter));
        newClusters.add(new Cluster<T>(this.getCenter()));
        this.points.remove(pointToSplit);
        newClusters.get(0).addAll(this.points);
        return newClusters;
    }
}