import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final double[] data;
    private final int k;

    public KMeansPlusPlusClustererTest(double[] data, int k) {
        this.data = data;
        this.k = k;
    }

    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                        {new double[]{1.0, 2.0, 3.0, 1.0, 2.0, 3.0}, 2},
                        {new double[]{1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 4.0, 5.0}, 3},
                        {new double[]{1.0, 2.0, 2.0, 1.0, 2.0, 3.0, 4.0, 5.0}, 4},
                        {new double[]{1.0, 2.0, 2.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, 5}
                });
    }

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> result = clusterer.cluster(getPoints(data), k, 10);

        assertNotNull(result);
        assertEquals(k, result.size());

        for (Cluster<Point> cluster : result) {
            assertEquals(k, cluster.getPoints().size());
            assertEquals(Precision.equals(cluster.getCenter().getX(), data[cluster.getIndexOfCentroid()]), true);
            assertEquals(Precision.equals(cluster.getCenter().getY(), data[cluster.getIndexOfCentroid()]), true);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClustersError() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<Cluster<Point>> result = clusterer.cluster(Arrays.asList(new Point(0.0, 0.0)), 1, 1);
    }

    @Test
    public void testEmptyClustersLargestPointsNumber() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Point> points = getPoints(new double[]{1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 0.0, 0.0});
        List<Cluster<Point>> result = clusterer.cluster(points, 2, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Precision.equals(result.get(0).getCenter().getX(), 1.0), true);
        assertEquals(Precision.equals(result.get(0).getCenter().getY(), 2.0), true);
        assertEquals(2, result.get(0).getPoints().size());
        assertEquals(Precision.equals(result.get(1).getCenter().getX(), 0.0), true);
        assertEquals(Precision.equals(result.get(1).getCenter().getY(), 0.0), true);
        assertEquals(6, result.get(1).getPoints().size());
    }

    @Test
    public void testEmptyClustersLargestVariance() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point> points = getPoints(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0});
        List<Cluster<Point>> result = clusterer.cluster(points, 2, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Precision.equals(result.get(0).getCenter().getX(), 1.0), true);
        assertEquals(Precision.equals(result.get(0).getCenter().getY(), 1.0), true);
        assertEquals(7, result.get(0).getPoints().size());
        assertEquals(Precision.equals(result.get(1).getCenter().getX(), 0.0), true);
        assertEquals(Precision.equals(result.get(1).getCenter().getY(), 0.0), true);
        assertEquals(1, result.get(1).getPoints().size());
    }

    private static List<Point> getPoints(double[] data) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            points.add(new Point(data[i], data[i]));
        }
        return points;
    }

    private static class Point implements Clusterable<Point> {

        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(Clusterable<?> other) {
            Point otherPoint = (Point) other;
            return Math.sqrt(Math.pow(this.x - otherPoint.x, 2.0) + Math.pow(this.y - otherPoint.y, 2.0));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}