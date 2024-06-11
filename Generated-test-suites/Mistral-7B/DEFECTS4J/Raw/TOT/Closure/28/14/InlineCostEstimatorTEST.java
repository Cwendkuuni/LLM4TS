import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Tests for InlineCostEstimator class.
 */
public class InlineCostEstimatorTest {

  @Test
  public void testGetCostWithRootNode() {
    Node root = Node.objectLiteral(); // empty object
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithSimpleExpression() {
    Node root = Node.literal(true);
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithComplexExpression() {
    String code = "function foo(a, b) {" +
                  "  var c = a * b;" +
                  "  return c + 1;" +
                  "}";
    Node root = Node.parse(code);
    int cost = getCost(root);
    int expectedCost = 14; // (function, var, *, *, *, return, +)
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    int costThreshhold = -1;
    Node root = Node.literal(1);
    try {
      getCost(root, costThreshhold);
    } catch (IllegalArgumentException e) {
      assertEquals("Cost threshold must be non-negative", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testGetCostWithLargeCostThreshold() {
    int costThreshhold = 1000;
    String code = "function foo(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) {" +
                  "  // some complex calculations here" +
                  "  return 42;" +
                  "}";
    Node root = Node.parse(code);
    int cost = getCost(root, costThreshhold);
    // This cost is not deterministic as it depends on the actual length of generated code
    // but it should be greater than 43 (function + return)
    assertTrue(cost > 43);
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("a");
    estimator.addIdentifier("b");
    int cost = estimator.getCost();
    int expectedCost = 6; // 2 identifiers
    assertEquals(expectedCost, cost);
  }

  @Test
  public void testAddComplexIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("thisIsALongIdentifierThatShouldBeShortened");
    int cost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 2 + 1; // 2 identifiers + 1 constant
    assertEquals(expectedCost, cost);
  }
}