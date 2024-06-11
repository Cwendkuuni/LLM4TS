import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import java.util.*;

public class KMeansPlusPlusClustererTest {
  
  @Test
  public void testCluster() {
    KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(new Random());
    
    // Test with k=2 and maxIterations = 10
    List<Cluster<Clusterable>> clusters = clusterer.cluster(generateTestData(), 2, 10);
    
    // Assert clusters size is correct
    assertEquals(2, clusters.size());
    
    // Assert all points are assigned to a cluster
    int totalPoints = clusters.get(0).getPoints().size() + clusters.get(1).getPoints().size();
    assertEquals(Points.size(), totalPoints);
    
    // Assert all points are of type Clusterable
    for (Cluster<Clusterable> cluster : clusters) {
      for (Clusterable point : cluster.getPoints()) {
        assertTrue(point instanceof Clusterable);
      }
    }
  }
  
  @Test(expected = ConvergenceException.class)
  public void testClusterEmptyClusterException() {
    Random random = new Random();
    KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(random);
    
    // Test with empty collection of points
    List<Cluster<Clusterable>> clusters = clusterer.cluster(new ArrayList<Clusterable>(), 2, 10);
    
    // Assert exception thrown when clusters are empty
    fail("ConvergenceException was not thrown");
  }
  
  @Test
  public void testChooseInitialCenters() {
    KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(new Random());
    
    // Test choose initial centers with k=2
    List<Cluster<Clusterable>> clusters = clusterer.chooseInitialCenters(generateTestData(), 2, new Random());
    
    // Assert number of clusters is correct
    assertEquals(2, clusters.size());
    
    // Assert all initial centers are of type Clusterable
    for (Cluster<Clusterable> cluster : clusters) {
      assertTrue(cluster.getCenter() instanceof Clusterable);
    }
  }
  
  @Test
  public void testAssignPointsToClusters() {
    KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(new Random());
    List<Cluster<Clusterable>> clusters = clusterer.chooseInitialCenters(generateTestData(), 2, new Random());
    Collection<Clusterable> points = generateTestData();
    
    // Assign points to clusters
    clusterer.assignPointsToClusters(clusters, points);
    
    // Assert all points have been assigned to a cluster
    int totalPoints = clusters.get(0).getPoints().size() + clusters.get(1).getPoints().size();
    assertEquals(Points.size(), totalPoints);
    
    // Assert all points are of type Clusterable
    for (Cluster<Clusterable> cluster : clusters) {
      for (Clusterable point : cluster.getPoints()) {
        assertTrue(point instanceof Clusterable);
      }
    }
  }
  
  @Test
  public void testGetNearestCluster() {
    KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<Clusterable>(new Random());
    
    List<Cluster<Clusterable>> clusters = clusterer.chooseInitialCenters(generateTestData(), 2, new Random());
    Clusterable point = generateTestData().get(0);
    
    // Get nearest cluster for the point
    Cluster<Clusterable> nearestCluster = clusterer.getNearestCluster(clusters, point);
    
    // Assert the nearest cluster is not null
    assertNotNull(nearestCluster);
    
    // Assert the nearest cluster is of type Cluster
    assertTrue(nearestCluster instanceof Cluster);
  }
  
  private List<Clusterable> generateTestData() {
    // Generate a list of Clusterable data points
    List<Clusterable> testData = new ArrayList<Clusterable>();
    for (int i = 0; i < 100; i++) {
      testData.add(new Clusterable());
    }
    return testData;
  }
  
  private class Clusterable implements Clusterable<Clusterable> {
    // Implementation of Clusterable interface
  }
  
}