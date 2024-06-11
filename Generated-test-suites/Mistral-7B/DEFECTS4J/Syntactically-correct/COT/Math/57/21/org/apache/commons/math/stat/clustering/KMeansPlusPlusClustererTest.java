import org.apache.commons.math3.stat.clustering.*;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class KMeansPlusPlusClustererTest {

 private static final double DELTA = 1e-7;

 @Mock
 private Random random;

 @Mock
 private Cluster<ClusterableTestPoint> emptyCluster;

 @Test
 public void testEmptyConstructor() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>();
 assertNotNull(clusterer.random);
 assertNull(clusterer.emptyStrategy);
 }

 @Test
 public void testConstructorWithRandom() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer(random);
 assertSame(random, clusterer.random);
 assertNull(clusterer.emptyStrategy);
 }

 @Test
 public void testConstructorWithRandomAndStrategy() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer(random, EmptyClusterStrategy.LARGEST_VARIANCE);
 assertSame(random, clusterer.random);
 assertSame(EmptyClusterStrategy.LARGEST_VARIANCE, clusterer.emptyStrategy);
 }

 @Test(expected = ConvergenceException.class)
 public void testClusterWithEmptyCollection() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(Collections.emptyList(), 3, 1);
 fail("Should have thrown ConvergenceException");
 }

 @Test
 public void testClusterWithNullCollection() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 try {
 List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(null, 3, 1);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 }
 }

 @Test
 public void testClusterWithNegativeMaxIterations() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(Arrays.asList(new ClusterableTestPoint(1.0, 1.0), new ClusterableTestPoint(2.0, 2.0)), 3, -1);
 assertNotNull(clusters);
 assertEquals(3, clusters.size());
 }

 @Test
 public void testClusterWithMaxIterations() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = clusterer.cluster(Arrays.asList(new ClusterableTestPoint(1.0, 1.0), new ClusterableTestPoint(2.0, 2.0)), 3, 10);
 assertNotNull(clusters);
 assertEquals(3, clusters.size());
 }

 @Test
 public void testChooseInitialCentersWithEmptyCollection() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> result = clusterer.chooseInitialCenters(Collections.emptyList(), 3, random);
 assertEquals(3, result.size());
 for (Cluster<ClusterableTestPoint> cluster : result) {
 assertEquals(0.0, cluster.getCenter().getX(), DELTA);
 assertEquals(0.0, cluster.getCenter().getY(), DELTA);
 }
 }

 @Test
 public void testChooseInitialCenters() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<ClusterableTestPoint> points = Arrays.asList(new ClusterableTestPoint(1.0, 1.0), new ClusterableTestPoint(2.0, 2.0), new ClusterableTestPoint(3.0, 3.0));
 List<Cluster<ClusterableTestPoint>> initialCenters = clusterer.chooseInitialCenters(points, 3, random);
 assertEquals(3, initialCenters.size());
 for (Cluster<ClusterableTestPoint> cluster : initialCenters) {
 // Initial centers should be uniformly distributed among the data points
 double minDistance = Double.MAX_VALUE;
 ClusterableTestPoint minPoint = null;
 for (ClusterableTestPoint point : points) {
 double distance = point.distanceFrom(cluster.getCenter());
 if (distance < minDistance) {
 minDistance = distance;
 minPoint = point;
 }
 }
 assertNotNull(minPoint);
 }
 }

 @Test
 public void testAssignPointsToClusters() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0)));
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(1.0, 1.0)));
 List<ClusterableTestPoint> points = Arrays.asList(new ClusterableTestPoint(0.5, 0.5), new ClusterableTestPoint(1.5, 1.5));
 assignPointsToClusters(clusterer, clusters, points);
 for (Cluster<ClusterableTestPoint> cluster : clusters) {
 assertEquals(1, cluster.getPoints().size());
 }
 }

 @Test
 public void testAssignPointsToClustersWithEmptyList() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0)));
 List<ClusterableTestPoint> points = new ArrayList<>();
 assignPointsToClusters(clusterer, clusters, points);
 assertTrue(clusters.get(0).getPoints().isEmpty());
 }

 @Test
 public void testAssignPointsToClustersWithNullList() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0)));
 try {
 assignPointsToClusters(clusterer, clusters, null);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 }
 }

 @Test
 public void testGetNearestCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0)));
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(1.0, 1.0)));
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 Cluster<ClusterableTestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
 assertEquals(ClusterableTestPoint.class, nearestCluster.getClass());
 assertEquals(0.0, nearestCluster.getCenter().getX(), DELTA);
 assertEquals(0.0, nearestCluster.getCenter().getY(), DELTA);
 }

 @Test
 public void testGetNearestClusterWithEmptyList() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 Cluster<ClusterableTestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
 assertNull(nearestCluster);
 }

 @Test
 public void testGetNearestClusterWithNullList() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = null;
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 Cluster<ClusterableTestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
 assertNull(nearestCluster);
 }

 @Test
 public void testGetNearestClusterWithSameCenter() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.5, 0.5)));
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 Cluster<ClusterableTestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
 assertEquals(ClusterableTestPoint.class, nearestCluster.getClass());
 assertEquals(point.getX(), nearestCluster.getCenter().getX(), DELTA);
 assertEquals(point.getY(), nearestCluster.getCenter().getY(), DELTA);
 }

 @Test
 public void testGetNearestClusterWithEmptyCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(emptyCluster);
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 Cluster<ClusterableTestPoint> nearestCluster = clusterer.getNearestCluster(clusters, point);
 assertNull(nearestCluster);
 }

 @Test(expected = ConvergenceException.class)
 public void testGetNearestClusterWithEmptyClusterAndStrategyError() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(emptyCluster);
 ClusterableTestPoint point = new ClusterableTestPoint(0.5, 0.5);
 clusterer.getNearestCluster(clusters, point);
 fail("Should have thrown ConvergenceException");
 }

 @Test
 public void testGetPointFromLargestVarianceCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0), Arrays.asList(new ClusterableTestPoint(0.5, 0.5), new ClusterableTestPoint(1.0, 1.0))));
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(1.0, 1.0), Arrays.asList(new ClusterableTestPoint(1.5, 1.5), new ClusterableTestPoint(2.0, 2.0))));
 Cluster<ClusterableTestPoint> largestVarianceCluster = clusters.get(0);
 T pointFromLargestVarianceCluster = clusterer.getPointFromLargestVarianceCluster(clusters);
 assertSame(largestVarianceCluster.getPoints().get(0), pointFromLargestVarianceCluster);
 }

 @Test
 public void testGetPointFromLargestNumberCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0), Arrays.asList(new ClusterableTestPoint(0.5, 0.5), new ClusterableTestPoint(1.0, 1.0))));
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(1.0, 1.0), Arrays.asList(new ClusterableTestPoint(1.5, 1.5), new ClusterableTestPoint(2.0, 2.0), new ClusterableTestPoint(2.5, 2.5))));
 Cluster<ClusterableTestPoint> largestNumberCluster = clusters.get(1);
 T pointFromLargestNumberCluster = clusterer.getPointFromLargestNumberCluster(clusters);
 assertSame(largestNumberCluster.getPoints().get(2), pointFromLargestNumberCluster);
 }

 @Test
 public void testGetPointFromLargestNumberClusterWithEmptyCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(emptyCluster);
 T pointFromLargestNumberCluster = clusterer.getPointFromLargestNumberCluster(clusters);
 assertNull(pointFromLargestNumberCluster);
 }

 @Test
 public void testGetFarthestPoint() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(0.0, 0.0), Arrays.asList(new ClusterableTestPoint(0.5, 0.5), new ClusterableTestPoint(1.0, 1.0))));
 clusters.add(new Cluster<ClusterableTestPoint>(new ClusterableTestPoint(1.0, 1.0), Arrays.asList(new ClusterableTestPoint(1.5, 1.5), new ClusterableTestPoint(2.0, 2.0), new ClusterableTestPoint(2.5, 2.5))));
 T farthestPoint = clusterer.getFarthestPoint(clusters);
 Cluster<ClusterableTestPoint> nearestCluster = clusters.get(0);
 double distanceFromNearest = nearestCluster.getPoints().get(0).distanceFrom(nearestCluster.getCenter());
 double distanceFromFarthest = farthestPoint.distanceFrom(nearestCluster.getCenter());
 assertTrue(distanceFromFarthest > distanceFromNearest);
 }

 @Test
 public void testGetFarthestPointWithEmptyCluster() {
 KMeansPlusPlusClusterer<ClusterableTestPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
 List<Cluster<ClusterableTestPoint>> clusters = new ArrayList<>();
 clusters.add(emptyCluster);
 T farthestPoint = clusterer.getFarthestPoint(clusters);
 assertNull(farthestPoint);
 }

}