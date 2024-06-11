import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostWithCostThreshhold() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, 100);
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    Node root = new Node();
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorGetCost() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
  
  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    boolean continueProcessing = estimator.continueProcessing();
    assertTrue(continueProcessing);
  }
  
  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    char lastChar = estimator.getLastChar();
    assertEquals('\0', lastChar);
  }
  
  @Test
  public void testAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("abc");
    int cost = estimator.getCost();
    assertEquals(3, cost);
  }
  
  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
}