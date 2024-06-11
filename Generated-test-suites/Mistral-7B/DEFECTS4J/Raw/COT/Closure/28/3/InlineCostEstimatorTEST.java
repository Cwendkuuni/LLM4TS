import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("{}"); // Empty JS object
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("function test() {}");
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(3, cost); // Function keyword, identifier, opening and closing braces
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("test");
    int cost = estimator.getCost();
    assertEquals(5, cost); // EstimatedIdentifier + 2 for "test"
  }

  @Test
  public void testCompiledSizeEstimatorAddString() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add("test");
    int cost = estimator.getCost();
    assertEquals(4, cost); // 4 for the string "test"
  }

  @Test(expected = AssertionError.class)
  public void testCompiledSizeEstimatorExceedThreshold() {
    Node root = new Node("function test() { return 'test'; }");
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
  }

  @Test
  public void testCompiledSizeEstimatorIsProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("test");
    assertFalse(estimator.continueProcessing());
  }
}