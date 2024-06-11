import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test the error strategy
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Point2D> points = new ArrayList<>();
        Assert.assertArrayEquals(new Point2D[]{}, clustering.cluster(points, 1, 1).toArray());

        // Test the largest variance strategy
        clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        points = createRandomPoints(10, 2);
        List<Cluster<Point2D>> clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertEquals(0, clusters.get(0).getPoints().size());
        Assert.assertEquals(2, clusters.get(1).getPoints().size());
        Assert.assertArrayEquals(new Double[]{0.0, 0.0}, clusters.get(1).getCenter().getCoordinatesRef());
        Assert.assertEquals(0.0, new Variance().increment(0.0, clusters.get(1).getCenter().distanceFrom(clusters.get(1).getPoints().get(0))), 1e-5);
        Assert.assertEquals(1.0, new Variance().increment(0.0, clusters.get(1).getCenter().distanceFrom(clusters.get(1).getPoints().get(1))), 1e-5);

        // Test the largest number strategy
        clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        points = createRandomPoints(10, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertEquals(1, clusters.get(0).getPoints().size());
        Assert.assertEquals(9, clusters.get(1).getPoints().size());

        // Test the farthest strategy
        clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        points = createRandomPoints(10, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertEquals(0, clusters.get(0).getPoints().size());
        Point2D farthest = clusters.get(1).getPoints().get(0);
        for (Point2D point : clusters.get(1).getPoints()) {
            if (point.distanceFrom(farthest) > clusters.get(1).getPoints().get(0).distanceFrom(farthest)) {
                farthest = point;
            }
        }
        Assert.assertEquals(farthest, clusters.get(1).getCenter());
    }

    @Test
    public void testConvergence() {
        // Test convergence with 2 clusters and 4 points
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Point2D> points = createRandomPoints(4, 2);
        List<Cluster<Point2D>> clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);

        // Test convergence with 2 clusters and 8 points
        points = createRandomPoints(8, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);

        // Test convergence with 2 clusters and 32 points
        points = createRandomPoints(32, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);

        // Test convergence with 3 clusters and 5 points
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);

        // Test convergence with 3 clusters and 10 points
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);

        // Test convergence with 3 clusters and 32 points
        points = createRandomPoints(32, 2);
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);

        // Test non-convergence with 2 clusters and 4 points
        clusters = clustering.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);

        // Test non-convergence with 2 clusters and 8 points
        points = createRandomPoints(8, 2);
        clusters = clustering.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);

        // Test non-convergence with 2 clusters and 32 points
        points = createRandomPoints(32, 2);
        clusters = clustering.cluster(points, 2, -1);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
    }

    @Test
    public void testRandomInitialization() {
        // Test random initialization with 2 clusters and 4 points
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random);
        List<Point2D> points = createRandomPoints(4, 2);
        List<Cluster<Point2D>> clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());

        // Test random initialization with 2 clusters and 8 points
        points = createRandomPoints(8, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());

        // Test random initialization with 2 clusters and 32 points
        points = createRandomPoints(32, 2);
        clusters = clustering.cluster(points, 2, 100);
        Assert.assertEquals(2, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());

        // Test random initialization with 3 clusters and 5 points
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(2).getCenter());
        Assert.assertNotSame(clusters.get(1).getCenter(), clusters.get(2).getCenter());

        // Test random initialization with 3 clusters and 10 points
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(2).getCenter());
        Assert.assertNotSame(clusters.get(1).getCenter(), clusters.get(2).getCenter());

        // Test random initialization with 3 clusters and 32 points
        points = createRandomPoints(32, 2);
        clusters = clustering.cluster(points, 3, 100);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
        Assert.assertTrue(clusters.get(2).getPoints().size() > 0);
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(1).getCenter());
        Assert.assertNotSame(clusters.get(0).getCenter(), clusters.get(2).getCenter());
        Assert.assertNotSame(clusters.get(1).getCenter(), clusters.get(2).getCenter());
    }

    private List<Point2D> createRandomPoints(int n, int dim) {
        List<Point2D> points = new ArrayList<>();
        NormalDistribution distribution = new NormalDistribution(0, 1);
        for (int i = 0; i < n; i++) {
            double[] coordinates = new double[dim];
            for (int j = 0; j < dim; j++) {
                try {
                    coordinates[j] = distribution.inverseCumulativeProbability(random.nextDouble());
                } catch (OutOfRangeException | NumberIsTooSmallException e) {
                    points.add(new Point2D(Collections.nCopies(dim, 0.0)));
                    break;
                }
            }
            points.add(new Point2D(coordinates));
        }
        return points;
    }

}