import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Create test data
        Collection<TestClusterable> points = new ArrayList<TestClusterable>();
        points.add(new TestClusterable(1, 1));
        points.add(new TestClusterable(2, 2));
        points.add(new TestClusterable(3, 3));
        points.add(new TestClusterable(4, 4));

        // Create clusterer
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());

        // Run test
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 2, 10);

        // Verify output
        assertEquals(clusters.size(), 2);
        Cluster<TestClusterable> cluster1 = clusters.get(0);
        Cluster<TestClusterable> cluster2 = clusters.get(1);
        assertEquals(cluster1.getPoints().size(), 2);
        assertEquals(cluster2.getPoints().size(), 2);
    }

    private static class TestClusterable implements Clusterable<TestClusterable> {

        private double x;
        private double y;

        public TestClusterable(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceFrom(TestClusterable point) {
            double dx = x - point.x;
            double dy = y - point.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public TestClusterable centroidOf(Collection<TestClusterable> points) {
            double totalX = 0;
            double totalY = 0;
            for (TestClusterable point : points) {
                totalX += point.x;
                totalY += point.y;
            }
            double centerX = totalX / points.size();
            double centerY = totalY / points.size();
            return new TestClusterable(centerX, centerY);
        }
    }

}