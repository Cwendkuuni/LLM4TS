import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // create some mock data
        Collection<Clusterable<Integer>> mockPoints = generateMockPoints();

        // create the clusterer object
        KMeansPlusPlusClusterer<Clusterable<Integer>> clusterer = new KMeansPlusPlusClusterer<>(new Random());

        // cluster the mock data
        List<Cluster<Clusterable<Integer>>> clusters = clusterer.cluster(mockPoints, 3, -1);

        // assert the number of clusters is accurate
        assertEquals(3, clusters.size());

        // assert the number of points in each cluster is accurate
        for (Cluster<Clusterable<Integer>> cluster : clusters) {
            assertEquals(10, cluster.getPoints().size());
        }
    }

    private Collection<Clusterable<Integer>> generateMockPoints() {
        List<Clusterable<Integer>> mockPoints = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mockPoints.add(new Clusterable<Integer>() {
                @Override
                public double distanceFrom(Clusterable<Integer> other) {
                    return Math.abs(this.getValue() - other.getValue());
                }

                @Override
                public Integer getValue() {
                    return i;
                }
            });
        }
        return mockPoints;
    }
}