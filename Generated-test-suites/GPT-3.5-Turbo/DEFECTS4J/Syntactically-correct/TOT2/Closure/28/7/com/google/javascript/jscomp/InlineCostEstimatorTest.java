import static org.junit.Assert.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Typical use case
    Node root = new Node(NodeType.METHOD, "testMethod");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(4, cost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    // Edge case: cost threshold reached
    Node root = new Node(NodeType.FUNCTION, "testFunction");
    int cost = InlineCostEstimator.getCost(root, 5);
    assertEquals(5, cost);
  }

  @Test
  public void testGetCostWithLargeNode() {
    // Edge case: large node
    Node root = new Node(NodeType.BLOCK, "testBlock");
    for (int i = 0; i < 100; i++) {
      root.addChildToBack(new Node(NodeType.VARIABLE, "var" + i));
    }
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(208, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    Node root = new Node(NodeType.METHOD, "testMethod");
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }

  @Test
  public void testCompiledSizeEstimatorGetCost() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(50);
    estimator.addIdentifier("testIdentifier");
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }
}