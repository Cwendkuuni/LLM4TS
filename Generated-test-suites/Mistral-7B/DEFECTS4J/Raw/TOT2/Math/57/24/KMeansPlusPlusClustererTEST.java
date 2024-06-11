import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy_LargestVariance() {
        // Given
        List<Clusterable<Double>> points = Arrays.asList(
                new Clusterable<Double>() {
                    @Override
                    public Double getX() {
                        return 0.0;
                    }

                    @Override
                    public Double getY() {
                        return 0.0;
                    }

                    @Override
                    public double distanceFrom(Clusterable<Double> other) {
                        return 0.0;
                    }
                },
                new Clusterable<Double>() {
                    @Override
                    public Double getX() {
                        return 1.0;
                    }

                    @Override
                    public Double getY() {
                        return 1.0;
                    }

                    @Override
                    public double distanceFrom(Clusterable<Double> other) {
                        return 2.0;
                    }
                },
                new Clusterable<Double>() {
                    @Override
                    public Double getX() {
                        return 2.0;
                    }

                    @Override
                    public Double getY() {
                        return 2.0;
                    }

                    @Override
                    public double distanceFrom(Clusterable<Double> other) {
                        return 1.0;
                    }
                },
                new Clusterable<Double>() {
                    @Override
                    public Double getX() {
                        return 3.0;
                    }

                    @Override
                    public Double getY() {
                        return 3.0;
                    }

                    @Override
                    public double distanceFrom(Clusterable<Double> other) {
                        return Double.MAX_VALUE;
                    }
                }
        );

        KMeansPlusPlusClusterer<Clusterable<Double>> kMeans = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);

        // When
        List<Cluster<Clusterable<Double>>> result = kMeans.cluster(points, 2, 10);

        // Then
        assertEquals(2, result.size());

        Cluster<Clusterable<Double>> firstCluster = result.get(0);
        assertNotNull(firstCluster);
        assertEquals(1, firstCluster.getPoints().size());
        assertEquals(points.get(0), firstCluster.getPoints().get(0));

        Cluster<Clusterable<Double>> secondCluster = result.get(1);
        assertNotNull(secondCluster);
        assertEquals(3, secondCluster.getPoints().size());
        List<Clusterable<Double>> secondClusterPoints = secondCluster.getPoints();
        assertEquals(points.get(1), secondClusterPoints.get(0));
        assertEquals(points.get(2), secondClusterPoints.get(1));
        Clusterable<Double> lastPoint = secondClusterPoints.get(2);
        assertEquals(points.get(3), lastPoint);

        // Edge Cases and Error Scenarios
        // Test if the method throws ConvergenceException when no non-empty cluster is found
        List<Clusterable<Double>> emptyPoints = Collections.emptyList();
        assertThrows(ConvergenceException.class, () -> kMeans.cluster(emptyPoints, 2, 10));

        // Test if the method throws ConvergenceException when the number of points is less than the number of clusters
        List<Clusterable<Double>> tooFewPoints = Arrays.asList(points.get(0));
        assertThrows(ConvergenceException.class, () -> kMeans.cluster(tooFewPoints, 2, 10));

        // Test if the method throws ConvergenceException when the number of iterations is negative
        assertThrows(ConvergenceException.class, () -> kMeans.cluster(points, 2, -1));
    }

    // Add more test methods for other empty cluster strategies and other edge cases

    private void assertThrows(Class<? extends Exception> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (expectedExceptionClass.isInstance(e)) {
                return;
            }
            throw new AssertionError("Expected exception of type " + expectedExceptionClass.getName() +
                    ", but got: " + e.getClass().getName());
        }
        throw new AssertionError("Expected exception: " + expectedExceptionClass.getName());
    }

}