package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE;
import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER;
import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT;
import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KMeansPlusPlusClusterer.class)
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-10;
    private static final int K = 3;
    private static final int MAX_ITERATIONS = 10;

    private KMeansPlusPlusClusterer<Point2D> clusterer;
    private List<Point2D> points;

    @Before
    public void setup() {
        this.clusterer = new KMeansPlusPlusClusterer<>(new Random(), LARGEST_VARIANCE);
        this.points = Arrays.asList(
            new Point2D(1.0, 1.0), new Point2D(2.0, 2.0), new Point2D(3.0, 3.0),
            new Point2D(4.0, 4.0), new Point2D(5.0, 5.0)
        );
    }

    @Test
    public void shouldClusterPointsCorrectly() {
        List<Cluster<Point2D>> clusters = this.clusterer.cluster(this.points, K, MAX_ITERATIONS);

        assertEquals(K, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            assertFalse(cluster.isEmpty());
            double centerX = cluster.getCenter().getX();
            double centerY = cluster.getCenter().getY();
            List<Point2D> pointsInCluster = new ArrayList<>();
            for (Point2D point : this.points) {
                if (cluster.contains(point)) {
                    pointsInCluster.add(point);
                }
            }
            double centerOfMassX = pointsInCluster.stream().mapToDouble(p -> p.getX()).sum() / pointsInCluster.size();
            double centerOfMassY = pointsInCluster.stream().mapToDouble(p -> p.getY()).sum() / pointsInCluster.size();
            assertEquals(Precision.round(centerX, EPSILON), centerOfMassX, EPSILON);
            assertEquals(Precision.round(centerY, EPSILON), centerOfMassY, EPSILON);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void shouldThrowConvergenceExceptionWhenEmptyClusterAppears() {
        PowerMockito.spy(KMeansPlusPlusClusterer.class);
        Mockito.doThrow(new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS)).when(KMeansPlusPlusClusterer.class).getPointFromLargestNumberCluster(Mockito.anyList());
        this.clusterer = new KMeansPlusPlusClusterer<>(new Random(), ERROR);

        this.clusterer.cluster(Collections.emptyList(), K, MAX_ITERATIONS);
    }

}

class Point2D implements Clusterable<Point2D> {

    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Point2D centroidOf(Collection<? extends Clusterable<Point2D>> points) {
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        for (Clusterable<Point2D> point : points) {
            xCoordinates.add(point.getX());
            yCoordinates.add(point.getY());
        }
        return new Point2D(xCoordinates.stream().mapToDouble(d -> d).sum() / points.size(),
                          yCoordinates.stream().mapToDouble(d -> d).sum() / points.size());
    }

    public double distanceFrom(Clusterable<Point2D> other) {
        Point2D otherPoint = (Point2D) other;
        return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
    }

}