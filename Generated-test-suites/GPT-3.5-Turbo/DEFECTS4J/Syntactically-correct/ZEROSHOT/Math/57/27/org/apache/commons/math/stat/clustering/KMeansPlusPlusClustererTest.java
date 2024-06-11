import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private List<TestPoint> points;

    @Before
    public void setUp() {
        points = new ArrayList<TestPoint>();
        points.add(new TestPoint(1, 1));
        points.add(new TestPoint(2, 2));
        points.add(new TestPoint(3, 3));
        points.add(new TestPoint(10, 10));
        points.add(new TestPoint(11, 11));
        points.add(new TestPoint(12, 12));
        points.add(new TestPoint(20, 20));
        points.add(new TestPoint(21, 21));
        points.add(new TestPoint(22, 22));
    }

    @Test
    public void testCluster() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());

        Cluster<TestPoint> cluster1 = clusters.get(0);
        assertEquals(3, cluster1.getPoints().size());

        Cluster<TestPoint> cluster2 = clusters.get(1);
        assertEquals(3, cluster2.getPoints().size());

        Cluster<TestPoint> cluster3 = clusters.get(2);
        assertEquals(3, cluster3.getPoints().size());
    }

    @Test(expected=ConvergenceException.class)
    public void testClusterEmptyPoints() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        List<Cluster<TestPoint>> clusters = clusterer.cluster(new ArrayList<TestPoint>(), 3, 10);
    }

    @Test
    public void testClusterZeroK() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 0, 10);

        assertEquals(0, clusters.size());
    }

    @Test
    public void testClusterNegativeK() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random());
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, -1, 10);

        assertEquals(0, clusters.size());
    }

    @Test(expected=ConvergenceException.class)
    public void testClusterEmptyClusterStrategyError() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(new Random(),
                KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(TestPoint other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx*dx + dy*dy);
        }

        public TestPoint centroidOf(Collection<TestPoint> points) {
            double cx = 0;
            double cy = 0;
            for (TestPoint point : points) {
                cx += point.getX();
                cy += point.getY();
            }
            int numPoints = points.size();
            return new TestPoint(cx/numPoints, cy/numPoints);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}