import static org.junit.Assert.*;
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
  public void testCompiledSizeEstimatorAdd() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    Node root = new Node();
    estimator.add(root);
    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testAppend() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    estimator.append("test");
    assertEquals(4, estimator.getCost());
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    estimator.addIdentifier("test");
    assertEquals(2, estimator.getCost());
  }

}