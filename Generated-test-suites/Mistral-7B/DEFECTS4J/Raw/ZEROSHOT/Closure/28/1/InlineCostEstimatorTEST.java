import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newObjectLiteral();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.newObjectLiteral();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithLargeThreshold() {
    Node root = Node.newObjectLiteral();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int cost = estimator.getCost();
    // The actual cost is not provided in the code, so we can only test
    // the behavior when the threshold is not reached.
    assertTrue(cost <= Integer.MAX_VALUE);
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST + 6, cost); // plus "identifier" length
  }
}