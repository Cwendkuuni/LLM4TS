import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newObject();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.newObject();
    int cost = getCost(root, 10);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithComplexNode() {
    Node complexNode = Node.newObject().setProperty("prop", Node.newNumber(1));
    int cost = getCost(complexNode, 100);
    // The actual cost depends on the implementation of CompiledSizeEstimator,
    // but it should be greater than 0.
    assertTrue(cost > 0);
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("test");
    int cost = estimator.getCost();
    assertEquals(6, cost); // 2 for "ab", 2 for "te", 1 for "s", 1 for ""
  }

  @Test
  public void testAddComplexNode() {
    Node complexNode = Node.newObject().setProperty("prop", Node.newNumber(1));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(complexNode);
    int cost = estimator.getCost();
    // The actual cost depends on the implementation of CompiledSizeEstimator,
    // but it should be greater than 0.
    assertTrue(cost > 0);
  }
}