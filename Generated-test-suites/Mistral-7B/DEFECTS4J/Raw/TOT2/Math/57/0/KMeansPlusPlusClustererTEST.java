import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy_largestVariance() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point2D> points = Arrays.asList(new Point2D(0, 0), new Point2D(1, 1), new Point2D(2, 2), new Point2D(3, 3));

        // When
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertNotSame(points.get(0), result.get(0).getCenter());
        Assert.assertNotSame(points.get(1), result.get(1).getCenter());

        // Given
        points.remove(points.size() - 1);
        result.clear();

        // When
        try {
            result = clusterer.cluster(points, 2, 10);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected
        }

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(0, result.get(0).getPoints().size());

        // Given
        KMeansPlusPlusClusterer<Point2D> clustererError = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        // When
        try {
            result = clustererError.cluster(points, 2, 10);
            Assert.fail();
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testEmptyClusterStrategy_largestPointsNumber() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Point2D> points = Arrays.asList(new Point2D(0, 0), new Point2D(1, 1), new Point2D(2, 2), new Point2D(3, 3));

        // When
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertNotSame(points.get(0), result.get(0).getCenter());
        Assert.assertNotSame(points.get(1), result.get(1).getCenter());

        // Given
        points.remove(points.size() - 1);
        result.clear();

        // When
        result = clusterer.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(2, result.get(0).getPoints().size());

        // Given
        KMeansPlusPlusClusterer<Point2D> clustererFarthest = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);

        // When
        result = clustererFarthest.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertNotSame(points.get(0), result.get(0).getCenter());
        Assert.assertNotSame(points.get(1), result.get(1).getCenter());

        // Given
        points.remove(0);
        result.clear();

        // When
        result = clustererFarthest.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.get(0).getPoints().size());
    }

    @Test
    public void testEmptyClusterStrategy_farthestPoint() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Point2D> points = Arrays.asList(new Point2D(0, 0), new Point2D(1, 1), new Point2D(2, 2), new Point2D(3, 3));

        // When
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertNotSame(points.get(0), result.get(0).getCenter());
        Assert.assertNotSame(points.get(1), result.get(1).getCenter());

        // Given
        points.remove(points.size() - 1);
        result.clear();

        // When
        result = clusterer.cluster(points, 2, 10);

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.get(0).getPoints().size());
    }

    @Test
    public void testCluster() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Point2D> points = Arrays.asList(new Point2D(0, 0), new Point2D(1, 1), new Point2D(2, 2), new Point2D(3, 3), new Point2D(4, 4), new Point2D(5, 5), new Point2D(6, 6), new Point2D(7, 7), new Point2D(8, 8), new Point2D(9, 9));

        // When
        List<Cluster<Point2D>> result = clusterer.cluster(points, 3, 100);

        // Then
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.get(0).getPoints().size() > 0);
        Assert.assertTrue(result.get(1).getPoints().size() > 0);
        Assert.assertTrue(result.get(2).getPoints().size() > 0);

        // Given
        List<Point2D> points2 = new ArrayList<>(points);
        points2.remove(points2.size() - 1);

        // When
        List<Cluster<Point2D>> result2 = clusterer.cluster(points2, 3, 100);

        // Then
        Assert.assertEquals(3, result2.size());
        Assert.assertTrue(result2.get(0).getPoints().size() > 0);
        Assert.assertTrue(result2.get(1).getPoints().size() > 0);
        Assert.assertTrue(result2.get(2).getPoints().size() > 0);

        // Given
        List<Point2D> points3 = new ArrayList<>(points);
        points3.remove(points3.size() - 2);

        // When
        List<Cluster<Point2D>> result3 = clusterer.cluster(points3, 3, 100);

        // Then
        Assert.assertEquals(3, result3.size());
        Assert.assertTrue(result3.get(0).getPoints().size() > 0);
        Assert.assertTrue(result3.get(1).getPoints().size() > 0);
        Assert.assertTrue(result3.get(2).getPoints().size() > 0);

        // Given
        List<Point2D> points4 = new ArrayList<>(points);
        points4.remove(points4.size() - 3);

        // When
        List<Cluster<Point2D>> result4 = clusterer.cluster(points4, 3, 100);

        // Then
        Assert.assertEquals(2, result4.size());
        Assert.assertTrue(result4.get(0).getPoints().size() > 0);
        Assert.assertTrue(result4.get(1).getPoints().size() > 0);
    }
}