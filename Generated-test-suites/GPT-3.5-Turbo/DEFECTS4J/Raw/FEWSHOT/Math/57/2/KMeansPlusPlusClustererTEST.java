package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class KMeansPlusPlusClustererTest {
    
    @Test
    public void testCluster() {
        MockClusterable p1 = new MockClusterable(1, 1);
        MockClusterable p2 = new MockClusterable(2, 2);
        MockClusterable p3 = new MockClusterable(3, 3);
        MockClusterable p4 = new MockClusterable(10, 10);
        MockClusterable p5 = new MockClusterable(11, 11);
        MockClusterable p6 = new MockClusterable(12, 12);

        List<MockClusterable> points = Arrays.asList(p1, p2, p3, p4, p5, p6);
        KMeansPlusPlusClusterer<MockClusterable> clusterer = new KMeansPlusPlusClusterer<>(points, 2, 10);
        
        List<Cluster<MockClusterable>> clusters = clusterer.cluster();
        
        assertEquals(2, clusters.size());
        
        for (Cluster<MockClusterable> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterEmptyCluster() {
        MockClusterable p1 = new MockClusterable(1, 1);
        MockClusterable p2 = new MockClusterable(2, 2);
        MockClusterable p3 = new MockClusterable(10, 10);

        List<MockClusterable> points = Arrays.asList(p1, p2, p3);
        KMeansPlusPlusClusterer<MockClusterable> clusterer = new KMeansPlusPlusClusterer<>(points, 2, 10);
        
        List<Cluster<MockClusterable>> clusters = clusterer.cluster();

        for (Cluster<MockClusterable> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
    }
    
    private static class MockClusterable implements Clusterable<MockClusterable> {

        private double x;
        private double y;

        public MockClusterable(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(MockClusterable other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public MockClusterable centroidOf(List<MockClusterable> points) {
            double sumX = x;
            double sumY = y;
            for (MockClusterable point : points) {
                sumX += point.x;
                sumY += point.y;
            }
            double centerX = sumX / (points.size() + 1);
            double centerY = sumY / (points.size() + 1);
            return new MockClusterable(centerX, centerY);
        }
    }
}