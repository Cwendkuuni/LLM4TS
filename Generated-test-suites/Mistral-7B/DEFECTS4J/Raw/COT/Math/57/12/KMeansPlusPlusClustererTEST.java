import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterWithTwoPoints() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 1, -1);
        Assert.assertEquals(1, clusters.size());
        Cluster<Point2D> cluster = clusters.get(0);
        Assert.assertEquals(2.0, cluster.getSize(), 0.0);
        Point2D center = cluster.getCenter();
        Assert.assertEquals(1.5, center.getX(), 0.0);
        Assert.assertEquals(1.5, center.getY(), 0.0);
    }

    @Test
    public void testClusterWithThreePoints() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertTrue(cluster.getSize() > 0);
        }

        Cluster<Point2D> firstCluster = clusters.get(0);
        Point2D firstCenter = firstCluster.getCenter();
        Assert.assertEquals(1.5, firstCenter.getX(), 0.0);
        Assert.assertEquals(1.5, firstCenter.getY(), 0.0);
        Assert.assertEquals(1, firstCluster.getPoints().size());
        Point2D firstPoint = firstCluster.getPoints().get(0);
        Assert.assertEquals(1.0, firstPoint.getX(), 0.0);
        Assert.assertEquals(1.0, firstPoint.getY(), 0.0);

        Cluster<Point2D> secondCluster = clusters.get(1);
        Point2D secondCenter = secondCluster.getCenter();
        Assert.assertEquals(3.0, secondCenter.getX(), 0.0);
        Assert.assertEquals(3.0, secondCenter.getY(), 0.0);
        Assert.assertEquals(1, secondCluster.getPoints().size());
        Point2D secondPoint = secondCluster.getPoints().get(0);
        Assert.assertEquals(3.0, secondPoint.getX(), 0.0);
        Assert.assertEquals(3.0, secondPoint.getY(), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Point2D> points = Collections.emptyList();
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 1, -1);
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(0.0, 0.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());

        Cluster<Point2D> firstCluster = clusters.get(0);
        Point2D firstCenter = firstCluster.getCenter();
        Assert.assertEquals(1.0, firstCenter.getX(), 0.0);
        Assert.assertEquals(1.0, firstCenter.getY(), 0.0);
        List<Point2D> firstPoints = firstCluster.getPoints();
        Assert.assertEquals(1, firstPoints.size());
        Point2D firstPoint = firstPoints.get(0);
        Assert.assertEquals(1.0, firstPoint.getX(), 0.0);
        Assert.assertEquals(1.0, firstPoint.getY(), 0.0);

        Cluster<Point2D> secondCluster = clusters.get(1);
        Point2D secondCenter = secondCluster.getCenter();
        Assert.assertEquals(0.0, secondCenter.getX(), 0.0);
        Assert.assertEquals(0.0, secondCenter.getY(), 0.0);
        List<Point2D> secondPoints = secondCluster.getPoints();
        Assert.assertEquals(1, secondPoints.size());
        Point2D secondPoint = secondPoints.get(0);
        Assert.assertEquals(0.0, secondPoint.getX(), 0.0);
        Assert.assertEquals(0.0, secondPoint.getY(), 0.0);
    }

    @Test
    public void testEmptyClusterStrategyLargestNumber() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(0.0, 0.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());

        Cluster<Point2D> firstCluster = clusters.get(0);
        Point2D firstCenter = firstCluster.getCenter();
        Assert.assertEquals(1.0, firstCenter.getX(), 0.0);
        Assert.assertEquals(1.0, firstCenter.getY(), 0.0);
        List<Point2D> firstPoints = firstCluster.getPoints();
        Assert.assertEquals(1, firstPoints.size());
        Point2D firstPoint = firstPoints.get(0);
        Assert.assertEquals(1.0, firstPoint.getX(), 0.0);
        Assert.assertEquals(1.0, firstPoint.getY(), 0.0);

        Cluster<Point2D> secondCluster = clusters.get(1);
        Point2D secondCenter = secondCluster.getCenter();
        Assert.assertEquals(0.0, secondCenter.getX(), 0.0);
        Assert.assertEquals(0.0, secondCenter.getY(), 0.0);
        List<Point2D> secondPoints = secondCluster.getPoints();
        Assert.assertEquals(1, secondPoints.size());
        Point2D secondPoint = secondPoints.get(0);
        Assert.assertEquals(0.0, secondPoint.getX(), 0.0);
        Assert.assertEquals(0.0, secondPoint.getY(), 0.0);
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(0.0, 0.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());

        Cluster<Point2D> firstCluster = clusters.get(0);
        Point2D firstCenter = firstCluster.getCenter();
        Assert.assertEquals(1.0, firstCenter.getX(), 0.0);
        Assert.assertEquals(1.0, firstCenter.getY(), 0.0);
        List<Point2D> firstPoints = firstCluster.getPoints();
        Assert.assertEquals(1, firstPoints.size());
        Point2D firstPoint = firstPoints.get(0);
        Assert.assertEquals(1.0, firstPoint.getX(), 0.0);
        Assert.assertEquals(1.0, firstPoint.getY(), 0.0);

        Cluster<Point2D> secondCluster = clusters.get(1);
        Point2D secondCenter = secondCluster.getCenter();
        Assert.assertEquals(0.0, secondCenter.getX(), 0.0);
        Assert.assertEquals(0.0, secondCenter.getY(), 0.0);
        List<Point2D> secondPoints = secondCluster.getPoints();
        Assert.assertEquals(1, secondPoints.size());
        Point2D secondPoint = secondPoints.get(0);
        Assert.assertEquals(0.0, secondPoint.getX(), 0.0);
        Assert.assertEquals(0.0, secondPoint.getY(), 0.0);

        // 2 points have the same coordinates
        Variance variance = new Variance();
        variance.increment(firstPoint.distanceFrom(firstCenter));
        variance.increment(secondPoint.distanceFrom(secondCenter));
        double expectedVariance = variance.getResult();
        Assert.assertEquals(expectedVariance, 0.0, 0.01);
    }

}