package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double EPSILON = 1e-9;

    private final Random random;
    private final EmptyClusterStrategy emptyStrategy;
    private final int k;

    public KMeansPlusPlusClustererTest(Random random, EmptyClusterStrategy emptyStrategy, int k) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
        this.k = k;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new Random(), EmptyClusterStrategy.LARGEST_VARIANCE, 2},
                new Object[]{new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER, 3},
                new Object[]{new Random(), EmptyClusterStrategy.FARTHEST_POINT, 4}
        });
    }

    @Test
    public void testCluster() {
        Collection<T> points = createPoints();
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(random, emptyStrategy).cluster(points, k, -1);
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
        checkClusterCenters(clusters);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() {
        Collection<T> points = Arrays.asList(new Point(0, 0));
        new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR).cluster(points, k, -1);
    }

    @Test(expected = ConvergenceException.class)
    public void testMaxIterations() throws InterruptedException {
        Collection<T> points = createPoints();
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(random, emptyStrategy).cluster(points, k, 0);
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsTooMany() throws InterruptedException {
        Collection<T> points = createPoints();
        new KMeansPlusPlusClusterer<>(random, emptyStrategy).cluster(points, k, Integer.MAX_VALUE + 1);
    }

    @Test(expected = NoRealSolutionException.class)
    public void testNoSolution() {
        Collection<T> points = Arrays.asList(new Point(0, 0), new Point(0, 0));
        new KMeansPlusPlusClusterer<>(random, emptyStrategy).cluster(points, k, 1);
    }

    private Collection<T> createPoints() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            double x = Math.random();
            double y = Math.random();
            points.add(new Point(x, y));
        }
        return points;
    }

    private void checkClusterCenters(List<Cluster<T>> clusters) {
        for (Cluster<T> cluster : clusters) {
            T center = cluster.getCenter();
            List<T> points = cluster.getPoints();
            double sumX = 0;
            double sumY = 0;
            for (T point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            assertEquals(center.getX(), sumX / points.size(), EPSILSON);
            assertEquals(center.getY(), sumY / points.size(), EPSILSON);
        }
    }

    private static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<? extends Point> other) {
            Point p = (Point) other;
            return Precision.doubleDistance(x, p.x) + Precision.doubleDistance(y, p.y);
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public double[] toArray() {
            return new double[]{x, y};
        }
    }

}