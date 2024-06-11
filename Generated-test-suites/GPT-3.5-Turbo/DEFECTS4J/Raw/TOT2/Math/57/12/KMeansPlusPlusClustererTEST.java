import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterInitialization() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(random);

        assertNotNull(clusterer);
    }

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        assertNotNull(clusterer);
    }

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<Cluster<Clusterable>> clusters = clusterer.cluster(new ArrayList<>(), 2, 100);

        assertNotNull(clusters);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterInKMeans() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        clusterer.cluster(new ArrayList<>(), 2, 100);
    }

}