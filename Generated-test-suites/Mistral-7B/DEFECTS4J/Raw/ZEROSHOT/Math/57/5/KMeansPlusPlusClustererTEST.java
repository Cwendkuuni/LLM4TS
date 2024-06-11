import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Collection<Point2D> points = Arrays.asList(
                new Point2D(1.0, 1.0), new Point2D(1.1, 1.1), new Point2D(1.2, 1.2),
                new Point2D(2.0, 2.0), new Point2D(2.1, 2.1), new Point2D(2.2, 2.2),
                new Point2D(3.0, 3.0), new Point2D(3.1, 3.1), new Point2D(3.2, 3.2)
        );

        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 10);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertTrue(cluster.getPoints().size() > 0);
            System.out.println(cluster.toString());
        }
    }

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        Collection<Point2D> points = new ArrayList<>();
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertTrue(cluster.isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        Clusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        Collection<Point2D> points = Arrays.asList(
                new Point2D(1.0, 1.0), new Point2D(1.1, 1.1), new Point2D(1.2, 1.2),
                new Point2D(2.0, 2.0), new Point2D(2.1, 2.1), new Point2D(2.2, 2.2),
                new Point2D(3.0, 3.0), new Point2D(3.1, 3.1), new Point2D(3.2, 3.2),
                new Point2D(4.0, 4.0) // empty cluster
        );

        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 10);
    }

    @Test
    public void testGetNearestCluster() {
        Collection<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<Point2D>(new Point2D(0.0, 0.0)));
        clusters.add(new Cluster<Point2D>(new Point2D(1.0, 1.0)));
        clusters.add(new Cluster<Point2D>(new Point2D(2.0, 2.0)));

        Point2D point = new Point2D(1.5, 1.5);
        Cluster<Point2D> nearestCluster = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        Assert.assertEquals(point.distanceFrom(nearestCluster.getCenter()), 0.0, 0.1);
    }

}