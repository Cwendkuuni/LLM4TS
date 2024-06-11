import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {

        // Creating an instance of KMeansPlusPlusClusterer
        Random random = new Random();
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer =
                new KMeansPlusPlusClusterer<>(random);

        // Generating dummy data points
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            double value = random.nextDouble();
            Clusterable<Double> point = mock(Clusterable.class);
            when(point.distanceFrom(any())).thenReturn(value);
            points.add(point);
        }

        // Testing typical use case
        int k = 3;
        int maxIterations = 10;
        List<Cluster<Clusterable<Double>>> clusters = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, clusters.size());

        // Testing edge cases
        Cluster<Clusterable<Double>> cluster1 = new Cluster<>(mock(Clusterable.class));
        Cluster<Clusterable<Double>> cluster2 = new Cluster<>(mock(Clusterable.class));
        Cluster<Clusterable<Double>> cluster3 = new Cluster<>(mock(Clusterable.class));
        Collection<T> emptyPoints = new ArrayList<>();
        cluster1.addPoints(emptyPoints);
        cluster2.addPoints(points);
        cluster3.addPoints(points);

        // Testing empty cluster handling for strategy LARGEST_VARIANCE
        cluster3.addPoint(mock(Clusterable.class));
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer1 =
                new KMeansPlusPlusClusterer<>(random,
                KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Clusterable<Double>>> clusters1 =
                clusterer1.cluster(clusters, k, maxIterations);
        assertNotNull(clusters1);

        // Testing empty cluster handling for strategy LARGEST_POINTS_NUMBER
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer2 =
                new KMeansPlusPlusClusterer<>(random,
                KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Clusterable<Double>>> clusters2 =
                clusterer2.cluster(clusters, k, maxIterations);
        assertNotNull(clusters2);

        // Testing empty cluster handling for strategy FARTHEST_POINT
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer3 =
                new KMeansPlusPlusClusterer<>(random,
                KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Clusterable<Double>>> clusters3 =
                clusterer3.cluster(clusters, k, maxIterations);
        assertNotNull(clusters3);

        // Testing empty cluster handling for strategy ERROR
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer4 =
                new KMeansPlusPlusClusterer<>(random,
                KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        try {
            clusterer4.cluster(clusters, k, maxIterations);
            fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            assertEquals("Empty cluster in K-means", e.getMessage());
        }

    }
}