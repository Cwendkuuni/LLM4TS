package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.clustering.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testCluster() {
        // Test with 2D points
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Point2D> points = Arrays.asList(
                new Point2D(1.0, 1.0),
                new Point2D(1.5, 1.5),
                new Point2D(2.0, 2.0),
                new Point2D(2.5, 2.5),
                new Point2D(3.0, 3.0)
        );

        List<Cluster<Point2D>> clusters = kmeans.cluster(points, 3, 10);
        assertEquals(3, clusters.size());

        // Check clusters contain expected points
        List<Point2D> expectedCluster1 = Arrays.asList(new Point2D(1.0, 1.0), new Point2D(1.5, 1.5));
        List<Point2D> expectedCluster2 = Arrays.asList(new Point2D(2.0, 2.0), new Point2D(2.5, 2.5));
        List<Point2D> expectedCluster3 = Arrays.asList(new Point2D(3.0, 3.0));

        for (int i = 0; i < clusters.size(); i++) {
            List<Point2D> clusterPoints = clusters.get(i).getPoints();
            assertEquals(expectedCluster1.size(), clusterPoints.size());
            assertTrue(expectedCluster1.containsAll(clusterPoints));
            expectedCluster1.clear();
            expectedCluster1.addAll(clusterPoints);

            Cluster<Point2D> cluster = clusters.get(i);
            Point2D center = cluster.getCenter();
            Point2D expectedCenter = new Point2D(
                    (sum(new RealMatrix(getPointsMatrix(clusterPoints)))).getEntry(0, 0) / clusterPoints.size(),
                    (sum(new RealMatrix(getPointsMatrix(clusterPoints)))).getEntry(0, 1) / clusterPoints.size()
            );
            assertEquals(expectedCenter.getX(), center.getX(), 0.001);
            assertEquals(expectedCenter.getY(), center.getY(), 0.001);
        }
    }

    private RealMatrix getPointsMatrix(List<Point2D> points) {
        double[][] data = new double[points.size()][2];
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            data[i][0] = point.getX();
            data[i][1] = point.getY();
        }
        return new Array2DRowRealMatrix(data);
    }

    private double sum(RealMatrix matrix) {
        double sum = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double[] row = matrix.getRowVector(i);
            for (int j = 0; j < row.length; j++) {
                sum += row[j];
            }
        }
        return sum;
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<Point2D> points = Arrays.asList(
                new Point2D(1.0, 1.0),
                new Point2D(1.5, 1.5),
                new Point2D(2.0, 2.0),
                new Point2D(2.5, 2.5)
        );

        kmeans.cluster(points, 3, 10);
    }
}

class Point2D implements Clusterable<Point2D> {
    private final double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(Clusterable<?> other) {
        Point2D that = (Point2D) other;
        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
    }

    @Override
    public double distanceFrom(Cluster<Point2D> cluster) {
        return distanceFrom(cluster.getCenter());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}