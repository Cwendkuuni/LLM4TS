package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.StatisticsFacts;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Point2D;
import org.apache.commons.math.stat.clustering.impl.distance.EuclideanDistance;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private static final EuclideanDistance DISTANCE = new EuclideanDistance();

    private final int numPoints;
    private final int numClusters;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int numPoints, int numClusters, EmptyClusterStrategy emptyStrategy) {
        this.numPoints = numPoints;
        this.numClusters = numClusters;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {5, 3, EmptyClusterStrategy.LARGEST_VARIANCE},
            {10, 3, EmptyClusterStrategy.LARGEST_VARIANCE},
            {15, 4, EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
            {20, 5, EmptyClusterStrategy.FARTHEST_POINT},
            {30, 8, EmptyClusterStrategy.ERROR}
        });
    }

    @Before
    public void setUp() {
        random = new Random(12345);
        clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
    }

    private Random random;
    private KMeansPlusPlusClusterer<Point2D> clusterer;

    @Test
    public void testConstructors() {
        assertNotNull(clusterer);
    }

    @Test
    public void testClusterWithValidArguments() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point2D(i, i));
        }
        final List<Cluster<Point2D>> result = clusterer.cluster(points, numClusters, Integer.MAX_VALUE);
        assertNotNull(result);
        assertEquals(numClusters, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClusterWithInvalidNumClusters() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point2D(i, i));
        }
        clusterer.cluster(points, 0, Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClusterWithNegativeMaxIterations() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point2D(i, i));
        }
        clusterer.cluster(points, numClusters, -1);
    }

    @Test
    public void testChooseInitialCenters() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point2D(i, i));
        }
        final List<Cluster<Point2D>> result = clusterer.chooseInitialCenters(points, numClusters, random);
        assertNotNull(result);
        assertEquals(numClusters, result.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        final List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new Cluster<Point2D>(new Point2D(i, i)));
        }
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point2D(i, i));
        }
        clusterer.assignPointsToClusters(clusters, points);
        for (Cluster<Point2D> cluster : clusters) {
            assertNotNull(cluster.getPoints());
            assertEquals(Math.min(numPoints / numClusters, cluster.getPoints().size()), cluster.getPoints().size());
        }
    }

    @Test
    public void testGetNearestCluster() {
        final List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new Cluster<Point2D>(new Point2D(i, i)));
        }
        final Point2D p = new Point2D(numPoints / 2, numPoints / 2);
        final Cluster<Point2D> expected = getNearestCluster(clusters, p);
        final Cluster<Point2D> result = clusterer.getNearestCluster(clusters, p);
        assertEquals(expected, result);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        final List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            final List<Point2D> points = new ArrayList<>();
            for (int j = 0; j < numPoints / numClusters; j++) {
                points.add(new Point2D(i * numPoints / numClusters + j, (double) i / numClusters));
            }
            clusters.add(new Cluster<Point2D>(new Point2D(i, i), points));
        }
        final Cluster<Point2D> result = clusterer.getPointFromLargestVarianceCluster(clusters);
        assertNotNull(result);
        final Cluster<Point2D> expected = getClusterWithLargestVariance(clusters);
        assertEquals(expected.getCenter(), result.getCenter());
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        final List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            final List<Point2D> points = new ArrayList<>();
            for (int j = 0; j < numPoints / numClusters; j++) {
                points.add(new Point2D(i * numPoints / numClusters + j, (double) i / numClusters));
            }
            clusters.add(new Cluster<Point2D>(new Point2D(i, i), points));
        }
        final Cluster<Point2D> result = clusterer.getPointFromLargestNumberCluster(clusters);
        assertNotNull(result);
        final Cluster<Point2D> expected = getClusterWithLargestNumberOfPoints(clusters);
        assertEquals(expected.getCenter(), result.getCenter());
    }

    @Test
    public void testGetFarthestPoint() {
        final List<Cluster<Point2D>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            final List<Point2D> points = new ArrayList<>();
            for (int j = 0; j < numPoints / numClusters; j++) {
                points.add(new Point2D(i * numPoints / numClusters + j, (double) i / numClusters));
            }
            clusters.add(new Cluster<Point2D>(new Point2D(i, i), points));
        }
        final Point2D expected = getFarthestPoint(clusters);
        final Point2D result = clusterer.getFarthestPoint(clusters);
        assertEquals(expected.getX(), result.getX(), Precision.EPSILON);
        assertEquals(expected.getY(), result.getY(), Precision.EPSILON);
    }

    private Cluster<Point2D> getNearestCluster(Collection<Cluster<Point2D>> clusters, Point2D point) {
        Comparator<Cluster<Point2D>> comparator = new Comparator<Cluster<Point2D>>() {
            @Override
            public int compare(Cluster<Point2D> o1, Cluster<Point2D> o2) {
                return Double.compare(DISTANCE.distance(point, o1.getCenter()), DISTANCE.distance(point, o2.getCenter()));
            }
        };
        return clusters.stream().min(comparator).orElseThrow(IllegalStateException::new);
    }

    private Cluster<Point2D> getClusterWithLargestVariance(Collection<Cluster<Point2D>> clusters) {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<Point2D> maxCluster = null;
        for (Cluster<Point2D> cluster : clusters) {
            double variance = StatisticsFacts.variance(cluster.getPoints().stream().mapToDouble(Point2D::getX).toArray());
            if (variance > maxVariance) {
                maxVariance = variance;
                maxCluster = cluster;
            }
        }
        return maxCluster;
    }

    private Cluster<Point2D> getClusterWithLargestNumberOfPoints(Collection<Cluster<Point2D>> clusters) {
        Cluster<Point2D> maxCluster = null;
        for (Cluster<Point2D> cluster : clusters) {
            if ((maxCluster == null) || (cluster.getPoints().size() > maxCluster.getPoints().size())) {
                maxCluster = cluster;
            }
        }
        return maxCluster;
    }

    private Point2D getFarthestPoint(Collection<Cluster<Point2D>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<Point2D> maxCluster = null;
        Point2D maxPoint = null;
        for (Cluster<Point2D> cluster : clusters) {
            for (Point2D point : cluster.getPoints()) {
                double distance = DISTANCE.distance(point, cluster.getCenter());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    maxCluster = cluster;
                    maxPoint = point;
                }
            }
        }
        return maxPoint;
    }

}