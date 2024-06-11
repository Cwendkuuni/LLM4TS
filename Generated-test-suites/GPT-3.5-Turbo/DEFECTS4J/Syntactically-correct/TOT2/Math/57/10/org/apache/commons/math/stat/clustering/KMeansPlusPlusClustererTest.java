import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import java.util.ArrayList;
import java.util.List;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterBasicFunctionality() {
        // Create test data
        List<TestClusterable> points = new ArrayList<>();
        points.add(new TestClusterable(1.0, 2.0));
        points.add(new TestClusterable(2.0, 3.0));
        points.add(new TestClusterable(3.0, 4.0));

        // Create a KMeansPlusPlusClusterer instance
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // Perform clustering
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 2, 10);
        
        // Check if the number of clusters is correct
        assertEquals(2, clusters.size());
        
        // Check if the clustering resulted in non-empty clusters
        for (Cluster<TestClusterable> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    // Custom Clusterable implementation for testing
    private class TestClusterable implements Clusterable<TestClusterable> {
        private double x;
        private double y;

        public TestClusterable(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(TestClusterable other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public TestClusterable centroidOf(List<TestClusterable> points) {
            double sumX = 0;
            double sumY = 0;
            for (TestClusterable p : points) {
                sumX += p.x;
                sumY += p.y;
            }
            return new TestClusterable(sumX / points.size(), sumY / points.size());
        }
    }
}