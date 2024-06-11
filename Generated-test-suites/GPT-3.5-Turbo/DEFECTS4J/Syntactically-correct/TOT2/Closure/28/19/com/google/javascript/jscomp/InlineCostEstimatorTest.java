import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, 100);
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimator() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    assertEquals(0, estimator.getCost());
  }
}