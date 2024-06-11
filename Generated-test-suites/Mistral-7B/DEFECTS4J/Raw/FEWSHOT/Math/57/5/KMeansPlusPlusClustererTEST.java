package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.*;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy_LargestVariance() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(0.0, 0.0, 0.0), new Point3D(0.0, 0.0, 0.0)};
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        Cluster<Clusterable> largestVarianceCluster = null;
        double maxVariance = 0;
        for (Cluster<Clusterable> cluster : clusters) {
            if (!cluster.isEmpty()) {
                Variance variance = new Variance();
                for (Clusterable p : cluster.getPoints()) {
                    variance.increment(p.distanceFrom(cluster.getCenter()));
                }
                double varianceValue = variance.getResult();
                if (varianceValue > maxVariance) {
                    maxVariance = varianceValue;
                    largestVarianceCluster = cluster;
                }
            }
        }
        assertEquals(2, clusters.size());
        assertTrue(largestVarianceCluster != null);
        assertEquals(points[1], largestVarianceCluster.getCenter());
    }

    @Test
    public void testEmptyClusterStrategy_LargestPointsNumber() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(0.0, 0.0, 0.0), new Point3D(0.0, 0.0, 0.0)};
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        Cluster<Clusterable> largestPointsNumberCluster = null;
        int maxPointsNumber = 0;
        for (Cluster<Clusterable> cluster : clusters) {
            if (!cluster.isEmpty()) {
                int pointsNumber = cluster.getPoints().size();
                if (pointsNumber > maxPointsNumber) {
                    maxPointsNumber = pointsNumber;
                    largestPointsNumberCluster = cluster;
                }
            }
        }
        assertEquals(2, clusters.size());
        assertTrue(largestPointsNumberCluster != null);
        assertEquals(points[2], largestPointsNumberCluster.getCenter());
    }

    @Test
    public void testEmptyClusterStrategy_FarthestPoint() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(0.0, 0.0, 0.0), new Point3D(0.0, 0.0, 0.0)};
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        Cluster<Clusterable> farthestPointCluster = null;
        double maxDistance = 0;
        for (Cluster<Clusterable> cluster : clusters) {
            if (!cluster.isEmpty()) {
                List<Clusterable> pointsInCluster = cluster.getPoints();
                double currentDistance = Precision.distance(pointsInCluster.get(0).getX(), pointsInCluster.get(0).getY(), pointsInCluster.get(0).getZ(), points[3].getX(), points[3].getY(), points[3].getZ());
                if (currentDistance > maxDistance) {
                    maxDistance = currentDistance;
                    farthestPointCluster = cluster;
                }
            }
        }
        assertEquals(2, clusters.size());
        assertTrue(farthestPointCluster != null);
        assertEquals(points[3], farthestPointCluster.getCenter());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategy_Error() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(0.0, 0.0, 0.0), new Point3D(0.0, 0.0, 0.0)};
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        assertEquals(0, clusters.size());
    }

    @Test
    public void testKMeansPlusPlusClusterer_WithInitialCenters() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(2.0, 2.0, 2.0), new Point3D(2.0, 2.0, 3.0)};
        List<Cluster<Clusterable>> expectedClusters = new ArrayList<>();
        expectedClusters.add(new Cluster<Clusterable>(new Point3D(1.0, 1.0, 1.0)));
        expectedClusters.add(new Cluster<Clusterable>(new Point3D(2.0, 2.0, 2.0)));
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        assertEquals(expectedClusters.size(), clusters.size());
        for (int i = 0; i < expectedClusters.size(); i++) {
            assertTrue(areClusterEqual(expectedClusters.get(i), clusters.get(i)));
        }
    }

    @Test
    public void testKMeansPlusPlusClusterer_WithRandomInitialCenters() {
        Clusterable[] points = new Clusterable[]{new Point3D(1.0, 1.0, 1.0), new Point3D(1.0, 1.0, 2.0), new Point3D(2.0, 2.0, 2.0), new Point3D(2.0, 2.0, 3.0)};
        List<Cluster<Clusterable>> expectedClusters = new ArrayList<>();
        expectedClusters.add(new Cluster<Clusterable>(new Point3D(1.5, 1.5, 1.5)));
        expectedClusters.add(new Cluster<Clusterable>(new Point3D(2.5, 2.5, 2.5)));
        List<Cluster<Clusterable>> clusters = new ArrayList<>();
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        clusters = clusterer.cluster(Arrays.asList(points), 2, 10);
        for (int i = 0; i < expectedClusters.size(); i++) {
            assertTrue(areClusterEqual(expectedClusters.get(i), clusters.get(i)));
        }
    }

    private boolean areClusterEqual(Cluster<Clusterable> a, Cluster<Clusterable> b) {
        if (a.getCenter() == b.getCenter()) {
            return true;
        }
        List<Clusterable> aPoints = a.getPoints();
        List<Clusterable> bPoints = b.getPoints();
        if (aPoints.size() != bPoints.size()) {
            return false;
        }
        for (int i = 0; i < aPoints.size(); i++) {
            if (!aPoints.get(i).equals(bPoints.get(i))) {
                return false;
            }
        }
        return true;
    }

    static class Point3D implements Clusterable<Point3D> {
        private final double x;
        private final double y;
        private final double z;

        public Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public Point3D centroidOf(Collection<Point3D> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            double sumZ = 0.0;
            int sumPoints = 0;
            for (Point3D point : points) {
                sumX += point.x;
                sumY += point.y;
                sumZ += point.z;
                sumPoints++;
            }
            return new Point3D(sumX / sumPoints, sumY / sumPoints, sumZ / sumPoints);
        }

        public double distanceFrom(Clusterable point) {
            Point3D otherPoint = (Point3D) point;
            double dx = x - otherPoint.x;
            double dy = y - otherPoint.y;
            double dz = z - otherPoint.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        public double distanceFrom(Point3D point) {
            return distanceFrom((Clusterable) point);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Point3D other = (Point3D) obj;
            if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
                return false;
            }
            if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
                return false;
            }
            if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Point3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
        }
    }
}