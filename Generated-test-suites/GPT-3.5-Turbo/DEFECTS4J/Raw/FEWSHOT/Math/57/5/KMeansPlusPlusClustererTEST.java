package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<ClusterablePoint> clusterer;
    
    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<ClusterablePoint>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
    }
    
    @Test
    public void testCluster() {
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(getTestData(), 4, 100);
        assertNotNull(clusters);
        assertEquals(4, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyPoints() {
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(Collections.emptyList(), 4, 100);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusters() {
        List<Cluster<ClusterablePoint>> clusters = clusterer.cluster(Collections.singletonList(new ClusterablePoint(1, 1)), 4, 100);
    }
    
    private List<ClusterablePoint> getTestData() {
        List<ClusterablePoint> points = new ArrayList<>();
        points.add(new ClusterablePoint(1, 1));
        points.add(new ClusterablePoint(2, 2));
        points.add(new ClusterablePoint(10, 10));
        points.add(new ClusterablePoint(11, 11));
        points.add(new ClusterablePoint(100, 100));
        points.add(new ClusterablePoint(101, 101));
        points.add(new ClusterablePoint(1000, 1000));
        points.add(new ClusterablePoint(1001, 1001));
        return points;
    }
}