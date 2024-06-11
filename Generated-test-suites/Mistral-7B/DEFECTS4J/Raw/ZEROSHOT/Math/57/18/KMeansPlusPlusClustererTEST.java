import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testKMeansPlusPlusClusterer() {
        // Test with default strategy
        Random random = new Random(123);
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);

        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 2.0));
        points.add(new TestPoint(2.0, 1.0));
        points.add(new TestPoint(3.0, 3.0));
        points.add(new TestPoint(4.0, 4.0));
        points.add(new TestPoint(5.0, 5.0));

        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 100);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            Assert.assertEquals(2, cluster.getPoints().size());
        }

        // Test with custom strategy
        KMeansPlusPlusClusterer<TestPoint> clustererError = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<TestPoint> pointsError = new ArrayList<>();
        pointsError.add(new TestPoint(1.0, 2.0));
        pointsError.add(new TestPoint(2.0, 1.0));
        pointsError.add(new TestPoint(3.0, 3.0));
        pointsError.add(new TestPoint(4.0, 4.0));
        pointsError.add(new TestPoint(5.0, 5.0));

        List<Cluster<TestPoint>> clustersError = clustererError.cluster(pointsError, 3, 100);
        Assert.assertEquals(3, clustersError.size());
        for (Cluster<TestPoint> cluster : clustersError) {
            if (cluster.getPoints().isEmpty()) {
                throw new AssertionError("Error strategy should not return empty clusters.");
            }
        }
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<TestPoint> other) {
            if (other instanceof TestPoint) {
                TestPoint otherPoint = (TestPoint) other;
                return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Cluster<TestPoint> getCenter() {
            return new Cluster<TestPoint>(new TestPoint(0.0, 0.0));
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}