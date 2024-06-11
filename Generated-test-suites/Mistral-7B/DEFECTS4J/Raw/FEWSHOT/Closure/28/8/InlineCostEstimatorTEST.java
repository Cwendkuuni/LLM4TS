package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_EmptyNode() {
    Node root = new Node_();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_SimpleNode() {
    Node root = new NumberLiteral(42);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(1, cost);
  }

  @Test
  public void testGetCost_ComplexNode() {
    String complexCode = "function foo(a, b) {" +
        "var x = a * b;" +
        "return x * 2; }";
    Node root = Parser.parse(complexCode);
    int cost = InlineCostEstimator.getCost(root, 100);
    assertEquals(23, cost);
  }

  @Test
  public void testGetCost_LargeNode() {
    String largeCode = "function foo(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) {" +
        "var a1 = a * b;" +
        "var a2 = a1 + c;" +
        "var a3 = a2 * d;" +
        "var a4 = a3 + e;" +
        "var a5 = a4 * f;" +
        "var a6 = a5 + g;" +
        "var a7 = a6 * h;" +
        "var a8 = a7 + i;" +
        "var a9 = a8 * j;" +
        "var a10 = a9 + k;" +
        "var a11 = a10 * l;" +
        "var a12 = a11 + m;" +
        "var a13 = a12 * n;" +
        "var a14 = a13 + o;" +
        "var a15 = a14 * p;" +
        "var a16 = a15 + q;" +
        "var a17 = a16 * r;" +
        "var a18 = a17 + s;" +
        "var a19 = a18 * t;" +
        "var a20 = a19 + u;" +
        "var a21 = a20 * v;" +
        "var a22 = a21 + w;" +
        "var a23 = a22 * x;" +
        "var a24 = a23 + y;" +
        "var a25 = a24 * z;" +
        "return a25; }";
    Node root = Parser.parse(largeCode);
    int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    assertEquals(252, cost); // The actual number may vary based on the length of the estimated identifier
  }

  @Test
  public void testGetCost_CostThreshold() {
    String complexCode = "function foo(a, b) {" +
        "var x = a * b;" +
        "return x * 2; }";
    Node root = Parser.parse(complexCode);
    int cost = InlineCostEstimator.getCost(root, 50);
    assertEquals(23, cost);
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier1");
    estimator.addIdentifier("identifier2");
    int cost = estimator.getCost();
    assertEquals(10, cost);
  }

  @Test
  public void testAddIdentifier_MultipleIdentifiers() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(50);
    String identifier = "longIdentifier";
    for (int i = 0; i < 20; i++) {
      estimator.addIdentifier(identifier);
    }
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER.length() * 20 + identifier.length() * 18, cost); // Assuming 18 characters for the estimated cost of the longest identifier
  }
}