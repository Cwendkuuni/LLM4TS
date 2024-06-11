import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math.stat.distance.EuclideanDistance;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-6;
    private static final Random random = new Random();

    private final int numPoints;
    private final int k;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int numPoints, int k, EmptyClusterStrategy emptyStrategy) {
        this.numPoints = numPoints;
        this.k = k;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameterized.Parameters(name = "k={0}, emptyStrategy={1}, numPoints={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {2, EmptyClusterStrategy.LARGEST_VARIANCE, 5},
            {2, EmptyClusterStrategy.LARGEST_POINTS_NUMBER, 5},
            {2, EmptyClusterStrategy.FARTHEST_POINT, 5},
            {10, EmptyClusterStrategy.ERROR, 50},
            {10, EmptyClusterStrategy.FARTHEST_POINT, 100},
            {10, EmptyClusterStrategy.LARGEST_VARIANCE, 100},
            {10, EmptyClusterStrategy.LARGEST_POINTS_NUMBER, 100}
        });
    }

    @Test
    public void testClusterMethod() {
        Collection<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point(new NormalDistribution(0, 1).sample(), new NormalDistribution(0, 1).sample(), new NormalDistribution(0, 1).sample()));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, k, -1);

        assertEquals(k, result.size());

        for (Cluster<Clusterable<Double>> cluster : result) {
            assertFalse(cluster.getPoints().isEmpty());
            assertEquals(Precision.compareDoubles(cluster.getCenter().getCoordinate().get(0), cluster.centroidOf(cluster.getPoints()).getCoordinate().get(0), EPSILON), 0);
            assertEquals(Precision.compareDoubles(cluster.getCenter().getCoordinate().get(1), cluster.centroidOf(cluster.getPoints()).getCoordinate().get(1), EPSILON), 0);
            assertEquals(Precision.compareDoubles(cluster.getCenter().getCoordinate().get(2), cluster.centroidOf(cluster.getPoints()).getCoordinate().get(2), EPSILON), 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Collection<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new Point(i, i, i));
        }

        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        clustering.cluster(points, k, -1);
    }

    private static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;
        private final double z;
        private final EuclideanDistance distance = new EuclideanDistance();

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double[] getCoordinate() {
            return new double[]{x, y, z};
        }

        public Clusterable<Point> centroidOf(Collection<Clusterable<Point>> collection) {
            double xSum = 0, ySum = 0, zSum = 0;
            int size = 0;

            for (Clusterable<Point> point : collection) {
                xSum += point.getCoordinate()[0];
                ySum += point.getCoordinate()[1];
                zSum += point.getCoordinate()[2];
                size++;
            }

            return new Point((xSum / size), (ySum / size), (zSum / size));
        }

        public double distanceFrom(Clusterable<Point> other) {
            return distance.distance(this, other);
        }
    }

}