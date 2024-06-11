import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.stat.descriptive.moment.Variance;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1.0, 1.0));
        points.add(new Point(2.0, 2.0));
        points.add(new Point(3.0, 3.0));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<Point>> clusters = clusterer.cluster(points, 2, 100);

        assertNotNull(clusters);
        assertEquals(2, clusters.size());
    }

    @Test
    public void testEmptyClusterStrategy() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1.0, 1.0));

        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        try {
            clusterer.cluster(points, 2, 100);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, e.getSpecific());
        }
    }

    @Test
    public void testChooseInitialCenters() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1.0, 1.0));
        points.add(new Point(2.0, 2.0));
        points.add(new Point(3.0, 3.0));

        List<Cluster<Point>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, 2, new Random());

        assertNotNull(initialCenters);
        assertEquals(2, initialCenters.size());
    }

    // Additional test cases for other methods can be added here

}

class Point implements Clusterable<Point> {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public double distanceFrom(Point p) {
        double dx = x - p.getX();
        double dy = y - p.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public Point centroidOf(List<Point> points) {
        double sumX = 0.0;
        double sumY = 0.0;

        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }

        return new Point(sumX / points.size(), sumY / points.size());
    }
}