import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterWithPositiveKAndMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(3, clusters.size());
    }
    
    @Test
    public void testClusterWithNegativeKAndMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, -1, 10);
        Assert.assertEquals(0, clusters.size());
    }
    
    @Test
    public void testClusterWithPositiveKAndNegativeMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 3, -1);
        Assert.assertEquals(3, clusters.size());
    }
    
    @Test
    public void testClusterWithZeroKAndMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 0, 10);
        Assert.assertEquals(0, clusters.size());
    }
    
    @Test
    public void testClusterWithPositiveKAndZeroMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 3, 0);
        Assert.assertEquals(3, clusters.size());
    }
    
    @Test
    public void testClusterWithEmptyPoints() {
        List<TestClusterable> points = new ArrayList<TestClusterable>();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(0, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPointsAndErrorStrategy() {
        List<TestClusterable> points = new ArrayList<TestClusterable>();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, 3, 10);
    }
    
    @Test
    public void testClusterWithNegativeKAndNegativeMaxIterations() {
        List<TestClusterable> points = generatePoints();
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        List<Cluster<TestClusterable>> clusters = clusterer.cluster(points, -1, -1);
        Assert.assertEquals(0, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestVarianceClusterWithNoClusters() {
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        clusterer.getPointFromLargestVarianceCluster(new ArrayList<Cluster<TestClusterable>>());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetPointFromLargestNumberClusterWithNoClusters() {
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        clusterer.getPointFromLargestNumberCluster(new ArrayList<Cluster<TestClusterable>>());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testGetFarthestPointWithNoClusters() {
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        clusterer.getFarthestPoint(new ArrayList<Cluster<TestClusterable>>());
    }
    
    @Test
    public void testGetNearestClusterWithNoClusters() {
        KMeansPlusPlusClusterer<TestClusterable> clusterer = new KMeansPlusPlusClusterer<TestClusterable>(new Random());
        Cluster<TestClusterable> nearestCluster = clusterer.getNearestCluster(new ArrayList<Cluster<TestClusterable>>(), new TestClusterable());
        Assert.assertNull(nearestCluster);
    }
    
    private List<TestClusterable> generatePoints() {
        List<TestClusterable> points = new ArrayList<TestClusterable>();
        points.add(new TestClusterable(1.0, 2.0));
        points.add(new TestClusterable(2.0, 3.0));
        points.add(new TestClusterable(3.0, 4.0));
        points.add(new TestClusterable(4.0, 5.0));
        points.add(new TestClusterable(5.0, 6.0));
        points.add(new TestClusterable(6.0, 7.0));
        points.add(new TestClusterable(7.0, 8.0));
        points.add(new TestClusterable(8.0, 9.0));
        points.add(new TestClusterable(9.0, 10.0));
        points.add(new TestClusterable(10.0, 11.0));
        return points;
    }
}