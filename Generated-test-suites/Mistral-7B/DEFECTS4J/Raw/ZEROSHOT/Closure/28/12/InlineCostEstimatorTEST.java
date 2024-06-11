import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newObject("{}");
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.newObject("{}");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testSimpleIdentifier() {
    Node root = Node.newIdentifier("a");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testMultipleIdentifiers() {
    Node root = Node.newObject("{a:1,b:2,c:3}");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(3 * ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testComplexExpression() {
    Node root = Node.newExpression("(a + b * c) / (d - e) * f");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    estimator.add(root);
    int cost = estimator.getCost();
    // Estimation is not accurate for complex expressions
    // Increase threshold to see more characters being counted
    assertEquals(14, cost);
  }

  @Test(expected = IllegalStateException.class)
  public void testCostThresholdExceeded() {
    Node root = Node.newObject("new Array(100000)");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
  }
}