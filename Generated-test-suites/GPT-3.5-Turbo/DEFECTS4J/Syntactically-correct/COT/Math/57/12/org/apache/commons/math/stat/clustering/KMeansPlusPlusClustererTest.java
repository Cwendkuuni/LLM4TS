
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        
        List<MyPoint> points = new ArrayList<>();
        points.add(new MyPoint(0, 0));
        points.add(new MyPoint(1, 1));
        points.add(new MyPoint(10, 10));
        points.add(new MyPoint(11, 11));
        
        List<Cluster<MyPoint>> clusters = clusterer.cluster(points, 2, 100);
        
        assertEquals(2, clusters.size());
        
        for (Cluster<MyPoint> cluster : clusters) {
            List<MyPoint> clusterPoints = cluster.getPoints();
            
            for (MyPoint point : clusterPoints) {
                assertTrue(points.contains(point));
            }
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterInKMeans() {
        Random random = new Random();
        KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        
        List<MyPoint> points = new ArrayList<>();
        
        clusterer.cluster(points, 2, 100);
    }
    
    static class MyPoint implements Clusterable<MyPoint> {
        private double x;
        private double y;
        
        public MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public double distanceFrom(MyPoint other) {
            double dx = x - other.x;
            double dy = y - other.y;
            
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        @Override
        public MyPoint centroidOf(Collection<MyPoint> points) {
            double sumX = 0;
            double sumY = 0;
            
            for (MyPoint point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            
            double centerX = sumX / points.size();
            double centerY = sumY / points.size();
            
            return new MyPoint(centerX, centerY);
        }
    }

}
