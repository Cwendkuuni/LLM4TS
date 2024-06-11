import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class KMeansPlusPlusClustererTest {

    private Random random;
    
    @Before
    public void setUp() {
        random = new Random();
    }

    @Test
    public void testKMeansPlusPlusClustererCreation() {
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random);
        assertNotNull(clusterer);
    }

    @Test
    public void testKMeansPlusPlusClustererClusterMethod() {
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random);
        List<Cluster<Clusterable>> clusters = clusterer.cluster(new ArrayList<Clusterable>(), 3, 100);
        assertNotNull(clusters);
    }

    @Test(expected = ConvergenceException.class)
    public void testKMeansPlusPlusClustererClusterMethodWithEmptyCluster() {
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<Clusterable>> clusters = clusterer.cluster(new ArrayList<Clusterable>(), 3, 100);
    }

    @Test
    public void testAssignPointsToClusters() {
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random);
        List<Cluster<Clusterable>> clusters = new ArrayList<Cluster<Clusterable>>();
        Cluster<Clusterable> cluster1 = new Cluster<Clusterable>(new Clusterable());
        clusters.add(cluster1);
        Clusterable point = new Clusterable();
        clusterer.assignPointsToClusters(clusters, point);
        assertEquals(1, cluster1.getPoints().size());
    }

    @Test
    public void testChooseInitialCenters() {
        List<Clusterable> points = new ArrayList<Clusterable>();
        for (int i = 0; i < 10; i++) {
            points.add(new Clusterable());
        }
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random);
        List<Cluster<Clusterable>> clusters = clusterer.chooseInitialCenters(points, 3, random);
        assertEquals(3, clusters.size());
    }

}