import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyClusterable> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<MyClusterable> points = new ArrayList<>();
        // Add test points
        points.add(new MyClusterable(1.0, 1.0));
        points.add(new MyClusterable(2.0, 2.0));
        points.add(new MyClusterable(3.0, 3.0));
        int k = 2;
        int maxIterations = 100;
        
        List<Cluster<MyClusterable>> clusters = clusterer.cluster(points, k, maxIterations);
        
        assertNotNull(clusters);
        assertEquals(k, clusters.size());
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithZeroPoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyClusterable> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<MyClusterable> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 100;
        
        clusterer.cluster(points, k, maxIterations);
    }

    class MyClusterable implements Clusterable<MyClusterable> {
        private double x;
        private double y;
        
        public MyClusterable(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(MyClusterable other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public MyClusterable centroidOf(List<MyClusterable> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            for (MyClusterable point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            double centroidX = sumX / points.size();
            double centroidY = sumY / points.size();
            return new MyClusterable(centroidX, centroidY);
        }
    }
}