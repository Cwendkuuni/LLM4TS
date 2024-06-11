import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @org.junit.Test
  public void testGetCost() {
    Node root = null; // edge case: null input
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node(""); // empty Node input
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node("some code"); // simple Node input
    cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(11, cost); // 11 characters in "some code" + 1 for the identifier

    root = new Node("if (true) { var x = 1; }"); // if statement
    cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(22, cost); // 20 characters in "if (true) { var x = 1; }" + 2 for "if" and "{"
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetCostNegativeThreshold() {
    Node root = new Node("some code");
    InlineCostEstimator.getCost(root, -1);
  }

  @org.junit.Test
  public void testCompiledSizeEstimator() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(new Node("some code"));
    estimator.add(new Node("some other code"));
    assertEquals(22, estimator.getCost()); // 22 characters in "some code" and "some other code"
  }

  @org.junit.Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
    assertEquals(9, estimator.getCost()); // 8 characters in "identifier" + 1 for the identifier
  }

  @org.junit.Test
  public void testAddLongIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("longLongIdentifier");
    assertEquals(20, estimator.getCost()); // 18 characters in "longLongIdentifier" + 2 for the identifier
  }
}