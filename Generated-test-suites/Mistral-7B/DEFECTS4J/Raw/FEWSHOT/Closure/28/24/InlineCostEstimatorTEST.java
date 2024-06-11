package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("program", new Node[0]);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("program", new Node[0]);
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithIdentifiers() {
    String identifier = "identifier";
    Node root = new Node("program", new Node[] { new Node("var", new Node[] { new Node("id", identifier) }) });
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testContinueProcessing() {
    String longString = "a" + new String(new char[1000]) + "b";
    Node root = new Node("program", new Node[] { new Node("var", new Node[] { new Node("id", "identifier") }) });
    root = new Node("ExpressionStatement", new Node[] {
        new Node("Assign", new Node[] { root, new Node("StringLiteral", longString) })
    });
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    assertTrue(estimator.continueProcessing());
    assertEquals(longString.length(), estimator.cost);
  }

  @Test
  public void testContinueProcessingFalse() {
    Node root = new Node("program", new Node[0]);
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
    estimator.add(root);
    estimator.add(new Node("StringLiteral", "longString"));
    assertFalse(estimator.continueProcessing());
  }
}