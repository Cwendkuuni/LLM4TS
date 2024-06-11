import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("root");
    int result = InlineCostEstimator.getCost(root);
    assertEquals(0, result);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("root");
    int threshold = 10;
    int result = InlineCostEstimator.getCost(root, threshold);
    assertEquals(0, result);
  }

  @Test
  public void testAddWithCostThreshold() {
    Node root = new Node("root");
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(15);
    estimator.add(root);
    int result = estimator.getCost();
    assertEquals(0, result);
  }
  
  @Test
  public void testLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(15);
    char lastChar = estimator.getLastChar();
    assertEquals('\0', lastChar);
  }

  @Test
  public void testAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(15);
    estimator.append("test");
    int result = estimator.getCost();
    assertEquals(4, result);
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(15);
    estimator.addIdentifier("test");
    int result = estimator.getCost();
    assertEquals(2, result);
  }

}