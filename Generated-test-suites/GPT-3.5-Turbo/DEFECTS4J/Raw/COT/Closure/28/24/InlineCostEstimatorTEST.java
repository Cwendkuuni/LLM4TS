import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    // Test case 1
    Node root1 = new Node();
    int cost1 = InlineCostEstimator.getCost(root1);
    assertEquals(0, cost1);
    
    // Test case 2
    Node root2 = new Node();
    int cost2 = InlineCostEstimator.getCost(root2, 100);
    assertEquals(0, cost2);
  }
  
  @Test
  public void testGetCost_thresholdExceeded() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(0, cost);
  }
  
  @Test
  public void testAdd() {
    InlineCostEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    Node root = new Node();
    estimator.add(root);
    assertEquals(0, estimator.getCost());
  }
  
  @Test
  public void testGetCostOfEmptyString() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("");
    assertEquals(0, estimator.getCost());
  }
  
  @Test
  public void testGetCostOfString() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("Test");
    assertEquals(4, estimator.getCost());
  }
  
  @Test
  public void testGetCostOfIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.addIdentifier("identifier");
    assertEquals(2, estimator.getCost());
  }
  
  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("Test");
    char lastChar = estimator.getLastChar();
    assertEquals('t', lastChar);
  }
  
  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator1 = new InlineCostEstimator.CompiledSizeEstimator(100);
    assertTrue(estimator1.continueProcessing());
    
    InlineCostEstimator.CompiledSizeEstimator estimator2 = new InlineCostEstimator.CompiledSizeEstimator(0);
    assertFalse(estimator2.continueProcessing());
  }
}