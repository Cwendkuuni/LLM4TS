package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {
  @Test
  public void testGetCost() {
    Node root = null; // an empty Node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Literal(Node.NNULL); // null node
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Identifier("ident"); // simple identifier node
    cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);

    // complex expression: 1 + 2 * 3
    Node add = new Node_BinaryOp("+", new Node_NumberLiteral(1), new Node_BinaryOp("*", new Node_NumberLiteral(2), new Node_NumberLiteral(3)));
    cost = InlineCostEstimator.getCost(add, 100);
    assertEquals(6, cost); // 2 identifiers + 3 operators + 1 null terminator
  }

  @Test
  public void testCompiledSizeEstimator() {
    String input = "function foo(){\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var z = x + y * 3;\n" +
        "  return z;\n" +
        "}";

    Node root = AbstractSyntaxTree.parseJavaScript(input);
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.add(root);

    int cost = estimator.getCost();
    assertEquals(37, cost); // ~30 identifiers + 6 operators + 1 function name
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node_BinaryOp("+", new Node_NumberLiteral(1), new Node_BinaryOp("*", new Node_NumberLiteral(2), new Node_NumberLiteral(3)));
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(6, cost); // 2 identifiers + 3 operators + 1 null terminator

    cost = InlineCostEstimator.getCost(root, 20);
    assertEquals(6, cost); // no change, threshold not reached

    cost = InlineCostEstimator.getCost(root, 30);
    assertEquals(6, cost); // no change, threshold still not reached

    cost = InlineCostEstimator.getCost(root, 100);
    assertEquals(6, cost); // no change, threshold still not reached

    cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    assertEquals(6, cost); // no change, threshold is infinite
  }
}