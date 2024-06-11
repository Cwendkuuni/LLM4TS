

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private static class MockClusterable implements Clusterable<MockClusterable> {

        private final double[] points;

        public MockClusterable(double[] points) {
            this.points = points;
        }

        @Override
        public double distanceFrom(MockClusterable center) {
            double sum = 0;
            for (int i = 0; i < points.length; i++) {
                sum += Math.pow(points[i] - center.points[i], 2);
            }
            return Math.sqrt(sum);
        }

        @Override
        public MockClusterable centroidOf(Collection<MockClusterable> points) {
            double[] newPoints = new double[this.points.length];
            for (MockClusterable clusterable : points) {
                for (int i = 0; i < this.points.length; i++) {
                    newPoints[i] += clusterable.points[i];
                }
            }
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] /= points.size();
            }
            return new MockClusterable(newPoints);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (int i = 0; i < points.length; i++) {
                sb.append(points[i]);
                if (i != points.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }

    @Test
    public void testCluster() {
        Random random = new Random(0);
        KMeansPlusPlusClusterer<MockClusterable> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<MockClusterable> points = new ArrayList<>();

        // Create 10 points in a 2-dimensional space
        for (int i = 0; i < 10; i++) {
            double[] point = new double[2];
            point[0] = random.nextDouble();
            point[1] = random.nextDouble();
            points.add(new MockClusterable(point));
        }

        // Cluster the points into 2 clusters
        List<Cluster<MockClusterable>> clusters = clusterer.cluster(points, 2, 100);

        // Assert that there are 2 clusters
        Assert.assertEquals(2, clusters.size());

        // Assert that each cluster contains at least 1 point
        for (Cluster<MockClusterable> cluster : clusters) {
            Assert.assertTrue(cluster.getPoints().size() > 0);
        }
    }
}

