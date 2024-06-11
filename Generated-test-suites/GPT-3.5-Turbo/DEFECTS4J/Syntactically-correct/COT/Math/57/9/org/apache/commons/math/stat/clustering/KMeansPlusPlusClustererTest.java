import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> clusterer;

    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
    }

    @After
    public void tearDown() {
        clusterer = null;
    }

    @Test
    public void testCluster_basic() {
        Collection<Point> points = generatePoints();
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 10);

        Assert.assertNotNull(clusters);
        Assert.assertEquals(3, clusters.size());

        for (Cluster<Point> cluster : clusters) {
            Assert.assertNotNull(cluster.getCenter());
            Assert.assertNotNull(cluster.getPoints());
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_emptyPoints() {
        Collection<Point> points = new ArrayList<Point>();
        clusterer.cluster(points, 3, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_emptyCluster() {
        Collection<Point> points = generatePoints();
        points.clear();
        clusterer.cluster(points, 3, 10);
    }

    @Test
    public void testCluster_maxIterations() {
        Collection<Point> points = generatePoints();
        List<Cluster<Point>> clusters = clusterer.cluster(points, 3, 0);

        Assert.assertNotNull(clusters);
        Assert.assertEquals(3, clusters.size());

        for (Cluster<Point> cluster : clusters) {
            Assert.assertNotNull(cluster.getCenter());
            Assert.assertNotNull(cluster.getPoints());
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }

    private Collection<Point> generatePoints() {
        Collection<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(10, 10));
        points.add(new Point(20, 20));
        points.add(new Point(30, 30));
        return points;
    }

}