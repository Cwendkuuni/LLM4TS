import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

 private static final int NUM_POINTS = 10;
 private static final int K = 3;
 private static final int MAX_ITERATIONS = 100;

 @SuppressWarnings("unchecked")
 @Test
 public void testBasicFunctionality() {
 KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
 List<Cluster<Point2D>> clusters = clustering.cluster(generatePoints(NUM_POINTS), K, MAX_ITERATIONS);
 assertEquals(K, clusters.size());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = ConvergenceException.class)
 public void testEmptyClusterError() {
 KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
 List<Cluster<Point2D>> clusters = clustering.cluster(generateEmptyPoints(NUM_POINTS), K, MAX_ITERATIONS);
 fail("Expected ConvergenceException but no exception was thrown.");
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testLargestVarianceEmptyClusterStrategy() {
 KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
 List<Cluster<Point2D>> clusters = clustering.cluster(generatePointsWithTwoEmptyClusters(NUM_POINTS), K, MAX_ITERATIONS);
 assertEquals(K, clusters.size());
 Cluster<Point2D> largestVarianceCluster = clusters.get(getIndexOfClusterWithLargestVariance(clusters));
 Point2D pointFromLargestVarianceCluster = largestVarianceCluster.getPoints().get(random.nextInt(largestVarianceCluster.getPoints().size()));
 assertNotNull(pointFromLargestVarianceCluster);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testLargestPointsNumberEmptyClusterStrategy() {
 KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
 List<Cluster<Point2D>> clusters = clustering.cluster(generatePointsWithTwoEmptyClusters(NUM_POINTS), K, MAX_ITERATIONS);
 assertEquals(K, clusters.size());
 Cluster<Point2D> largestPointsNumberCluster = clusters.get(getIndexOfClusterWithLargestPointsNumber(clusters));
 Point2D pointFromLargestPointsNumberCluster = largestPointsNumberCluster.getPoints().get(random.nextInt(largestPointsNumberCluster.getPoints().size()));
 assertNotNull(pointFromLargestPointsNumberCluster);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFarthestPointEmptyClusterStrategy() {
 KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
 List<Cluster<Point2D>> clusters = clustering.cluster(generatePointsWithTwoEmptyClusters(NUM_POINTS), K, MAX_ITERATIONS);
 assertEquals(K, clusters.size());
 Cluster<Point2D> farthestPointCluster = clusters.get(getIndexOfClusterWithFarthestPoint(clusters));
 Point2D pointFromFarthestPointCluster = farthestPointCluster.getPoints().get(random.nextInt(farthestPointCluster.getPoints().size()));
 assertNotNull(pointFromFarthestPointCluster);
 }

 private List<Point2D> generatePoints(int numPoints) {
 List<Point2D> points = new ArrayList<>();
 for (int i = 0; i < numPoints; i++) {
 points.add(new Point2D(random.nextDouble(), random.nextDouble()));
 }
 return points;
 }

 private List<Point2D> generateEmptyPoints(int numPoints) {
 List<Point2D> points = new ArrayList<>();
 for (int i = 0; i < numPoints; i++) {
 points.add(new Point2D(Double.NaN, Double.NaN));
 }
 return points;
 }

 private List<Point2D> generatePointsWithTwoEmptyClusters(int numPoints) {
 List<Point2D> points = generatePoints(numPoints);
 points.remove(random.nextInt(points.size()));
 points.remove(random.nextInt(points.size()));
 return points;
 }

 private int getIndexOfClusterWithLargestVariance(List<Cluster<Point2D>> clusters) {
 int largestVarianceIndex = 0;
 double largestVariance = 0.0;
 for (int i = 0; i < clusters.size(); i++) {
 double variance = clusters.get(i).getVariance();
 if (variance > largestVariance) {
 largestVarianceIndex = i;
 largestVariance = variance;
 }
 }
 return largestVarianceIndex;
 }

 private int getIndexOfClusterWithLargestPointsNumber(List<Cluster<Point2D>> clusters) {
 int largestPointsNumberIndex = 0;
 int largestPointsNumber = 0;
 for (int i = 0; i < clusters.size(); i++) {
 int pointsNumber = clusters.get(i).getPoints().size();
 if (pointsNumber > largestPointsNumber) {
 largestPointsNumberIndex = i;
 largestPointsNumber = pointsNumber;
 }
 }
 return largestPointsNumberIndex;
 }

 private int getIndexOfClusterWithFarthestPoint(List<Cluster<Point2D>> clusters) {
 int farthestPointIndex = 0;
 double maxDistance = 0.0;
 for (int i = 0; i < clusters.size(); i++) {
 Point2D farthestPoint = clusters.get(i).getCenter();
 for (Point2D point : clusters.get(i).getPoints()) {
 double distance = point.distanceFrom(farthestPoint);
 if (distance > maxDistance) {
 farthestPointIndex = i;
 maxDistance = distance;
 }
 }
 }
 return farthestPointIndex;
 }

}