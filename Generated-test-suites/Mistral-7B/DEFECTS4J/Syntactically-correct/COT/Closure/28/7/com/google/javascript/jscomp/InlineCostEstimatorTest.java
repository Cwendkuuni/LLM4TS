import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(""); // an empty node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node("some code");
    cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(12, cost); // 10 for "some" and 2 for "code"
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("some long code that will be truncated");
    InlineCostEstimator estimator = new InlineCostEstimator().getCost(root, 10);
    assertEquals(12, estimator.getCost()); // 10 for "some" and 2 for "code"
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    Node root = new Node("some code");
    InlineCostEstimator.getCost(root, -1);
  }

  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("ab");
    estimator.addIdentifier("cd");
    estimator.addIdentifier("ef");

    assertTrue(estimator.continueProcessing());

    estimator.addIdentifier("xyz"); // cost exceeds threshold

    assertFalse(estimator.continueProcessing());
  }
}