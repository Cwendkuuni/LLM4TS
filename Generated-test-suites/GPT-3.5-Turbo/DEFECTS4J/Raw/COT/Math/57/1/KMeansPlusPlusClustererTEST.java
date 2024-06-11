
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<ClusterablePoint> clusterer;
    private List<ClusterablePoint> points;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<ClusterablePoint>(new Random());
        points = new ArrayList<ClusterablePoint>();
    }

    @Test
    public void testCluster() {
        // Test case 1: Empty points list
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(points, 5, 100);
        assertEquals("Should have 0 clusters", 0, clusters.size());

        // Test case 2: Single point
        points.add(new ClusterablePoint(1, 1));
        clusters = clusterer.cluster(points, 5, 100);
        assertEquals("Should have 1 cluster", 1, clusters.size());
        assertEquals("Cluster should contain 1 point", 1, clusters.get(0).getPoints().size());

        // Test case 3: Multiple points
        points.add(new ClusterablePoint(2, 2));
        points.add(new ClusterablePoint(3, 3));
        clusters = clusterer.cluster(points, 2, 100);
        assertEquals("Should have 2 clusters", 2, clusters.size());
        assertEquals("First cluster should contain 1 point", 1, clusters.get(0).getPoints().size());
        assertEquals("Second cluster should contain 2 points", 2, clusters.get(1).getPoints().size());
    }
}

class ClusterablePoint implements Clusterable<ClusterablePoint> {
    private final double x;
    private final double y;

    public ClusterablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceFrom(ClusterablePoint other) {
        double dx = x - other.getX();
        double dy = y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public ClusterablePoint centroidOf(Collection<ClusterablePoint> points) {
        double sumX = 0;
        double sumY = 0;
        int count = 0;
        for (ClusterablePoint point : points) {
            sumX += point.getX();
            sumY += point.getY();
            count++;
        }
        return new ClusterablePoint(sumX / count, sumY / count);
    }
}
