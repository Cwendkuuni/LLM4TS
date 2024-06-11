import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-10;
    private Random random;
    private List<ClusterablePoint> points;

    private static class ClusterablePoint implements Clusterable<ClusterablePoint> {

        private final double[] point;

        public ClusterablePoint(double... point) {
            this.point = point;
        }

        @Override
        public double distanceFrom(ClusterablePoint other) {
            double sumSq = 0;
            for (int i = 0; i < point.length; i++) {
                double diff = point[i] - other.point[i];
                sumSq += diff * diff;
            }
            return Math.sqrt(sumSq);
        }

        @Override
        public ClusterablePoint centroidOf(Collection<ClusterablePoint> points) {
            double[] centroid = new double[point.length];
            int count = 0;
            for (ClusterablePoint p : points) {
                for (int i = 0; i < point.length; i++) {
                    centroid[i] += p.point[i];
                }
                count++;
            }
            if (count > 0) {
                for (int i = 0; i < point.length; i++) {
                    centroid[i] /= count;
                }
                return new ClusterablePoint(centroid);
            } else {
                return new ClusterablePoint(point.clone());
            }
        }

    }

    @Before
    public void setUp() {
        random = new Random(12345L);
        points = new ArrayList<>();
        points.add(new ClusterablePoint(1.0, 1.0));
        points.add(new ClusterablePoint(2.0, 2.0));
        points.add(new ClusterablePoint(5.0, 5.0));
        points.add(new ClusterablePoint(-1.0, -1.0));
        points.add(new ClusterablePoint(-2.0, -2.0));
        points.add(new ClusterablePoint(-5.0, -5.0));
    }

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithMaxIterations() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 3);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithNegativeMaxIterations() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, -1);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testClusterWithEmptyPoints() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(new ArrayList<>(), 2, 10);
        assertTrue(clusters.isEmpty());
    }

    @Test
    public void testClusterWithOnePoint() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint> singlePoint = new ArrayList<>();
        singlePoint.add(new ClusterablePoint(1.0, 1.0));
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(singlePoint, 2, 10);
        assertEquals(1, clusters.size());
    }

    @Test
    public void testClusterWithKGreaterThanPoints() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 10, 10);
        assertEquals(points.size(), clusters.size());
    }

    @Test
    public void testClusterCenterIsCentroid() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 10);
        for (Cluster<ClusterablePoint> cluster : clusters) {
            ClusterablePoint centroid = cluster.getPoints().get(0).centroidOf(cluster.getPoints());
            assertTrue(centroid.distanceFrom(cluster.getCenter()) < EPSILON);
        }
    }

    @Test
    public void testClusterWithInitialEmptyCentroid() {
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random,
                KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
    }

}