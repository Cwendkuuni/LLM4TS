import static org.junit.Assert.*;
import org.junit.Test;
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
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterableTestClass> clusterer = new KMeansPlusPlusClusterer<>(random);
        
        // Generate test data
        List<ClusterableTestClass> points = new ArrayList<>();
        points.add(new ClusterableTestClass(1, 1));
        points.add(new ClusterableTestClass(2, 2));
        points.add(new ClusterableTestClass(4, 4));
        points.add(new ClusterableTestClass(5, 5));
        
        // Test clustering with k=2 and maxIterations=10
        List<Cluster<ClusterableTestClass>> clusters = clusterer.cluster(points, 2, 10);
        
        assertEquals(2, clusters.size()); // Check if correct number of clusters are formed
    }

    @Test
    public void testInconsistentPointDimension() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterableTestClass> clusterer = new KMeansPlusPlusClusterer<>(random);

        // Generate test data with inconsistent dimensions
        List<ClusterableTestClass> points = new ArrayList<>();
        points.add(new ClusterableTestClass(1, 1));
        points.add(new ClusterableTestClass(2, 2, 3)); // Dimension mismatch
        
        // Test clustering with k=2 and maxIterations=10
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clusterer.cluster(points, 2, 10);
        });
        
        assertEquals("Points have inconsistent dimensions", exception.getMessage());
    }

    @Test
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterableTestClass> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        // Generate test data
        List<ClusterableTestClass> points = new ArrayList<>();
        
        // Test clustering with k=2 and maxIterations=10
        Exception exception = assertThrows(ConvergenceException.class, () -> {
            clusterer.cluster(points, 2, 10);
        });
        
        assertEquals("Empty cluster encountered in K-means", exception.getMessage());
    }
    
    // Add more test cases for other methods as needed

}

class ClusterableTestClass implements Clusterable<ClusterableTestClass> {
    private double[] point;
    
    public ClusterableTestClass(double... point) {
        this.point = point;
    }
    
    @Override
    public double distanceFrom(ClusterableTestClass other) {
        double sum = 0.0;
        for (int i = 0; i < point.length; i++) {
            sum += Math.pow(point[i] - other.point[i], 2);
        }
        return Math.sqrt(sum);
    }
    
    @Override
    public ClusterableTestClass centroidOf(Collection<ClusterableTestClass> points) {
        double[] sum = new double[point.length];
        for (ClusterableTestClass p : points) {
            for (int i = 0; i < p.point.length; i++) {
                sum[i] += p.point[i];
            }
        }
        double[] centroid = new double[point.length];
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = sum[i] / points.size();
        }
        return new ClusterableTestClass(centroid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClusterableTestClass other = (ClusterableTestClass) obj;
        return Arrays.equals(point, other.point);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }
}