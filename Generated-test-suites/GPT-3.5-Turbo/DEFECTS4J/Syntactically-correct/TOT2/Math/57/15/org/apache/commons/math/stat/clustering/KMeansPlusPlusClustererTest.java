import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);
        
        assertNotNull(clusters);
        assertEquals(2, clusters.size());
        
        for (Cluster<Point> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    // Add more test cases for other methods and edge cases

}