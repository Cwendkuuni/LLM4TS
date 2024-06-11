package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_EmptyNode() {
    Node root = Node.NULL();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_SimpleNode() {
    Node root = Node.ident("x");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCost_ComplexNode() {
    String complexCode = "function foo(x, y) {" +
        "var z = x * y;" +
        "return z + 42;" +
        "}";
    Node root = ParserTestUtil.parseNode(complexCode);
    int cost = InlineCostEstimator.getCost(root);
    // Assuming each identifier is 2 characters, each binary operator is 1 character, and each parenthesis is 1 character.
    // The actual cost may vary depending on the specific implementation of the Parser and CodeGenerator.
    int expectedCost = 12 + 3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST + 3; // 12 for function keyword, 3 identifiers, and 3 binary operators
    assertEquals(expectedCost, cost);
  }

  @Test
  public void testGetCostWithThreshold_SimpleNode() {
    Node root = Node.ident("x");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(1);
    InlineCostEstimator.getCost(root, 1, estimator);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testGetCostWithThreshold_ComplexNode() {
    String complexCode = "function foo(x, y) {" +
        "var z = x * y;" +
        "return z + 42;" +
        "}";
    Node root = ParserTestUtil.parseNode(complexCode);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    InlineCostEstimator.getCost(root, 10, estimator);
    int expectedCost = 12 + 3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST + 3; // 12 for function keyword, 3 identifiers, and 3 binary operators
    assertEquals(expectedCost, estimator.getCost());
  }

  @Test
  public void testGetCostWithThreshold_ExceedThreshold() {
    String complexCode = "function foo(x, y, z, w, v, u, t, s, r, q, p, o, n, m, l, k, j, i, h, g, f, e, d, c, b, a) {" +
        "var sum = 0;" +
        "for (var i = 0; i < a.length; i++) {" +
        " sum += a[i] * b[i] *" +
        " (c[i] + d[i]) *" +
        " (e[i] && f[i] || g[i] && h[i]);" +
        "}" +
        "return sum;" +
        "}";
    Node root = ParserTestUtil.parseNode(complexCode);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    InlineCostEstimator.getCost(root, 10, estimator);
    assertNotEquals(10, estimator.getCost()); // The code exceeds the threshold, so the cost estimator should continue processing and return a higher cost.
  }
}