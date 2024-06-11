import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest {

    private int k;
    private int maxIterations;
    private EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int k, int maxIterations, EmptyClusterStrategy emptyStrategy) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameterized.Parameters(name = "k={0}, maxIterations={1}, emptyStrategy={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, 10, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE},
                {5, 5, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                {7, -1, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT},
                {10, 10, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR},
        });
    }

    @Test
    public void testCluster() {
        // Given
        Collection<Clusterable<Double>> points = Arrays.asList(
                new Point3D(1.0, 2.0, 3.0),
                new Point3D(2.0, 3.0, 1.0),
                new Point3D(3.0, 1.0, 2.0),
                new Point3D(4.0, 1.0, 1.0),
                new Point3D(5.0, 5.0, 5.0),
                new Point3D(6.0, 6.0, 6.0),
                new Point3D(7.0, 7.0, 7.0)
        );

        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), this.emptyStrategy);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, this.k, this.maxIterations);

        // When / Then
        assertEquals("Number of clusters", this.k, result.size());

        for (Cluster<Clusterable<Double>> cluster : result) {
            assertFalse("Cluster cannot be empty", cluster.isEmpty());
            assertEquals("Number of points", cluster.size(), points.size() / this.k);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        // Given
        Collection<Clusterable<Double>> points = Arrays.asList(
                new Point3D(1.0, 2.0, 3.0),
                new Point3D(2.0, 3.0, 1.0),
                new Point3D(3.0, 1.0, 2.0),
                new Point3D(4.0, 1.0, 1.0) // empty cluster
        );

        KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(new Random(), this.emptyStrategy);
        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, this.k, this.maxIterations);

        // Then an error should be thrown
    }

    @Test
    public void testGetNearestCluster() {
        // Given
        Collection<Cluster<Clusterable<Double>>> clusters = Arrays.asList(
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(1.0, 1.0, 1.0)));
                    add(new Cluster<Clusterable<Double>>(new Point3D(3.0, 3.0, 3.0)));
                }},
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(0.5, 0.5, 0.5)));
                    add(new Cluster<Clusterable<Double>>(new Point3D(2.0, 2.0, 2.0)));
                }},
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(1.0, 1.0, 1.0)));
                    add(new Cluster<Clusterable<Double>>(new Point3D(2.0, 2.0, 2.0)));
                    add(new Cluster<Clusterable<Double>>(new Point3D(3.0, 3.0, 3.0)));
                }}
        );

        Cluster<Clusterable<Double>> point = new Clusterable<Double>() {
            @Override
            public double distanceFrom(Cluster<Clusterable<Double>> c) {
                return Math.sqrt(Math.pow(c.getCenter().getX() - 2.0, 2.0) + Math.pow(c.getCenter().getY() - 2.0, 2.0) + Math.pow(c.getCenter().getZ() - 2.0, 2.0));
            }

            @Override
            public double getX() {
                return 2.0;
            }

            @Override
            public double getY() {
                return 2.0;
            }

            @Override
            public double getZ() {
                return 2.0;
            }
        };

        // When
        Cluster<Clusterable<Double>> nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, point);

        // Then
        assertNotNull("Nearest cluster should not be null", nearestCluster);
        assertEquals("Nearest cluster index", clusters.indexOf(nearestCluster), 1);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        // Given
        List<Cluster<Clusterable<Double>>> clusters = Arrays.asList(
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(1.0, 1.0, 1.0), Arrays.asList(
                            new Point3D(1.0, 1.0, 1.0),
                            new Point3D(1.1, 1.0, 1.1),
                            new Point3D(1.2, 1.2, 1.2)
                    )));
                    add(new Cluster<Clusterable<Double>>(new Point3D(2.0, 2.0, 2.0), Arrays.asList(
                            new Point3D(2.1, 2.1, 2.1),
                            new Point3D(2.2, 2.2, 2.2),
                            new Point3D(2.3, 2.3, 2.3),
                            new Point3D(2.4, 2.4, 2.4)
                    )));
                }},
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(3.0, 3.0, 3.0), Arrays.asList(
                            new Point3D(3.1, 3.1, 3.1),
                            new Point3D(3.2, 3.2, 3.2),
                            new Point3D(3.3, 3.3, 3.3),
                            new Point3D(3.4, 3.4, 3.4),
                            new Point3D(3.5, 3.5, 3.5)
                    )));
                }}
        );

        // When
        Cluster<Clusterable<Double>> largestVarianceCluster = clusters.get(0);
        Clusterable<Double> point = largestVarianceCluster.getPoints().get(largestVarianceCluster.getPoints().size() - 1);
        Cluster<Clusterable<Double>> newCluster = new Cluster<Clusterable<Double>>(new Point3D(4.0, 4.0, 4.0));

        // Then
        Clusterable<Double> result = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);
        assertEquals("Result should be equal to point from largest variance cluster", point, result);

        // When
        largestVarianceCluster = newCluster;
        result = KMeansPlusPlusClusterer.getPointFromLargestVarianceCluster(clusters);

        // Then an error should be thrown
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        // Given
        List<Cluster<Clusterable<Double>>> clusters = Arrays.asList(
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(1.0, 1.0, 1.0), Arrays.asList(
                            new Point3D(1.0, 1.0, 1.0),
                            new Point3D(1.1, 1.0, 1.1),
                            new Point3D(1.2, 1.2, 1.2)
                    )));
                    add(new Cluster<Clusterable<Double>>(new Point3D(2.0, 2.0, 2.0), Arrays.asList(
                            new Point3D(2.1, 2.1, 2.1),
                            new Point3D(2.2, 2.2, 2.2),
                            new Point3D(2.3, 2.3, 2.3),
                            new Point3D(2.4, 2.4, 2.4),
                            new Point3D(2.5, 2.5, 2.5)
                    )));
                }},
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(3.0, 3.0, 3.0), Arrays.asList(
                            new Point3D(3.1, 3.1, 3.1),
                            new Point3D(3.2, 3.2, 3.2),
                            new Point3D(3.3, 3.3, 3.3),
                            new Point3D(3.4, 3.4, 3.4),
                            new Point3D(3.5, 3.5, 3.5),
                            new Point3D(3.6, 3.6, 3.6)
                    )));
                }}
        );

        // When
        Cluster<Clusterable<Double>> largestNumberCluster = clusters.get(1);
        Clusterable<Double> point = largestNumberCluster.getPoints().get(0);
        Cluster<Clusterable<Double>> newCluster = new Cluster<Clusterable<Double>>(new Point3D(4.0, 4.0, 4.0));

        // Then
        Clusterable<Double> result = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);
        assertEquals("Result should be equal to point from largest number cluster", point, result);

        // When
        largestNumberCluster = newCluster;
        result = KMeansPlusPlusClusterer.getPointFromLargestNumberCluster(clusters);

        // Then an error should be thrown
    }

    @Test
    public void testGetFarthestPoint() {
        // Given
        List<Cluster<Clusterable<Double>>> clusters = Arrays.asList(
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(1.0, 1.0, 1.0), Arrays.asList(
                            new Point3D(1.0, 1.0, 1.0),
                            new Point3D(1.1, 1.0, 1.1),
                            new Point3D(1.2, 1.2, 1.2)
                    )));
                    add(new Cluster<Clusterable<Double>>(new Point3D(2.0, 2.0, 2.0), Arrays.asList(
                            new Point3D(2.1, 2.1, 2.1),
                            new Point3D(2.2, 2.2, 2.2),
                            new Point3D(2.3, 2.3, 2.3),
                            new Point3D(2.4, 2.4, 2.4),
                            new Point3D(2.5, 2.5, 2.5)
                    )));
                }},
                new ArrayList<Cluster<Clusterable<Double>>>() {{
                    add(new Cluster<Clusterable<Double>>(new Point3D(3.0, 3.0, 3.0), Arrays.asList(
                            new Point3D(3.1, 3.1, 3.1),
                            new Point3D(3.2, 3.2, 3.2),
                            new Point3D(3.3, 3.3, 3.3),
                            new Point3D(3.4, 3.4, 3.4),
                            new Point3D(3.5, 3.5, 3.5),
                            new Point3D(3.6, 3.6, 3.6),
                            new Point3D(3.7, 3.7, 3.7)
                    )));
                }}
        );

        // When
        Cluster<Clusterable<Double>> farthestPointCluster = clusters.get(0);
        Clusterable<Double> point = farthestPointCluster.getPoints().get(0);
        Cluster<Clusterable<Double>> newCluster = new Cluster<Clusterable<Double>>(new Point3D(4.0, 4.0, 4.0));

        // Then
        Clusterable<Double> result = KMeansPlusPlusClusterer.getFarthestPoint(clusters);
        assertEquals("Result should be equal to point farthest to its cluster center", point, result);

        // When
        farthestPointCluster = newCluster;
        result = KMeansPlusPlusClusterer.getFarthestPoint(clusters);

        // Then an error should be thrown
    }

}

class Point3D implements Clusterable<Point3D> {
    private double x;
    private double y;
    private double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double distanceFrom(Cluster<Clusterable<Point3D>> c) {
        Point3D center = c.getCenter();
        double dx = x - center.getX();
        double dy = y - center.getY();
        double dz = z - center.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }
}