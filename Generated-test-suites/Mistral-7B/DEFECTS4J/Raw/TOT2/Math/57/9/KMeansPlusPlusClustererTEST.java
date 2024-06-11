import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    private static final Random RANDOM = new Random();

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        // Test clustering an empty list
        List<Clusterable<Double>> points = Collections.emptyList();
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, 1, 1);
        assertTrue(result.isEmpty());

        // Test clustering a list with one point
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        result = clustering.cluster(points, 1, 1);
        assertEquals(1, result.size());
        Cluster<Clusterable<Double>> cluster = result.get(0);
        assertEquals(points.get(0), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy splitting the largest variance cluster
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        result = clustering.cluster(points, 2, 1);
        assertEquals(2, result.size());
        cluster = result.get(0);
        assertEquals(1, cluster.getPoints().size());
        assertEquals(points.get(0), cluster.getCenter());
        cluster = result.get(1);
        assertEquals(2, cluster.getPoints().size());
        assertEquals(new MyPoint(1.5), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy throwing an error
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        try {
            result = clustering.cluster(points, 2, 1);
            fail("Expected ConvergenceException not thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        // Test clustering an empty list
        List<Clusterable<Double>> points = Collections.emptyList();
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, 1, 1);
        assertTrue(result.isEmpty());

        // Test clustering a list with one point
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        result = clustering.cluster(points, 1, 1);
        assertEquals(1, result.size());
        Cluster<Clusterable<Double>> cluster = result.get(0);
        assertEquals(points.get(0), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy splitting the largest points number cluster
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        result = clustering.cluster(points, 2, 1);
        assertEquals(2, result.size());
        cluster = result.get(0);
        assertEquals(1, cluster.getPoints().size());
        assertEquals(points.get(1), cluster.getCenter());
        cluster = result.get(1);
        assertEquals(2, cluster.getPoints().size());
        assertEquals(new MyPoint(1.5), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy throwing an error
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        try {
            result = clustering.cluster(points, 2, 1);
            fail("Expected ConvergenceException not thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        // Test clustering an empty list
        List<Clusterable<Double>> points = Collections.emptyList();
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, 1, 1);
        assertTrue(result.isEmpty());

        // Test clustering a list with one point
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        result = clustering.cluster(points, 1, 1);
        assertEquals(1, result.size());
        Cluster<Clusterable<Double>> cluster = result.get(0);
        assertEquals(points.get(0), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy splitting the farthest point cluster
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        result = clustering.cluster(points, 2, 1);
        assertEquals(2, result.size());
        cluster = result.get(0);
        assertEquals(1, cluster.getPoints().size());
        assertEquals(points.get(1), cluster.getCenter());
        cluster = result.get(1);
        assertEquals(2, cluster.getPoints().size());
        assertEquals(new MyPoint(1.5), cluster.getCenter());

        // Test clustering a list with multiple points and empty strategy throwing an error
        clustering = new KMeansPlusPlusClusterer<>(RANDOM, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        try {
            result = clustering.cluster(points, 2, 1);
            fail("Expected ConvergenceException not thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testCluster() {
        List<Clusterable<Double>> points = new ArrayList<>();
        points.add(new MyPoint(0.0));
        points.add(new MyPoint(1.0));
        points.add(new MyPoint(2.0));
        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, 2, 10);
        assertEquals(2, result.size());
        Cluster<Clusterable<Double>> cluster1 = result.get(0);
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(new MyPoint(1.5), cluster1.getCenter());
        Cluster<Clusterable<Double>> cluster2 = result.get(1);
        assertEquals(2, cluster2.getPoints().size());
        assertEquals(new MyPoint(1.5), cluster2.getCenter());
    }

    private static class MyPoint implements Clusterable<MyPoint> {
        private final double value;

        public MyPoint(double value) {
            this.value = value;
        }

        @Override
        public MyPoint centroidOf(Collection<MyPoint> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            int count = 0;
            for (MyPoint p : points) {
                sumX += p.value;
                sumY += p.value * p.value;
                count++;
            }
            return new MyPoint(sumX / count, Math.sqrt(sumY / count) );
        }

        @Override
        public double distanceFrom(MyPoint other) {
            return Math.abs(this.value - other.value);
        }

        @Override
        public String toString() {
            return "MyPoint{" +
                    "value=" + value +
                    '}';
        }

        public double getValue() {
            return value;
        }
    }
}