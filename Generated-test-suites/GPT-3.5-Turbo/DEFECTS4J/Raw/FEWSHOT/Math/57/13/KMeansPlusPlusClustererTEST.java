package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class KMeansPlusPlusClustererTest {

    private KMeansPlusPlusClusterer<Point> kMeansPlusPlusClusterer;

    @Before
    public void setUp() {
        Random random = new Random();
        kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer<>(random);
    }

    @Test
    public void testCluster_ValidInput() {
        List<Point> points = Arrays.asList(new Point(0, 0), new Point(1, 1), new Point(2, 2));
        int k = 2;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = kMeansPlusPlusClusterer.cluster(points, k, maxIterations);

        assertNotNull(clusters);
        assertEquals(k, clusters.size());

        for (Cluster<Point> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test
    public void testCluster_EmptyInput() {
        List<Point> points = new ArrayList<>();
        int k = 2;
        int maxIterations = 100;
        List<Cluster<Point>> clusters = kMeansPlusPlusClusterer.cluster(points, k, maxIterations);

        assertNotNull(clusters);
        assertTrue(clusters.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCluster_NullInput() {
        List<Point> points = null;
        int k = 2;
        int maxIterations = 100;
        kMeansPlusPlusClusterer.cluster(points, k, maxIterations);
    }

    @Test
     public void testGetNearestCluster_ValidInput() {
        List<Cluster<Point>> clusters = Arrays.asList(new Cluster<>(new Point(0, 0)), new Cluster<>(new Point(1, 1)), new Cluster<>(new Point(2, 2)));
        Point point = new Point(1.5, 1.5);
        Cluster<Point> nearestCluster = kMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        assertNotNull(nearestCluster);
        assertTrue(clusters.contains(nearestCluster));
    }

    @Test
    public void testGetNearestCluster_EmptyInput() {
        List<Cluster<Point>> clusters = new ArrayList<>();
        Point point = new Point(1.5, 1.5);
        Cluster<Point> nearestCluster = kMeansPlusPlusClusterer.getNearestCluster(clusters, point);

        assertNull(nearestCluster);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNearestCluster_NullInput() {
        List<Cluster<Point>> clusters = null;
        Point point = new Point(1.5, 1.5);
        kMeansPlusPlusClusterer.getNearestCluster(clusters, point);
    }
}