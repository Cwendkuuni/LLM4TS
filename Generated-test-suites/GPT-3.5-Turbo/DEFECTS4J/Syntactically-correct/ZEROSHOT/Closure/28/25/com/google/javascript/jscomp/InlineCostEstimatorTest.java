import org.junit.Test;
import static org.junit.Assert.*;

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
  public void testCompiledSizeEstimatorAdd() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
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
  public void testCompiledSizeEstimatorContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    boolean continueProcessing = estimator.continueProcessing();
    assertEquals(true, continueProcessing);
  }
  
  @Test
  public void testCompiledSizeEstimatorGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    char lastChar = estimator.getLastChar();
    assertEquals('\0', lastChar);
  }
  
  @Test
  public void testCompiledSizeEstimatorAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("test");
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.addIdentifier("test");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
}