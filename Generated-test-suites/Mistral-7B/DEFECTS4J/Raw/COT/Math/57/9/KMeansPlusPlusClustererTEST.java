import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

 private Random random;
 private KMeansPlusPlusClusterer<T> clusterer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 random = new Random();
 clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPoints() {
 clusterer.cluster(null, 1, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeK() {
 clusterer.cluster(Arrays.asList(new Point2D(0.0, 0.0)), -1, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxIterations() {
 clusterer.cluster(Arrays.asList(new Point2D(0.0, 0.0)), 1, -1);
 }

 @Test
 public void testEmptyPoints() {
 Collection<T> points = new ArrayList<>();
 List<Cluster<T>> clusters = clusterer.cluster(points, 1, 10);
 assertNotNull(clusters);
 assertNotNull(clusters.get(0).getCenter());
 assertNull(clusters.get(0).getPoints());
 }

 @Test
 public void testSinglePoint() {
 Collection<T> points = Arrays.asList(new Point2D(0.0, 0.0));
 List<Cluster<T>> clusters = clusterer.cluster(points, 1, 10);
 assertNotNull(clusters);
 assertNotNull(clusters.get(0).getCenter());
 assertEquals(points.size(), clusters.get(0).getPoints().size());
 }

 @Test
 public void testMultiplePoints() {
 Collection<T> points = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), new Point2D(2.0, 2.0), new Point2D(3.0, 3.0));
 List<Cluster<T>> clusters = clusterer.cluster(points, 2, 10);
 assertNotNull(clusters);
 assertNotNull(clusters.get(0).getCenter());
 assertNotNull(clusters.get(1).getCenter());
 assertEquals(2, clusters.size());
 List<T> cluster1Points = clusters.get(0).getPoints();
 List<T> cluster2Points = clusters.get(1).getPoints();
 assertNotNull(cluster1Points);
 assertNotNull(cluster2Points);
 assertEquals(1, cluster1Points.size());
 assertEquals(1, cluster2Points.size());
 for (T point : points) {
 if (!cluster1Points.contains(point)) {
 assertEquals(clusters.get(1).getCenter(), point, 0.1);
 }
 }
 }

 @Test
 public void testGetNearestCluster() {
 Collection<Cluster<T>> clusters = Arrays.asList(new Cluster<T>(new Point2D(0.0, 0.0)), new Cluster<T>(new Point2D(1.0, 1.0)), new Cluster<T>(new Point2D(2.0, 2.0)));
 T point = new Point2D(1.5, 1.5);
 Cluster<T> nearest = KMeansPlusPlusClusterer.getNearestCluster(clusters, point);
 assertEquals(clusters.get(1), nearest);
 }

 @Test
 public void testGetPointFromLargestVarianceCluster() {
 Collection<Cluster<T>> clusters = Arrays.asList(new Cluster<T>(new Point2D(0.0, 0.0), Arrays.asList(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5))),
 new Cluster<T>(new Point2D(1.0, 1.0), Arrays.asList(new Point2D(1.5, 1.5), new Point2D(2.0, 2.0))));
 T point = clusters.get(0).getPoints().get(0);
 T pointFromLargestVarianceCluster = clusterer.getPointFromLargestVarianceCluster(clusters);
 assertNotNull(pointFromLargestVarianceCluster);
 assertFalse(point.equals(pointFromLargestVarianceCluster));
 }

 @Test
 public void testGetPointFromLargestNumberCluster() {
 Collection<Cluster<T>> clusters = Arrays.asList(new Cluster<T>(new Point2D(0.0, 0.0), Arrays.asList(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5))),
 new Cluster<T>(new Point2D(1.0, 1.0), Arrays.asList(new Point2D(1.5, 1.5), new Point2D(2.0, 2.0))));
 T pointFromLargestNumberCluster = clusterer.getPointFromLargestNumberCluster(clusters);
 assertNotNull(pointFromLargestNumberCluster);
 assertFalse(pointFromLargestNumberCluster.equals(clusters.get(0).getPoints().get(0)));
 assertFalse(pointFromLargestNumberCluster.equals(clusters.get(1).getPoints().get(0)));
 }

 @Test
 public void testGetFarthestPoint() {
 Collection<Cluster<T>> clusters = Arrays.asList(new Cluster<T>(new Point2D(0.0, 0.0), Arrays.asList(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5))),
 new Cluster<T>(new Point2D(1.0, 1.0), Arrays.asList(new Point2D(1.5, 1.5), new Point2D(2.0, 2.0))));
 T farthestPoint = clusterer.getFarthestPoint(clusters);
 assertNotNull(farthestPoint);
 assertFalse(farthestPoint.equals(clusters.get(0).getPoints().get(0)));
 assertFalse(farthestPoint.equals(clusters.get(1).getPoints().get(0)));
 }

 @Test
 public void testAssignPointsToClusters() {
 Collection<T> points = Arrays.asList(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5), new Point2D(2.5, 2.5));
 List<Cluster<T>> clusters = new ArrayList<>();
 clusters.add(new Cluster<T>(new Point2D(0.0, 0.0)));
 clusters.add(new Cluster<T>(new Point2D(1.0, 1.0)));
 KMeansPlusPlusClusterer.assignPointsToClusters(clusters, points);
 assertEquals(3, clusters.get(0).getPoints().size());
 assertEquals(0, clusters.get(1).getPoints().size());
 }

}