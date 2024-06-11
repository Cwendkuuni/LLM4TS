package org.apache.commons.math.stat.clustering;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class KMeansPlusPlusClustererTest {
    
    private KMeansPlusPlusClusterer<Point> clusterer;
    private Collection<Point> points;
    private int k;
    private int maxIterations;
    
    @Before
    public void setUp() {
        clusterer = new KMeansPlusPlusClusterer<Point>(new Random());
        points = new ArrayList<Point>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        k = 2;
        maxIterations = 10;
    }
    
    @Test
    public void testCluster() {
        List<Cluster<Point>> result = clusterer.cluster(points, k, maxIterations);
        assertEquals(k, result.size());
        for (Cluster<Point> cluster : result) {
            for (Point point : cluster.getPoints()) {
                assertEquals(cluster, clusterer.getNearestCluster(result, point));
            }
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyClusters() {
        KMeansPlusPlusClusterer<Point> clusterer = new KMeansPlusPlusClusterer<Point>(new Random() {
            @Override
            public int nextInt(int n) {
                return 0;
            }
        });
        clusterer.cluster(points, k, maxIterations);
    }
    
    private class Point implements Clusterable<Point> {
        
        private double x;
        private double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
        public double distanceFrom(Point other) {
            double dx = getX() - other.getX();
            double dy = getY() - other.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        public Point centroidOf(Collection<Point> points) {
            double sumX = 0;
            double sumY = 0;
            for (Point point : points) {
                sumX += point.getX();
                sumY += point.getY();
            }
            return new Point(sumX / points.size(), sumY / points.size());
        }
    }
}