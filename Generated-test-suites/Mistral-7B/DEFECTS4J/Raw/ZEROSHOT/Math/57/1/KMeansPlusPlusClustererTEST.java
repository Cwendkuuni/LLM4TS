import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test with default settings
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(2.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 2, 1);
        Assert.assertEquals(2, clusters.size());

        // Empty strategy: Largest variance
        KMeansPlusPlusClusterer<Point2D> kmeansLargestVariance = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point2D> pointsLargestVariance = new ArrayList<>();
        pointsLargestVariance.add(new Point2D(1.0, 1.0));
        pointsLargestVariance.add(new Point2D(1.0, 2.0));
        pointsLargestVariance.add(new Point2D(2.0, 1.0));
        pointsLargestVariance.add(new Point2D(2.0, 2.0));
        pointsLargestVariance.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> clustersLargestVariance = kmeansLargestVariance.cluster(pointsLargestVariance, 2, 1);
        Assert.assertEquals(2, clustersLargestVariance.size());
        Cluster<Point2D> largestVarianceCluster = clustersLargestVariance.get(0);
        Assert.assertEquals(3, largestVarianceCluster.size());
        Cluster<Point2D> otherCluster = clustersLargestVariance.get(1);
        Assert.assertEquals(2, otherCluster.size());

        // Empty strategy: Largest points number
        KMeansPlusPlusClusterer<Point2D> kmeansLargestPointsNumber = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Point2D> pointsLargestPointsNumber = new ArrayList<>();
        pointsLargestPointsNumber.add(new Point2D(1.0, 1.0));
        pointsLargestPointsNumber.add(new Point2D(1.0, 2.0));
        pointsLargestPointsNumber.add(new Point2D(2.0, 1.0));
        pointsLargestPointsNumber.add(new Point2D(2.0, 2.0));
        pointsLargestPointsNumber.add(new Point2D(3.0, 3.0));
        pointsLargestPointsNumber.add(new Point2D(3.0, 3.1));
        List<Cluster<Point2D>> clustersLargestPointsNumber = kmeansLargestPointsNumber.cluster(pointsLargestPointsNumber, 2, 1);
        Assert.assertEquals(2, clustersLargestPointsNumber.size());
        Cluster<Point2D> largestPointsNumberCluster = clustersLargestPointsNumber.get(0);
        Assert.assertEquals(3, largestPointsNumberCluster.size());
        Cluster<Point2D> otherClusterLargestPointsNumber = clustersLargestPointsNumber.get(1);
        Assert.assertEquals(2, otherClusterLargestPointsNumber.size());

        // Empty strategy: Farthest point
        KMeansPlusPlusClusterer<Point2D> kmeansFarthestPoint = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Point2D> pointsFarthestPoint = new ArrayList<>();
        pointsFarthestPoint.add(new Point2D(1.0, 1.0));
        pointsFarthestPoint.add(new Point2D(1.0, 2.0));
        pointsFarthestPoint.add(new Point2D(2.0, 1.0));
        pointsFarthestPoint.add(new Point2D(2.0, 2.0));
        List<Cluster<Point2D>> clustersFarthestPoint = kmeansFarthestPoint.cluster(pointsFarthestPoint, 2, 1);
        Assert.assertEquals(2, clustersFarthestPoint.size());
        Cluster<Point2D> farthestPointCluster = clustersFarthestPoint.get(0);
        Assert.assertEquals(3, farthestPointCluster.size());
        Cluster<Point2D> otherClusterFarthestPoint = clustersFarthestPoint.get(1);
        Assert.assertEquals(1, otherClusterFarthestPoint.size());

        // Empty strategy: Error
        try {
            KMeansPlusPlusClusterer<Point2D> kmeansError = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
            List<Point2D> pointsError = new ArrayList<>();
            pointsError.add(new Point2D(1.0, 1.0));
            pointsError.add(new Point2D(1.0, 2.0));
            pointsError.add(new Point2D(2.0, 1.0));
            pointsError.add(new Point2D(2.0, 2.0));
            List<Cluster<Point2D>> clustersError = kmeansError.cluster(pointsError, 2, 1);
            fail("Expected ConvergenceException but it didn't occur.");
        } catch (ConvergenceException e) {
            // expected exception
        }
    }

    @Test
    public void testRandomInitialization() {
        // Test random initialization
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> pointsRandom = new ArrayList<>();
        pointsRandom.add(new Point2D(1.0, 1.0));
        pointsRandom.add(new Point2D(1.0, 2.0));
        pointsRandom.add(new Point2D(2.0, 1.0));
        pointsRandom.add(new Point2D(2.0, 2.0));
        List<Cluster<Point2D>> clustersRandom = kmeans.cluster(pointsRandom, 2, 10);
        Assert.assertEquals(2, clustersRandom.size());
        Cluster<Point2D> cluster1Random = clustersRandom.get(0);
        Cluster<Point2D> cluster2Random = clustersRandom.get(1);
        Assert.assertEquals(2, cluster1Random.size());
        Assert.assertEquals(2, cluster2Random.size());

        // Test random initialization with same points
        KMeansPlusPlusClusterer<Point2D> kmeansSamePoints = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> pointsSamePoints = new ArrayList<>();
        pointsSamePoints.addAll(pointsRandom);
        List<Cluster<Point2D>> clustersSamePoints = kmeans.cluster(pointsSamePoints, 2, 100);
        Assert.assertEquals(2, clustersSamePoints.size());
        Cluster<Point2D> cluster1SamePoints = clustersSamePoints.get(0);
        Cluster<Point2D> cluster2SamePoints = clustersSamePoints.get(1);
        Assert.assertEquals(2, cluster1SamePoints.size());
        Assert.assertEquals(2, cluster2SamePoints.size());

        // Test random initialization with different number of points
        List<Point2D> pointsDifferentPoints = new ArrayList<>();
        pointsDifferentPoints.add(new Point2D(1.0, 1.0));
        pointsDifferentPoints.add(new Point2D(1.0, 2.0));
        pointsDifferentPoints.add(new Point2D(2.0, 1.0));
        pointsDifferentPoints.add(new Point2D(2.0, 2.0));
        pointsDifferentPoints.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> clustersDifferentPoints = kmeans.cluster(pointsDifferentPoints, 2, 10);
        Assert.assertEquals(2, clustersDifferentPoints.size());
        Cluster<Point2D> cluster1DifferentPoints = clustersDifferentPoints.get(0);
        Cluster<Point2D> cluster2DifferentPoints = clustersDifferentPoints.get(1);
        Assert.assertEquals(3, cluster1DifferentPoints.size());
        Assert.assertEquals(2, cluster2DifferentPoints.size());
    }

    @Test
    public void testConvergence() {
        // Test convergence
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> pointsConvergence = new ArrayList<>();
        pointsConvergence.addAll(Collections.nCopies(100, new Point2D(1.0, 1.0)));
        List<Cluster<Point2D>> clustersConvergence = kmeans.cluster(pointsConvergence, 2, 100);
        Assert.assertEquals(2, clustersConvergence.size());
        Cluster<Point2D> cluster1Convergence = clustersConvergence.get(0);
        Cluster<Point2D> cluster2Convergence = clustersConvergence.get(1);
        Assert.assertTrue(cluster1Convergence.size() > 45);
        Assert.assertTrue(cluster2Convergence.size() > 55);
    }

}