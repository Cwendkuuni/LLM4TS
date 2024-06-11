import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.MemoryBasedClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testClusterEmptyCollection() {
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        List<Cluster<Point2D>> result = clustering.cluster(points, 3, -1);
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterEmptyClusterDuringAlgorithm() {
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        List<Cluster<Point2D>> result = clustering.cluster(points, 2, -1);
        Assert.assertEquals(1, result.size());
        result.get(0).getPoints().clear();
        result = clustering.cluster(points, 2, -1);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testSimpleTwoClusterCase() {
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> result = clustering.cluster(points, 2, 100);
        List<Cluster<Point2D>> expected = new ArrayList<>();
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points.subList(0, 2), 1)[0]);
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points.subList(2, 4), 1)[0]);
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getCenter().getDimension(), result.get(i).getCenter().getDimension());
            Assert.assertEquals(Collections.frequency(expected.get(i).getPoints(), result.get(i).getCenter()), result.get(i).getPoints().size());
            Assert.assertEquals(expected.get(i).getPoints(), result.get(i).getPoints());
        }
    }

    @Test
    public void testThreeClusterCase() {
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        List<Cluster<Point2D>> result = clustering.cluster(points, 3, 100);
        List<Cluster<Point2D>> expected = new ArrayList<>();
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points.subList(0, 2), 1)[0]);
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points.subList(2, 4), 1)[0]);
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points.subList(4, 6), 1)[0]);
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getCenter().getDimension(), result.get(i).getCenter().getDimension());
            Assert.assertEquals(Collections.frequency(expected.get(i).getPoints(), result.get(i).getCenter()), result.get(i).getPoints().size());
            Assert.assertEquals(expected.get(i).getPoints(), result.get(i).getPoints());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMaxIterationsReached() {
        KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> result = clustering.cluster(points, 2, 1);
        List<Cluster<Point2D>> expected = new ArrayList<>();
        expected.add(new MemoryBasedClusterer<Point2D>().cluster(points, 2)[0]);
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getCenter().getDimension(), result.get(i).getCenter().getDimension());
            Assert.assertEquals(Collections.frequency(expected.get(i).getPoints(), result.get(i).getCenter()), result.get(i).getPoints().size());
            Assert.assertEquals(expected.get(i).getPoints(), result.get(i).getPoints());
        }
    }

}