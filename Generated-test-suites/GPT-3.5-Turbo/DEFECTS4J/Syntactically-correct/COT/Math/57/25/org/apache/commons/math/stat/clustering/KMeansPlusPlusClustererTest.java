import org.junit.Test;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        List<Cluster<Clusterable>> clusters = new ArrayList<Cluster<Clusterable>>();
        Random random = new Random();
        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer(random);

        //Test for empty point collection
        List<Clusterable> points = new ArrayList<Clusterable>();
        int k = 5;
        int maxIterations = 100;
        clusters = clusterer.cluster(points, k, maxIterations);
        assert clusters.isEmpty() : "Cluster should be empty for no input points";

        //Test for clustering with max iterations
        points.add(new ClusterableImpl(1, 1));
        points.add(new ClusterableImpl(2, 2));
        points.add(new ClusterableImpl(3, 3));
        points.add(new ClusterableImpl(4, 4));
        points.add(new ClusterableImpl(5, 5));
        clusters = clusterer.cluster(points, k, maxIterations);
        assert clusters.size() == k : "Number of clusters should be equal to k";

        //Test for exceptional handling during clustering when all clusters are empty
        clusterer = new KMeansPlusPlusClusterer(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        points.clear();
        points.add(new ClusterableImpl(1, 1));
        points.add(new ClusterableImpl(2, 2));
        points.add(new ClusterableImpl(3, 3));
        points.add(new ClusterableImpl(4, 4));
        points.add(new ClusterableImpl(5, 5));
        try {
            clusters = clusterer.cluster(points, k, maxIterations);
        } catch (ConvergenceException e) {
            assert e.getMessage().equals("Empty cluster") : "Exception message should be 'Empty cluster'";
        }

        //Test for selecting cluster with largest variance
        clusterer = new KMeansPlusPlusClusterer(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        points.clear();
        points.add(new ClusterableImpl(1, 1));
        points.add(new ClusterableImpl(2, 2));
        points.add(new ClusterableImpl(3, 3));
        points.add(new ClusterableImpl(4, 4));
        points.add(new ClusterableImpl(5, 5));
        clusters = clusterer.cluster(points, k, maxIterations);
        assert clusters.size() == k : "Number of clusters should be equal to k";
        assert !clusters.isEmpty() : "Clusters should not be empty";

        //Test for selecting cluster with largest number of points
        clusterer = new KMeansPlusPlusClusterer(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        points.clear();
        points.add(new ClusterableImpl(1, 1));
        points.add(new ClusterableImpl(2, 2));
        points.add(new ClusterableImpl(3, 3));
        points.add(new ClusterableImpl(4, 4));
        points.add(new ClusterableImpl(5, 5));
        clusters = clusterer.cluster(points, k, maxIterations);
        assert clusters.size() == k : "Number of clusters should be equal to k";
        assert !clusters.isEmpty() : "Clusters should not be empty";

        //Test for selecting cluster with farthest point
        clusterer = new KMeansPlusPlusClusterer(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        points.clear();
        points.add(new ClusterableImpl(1, 1));
        points.add(new ClusterableImpl(2, 2));
        points.add(new ClusterableImpl(3, 3));
        points.add(new ClusterableImpl(4, 4));
        points.add(new ClusterableImpl(5, 5));
        clusters = clusterer.cluster(points, k, maxIterations);
        assert clusters.size() == k : "Number of clusters should be equal to k";
        assert !clusters.isEmpty() : "Clusters should not be empty";

    }
}