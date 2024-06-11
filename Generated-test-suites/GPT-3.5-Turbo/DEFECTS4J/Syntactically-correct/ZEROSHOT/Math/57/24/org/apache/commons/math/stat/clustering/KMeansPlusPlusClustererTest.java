import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer clusterer;
    private Random random;
    private Collection<Clusterable> points;
    private Clusterable point1;
    private Clusterable point2;
    private Clusterable point3;
    private Clusterable point4;
    private Clusterable point5;

    @Before
    public void setUp() {
        random = mock(Random.class);
        clusterer = new KMeansPlusPlusClusterer(random);

        point1 = mock(Clusterable.class);
        point2 = mock(Clusterable.class);
        point3 = mock(Clusterable.class);
        point4 = mock(Clusterable.class);
        point5 = mock(Clusterable.class);

        points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
    }

    @Test
    public void testChooseInitialCenters() {
        List<Cluster> initialCenters = clusterer.chooseInitialCenters(points, 3, random);

        Assert.assertEquals(3, initialCenters.size());
        verify(random, times(2)).nextInt(5);
    }

    @Test
    public void testGetNearestCluster() {
        Clusterable point = mock(Clusterable.class);
        Clusterable center1 = mock(Clusterable.class);
        Clusterable center2 = mock(Clusterable.class);
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(center1);
        Cluster cluster2 = new Cluster(center2);
        clusters.add(cluster1);
        clusters.add(cluster2);

        cluster1.addPoint(point);

        Cluster nearestCluster = clusterer.getNearestCluster(clusters, point);

        Assert.assertEquals(cluster1, nearestCluster);
        Assert.assertEquals(1, cluster1.getPoints().size());
    }

    @Test
    public void testAssignPointsToClusters() {
        Clusterable point = mock(Clusterable.class);
        Clusterable center1 = mock(Clusterable.class);
        Clusterable center2 = mock(Clusterable.class);
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = new Cluster(center1);
        Cluster cluster2 = new Cluster(center2);
        clusters.add(cluster1);
        clusters.add(cluster2);

        Collection<Clusterable> points = new ArrayList<>();
        points.add(point);

        clusterer.assignPointsToClusters(clusters, points);

        Assert.assertEquals(1, cluster1.getPoints().size());
        Assert.assertEquals(point, cluster1.getPoints().get(0));
    }

    @Test
    public void testCluster() {
        Clusterable center1 = mock(Clusterable.class);
        Clusterable center2 = mock(Clusterable.class);
        Clusterable center3 = mock(Clusterable.class);
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster1 = spy(new Cluster(center1));
        Cluster cluster2 = spy(new Cluster(center2));
        Cluster cluster3 = spy(new Cluster(center3));
        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.add(cluster3);

        clusterer.cluster(points, 3, 100);

        verify(cluster1, times(1)).addPoint(point1);
        verify(cluster1, times(2)).addPoint(point2);
        verify(cluster1, times(1)).addPoint(point4);
        verify(cluster1, times(1)).addPoint(point5);
        verify(cluster2, times(1)).addPoint(point4);
        verify(cluster3, times(1)).addPoint(point3);
    }

}