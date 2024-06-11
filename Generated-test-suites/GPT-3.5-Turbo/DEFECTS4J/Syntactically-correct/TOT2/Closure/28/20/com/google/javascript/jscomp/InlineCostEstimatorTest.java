import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("root");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    Node root = new Node("root");
    int cost = InlineCostEstimator.getCost(root, 50);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    Node root = new Node("root");
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorGetCost() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAppend() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.append("test");
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }

}