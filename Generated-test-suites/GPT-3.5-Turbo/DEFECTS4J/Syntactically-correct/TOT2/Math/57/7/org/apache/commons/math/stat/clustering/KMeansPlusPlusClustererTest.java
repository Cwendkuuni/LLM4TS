import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.math.stat.clustering.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(random);

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));

        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testAssignPointsToClusters() {
        // Test with some points and clusters
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 3));
        
        List<Cluster<Point>> clusters = new ArrayList<Cluster<Point>>();
        clusters.add(new Cluster<Point>(new Point(0, 0)));
        clusters.add(new Cluster<Point>(new Point(4, 4)));

        KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);

        assertEquals(2, clusters.get(0).getPoints().size());
        assertEquals(1, clusters.get(1).getPoints().size());
    }

    // Add more test cases for other methods as needed

}