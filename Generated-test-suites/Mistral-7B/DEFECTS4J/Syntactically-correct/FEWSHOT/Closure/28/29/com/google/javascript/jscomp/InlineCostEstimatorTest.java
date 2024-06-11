package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newIdent("root");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.newText("root text");
    Node child1 = Node.newIdent("child1");
    Node child2 = Node.newText("child2 text");

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.add(child1);
    estimator.add(child2);

    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST * 3 + child2.getText().length(), cost);
  }

  @Test
  public void testAddNode() {
    Node root = Node.newText("root text");
    Node child1 = Node.newIdent("child1");
    Node child2 = Node.newText("child2 text");

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.add(child1);
    estimator.add(child2);

    assertEquals(ESTIMATED_IDENTIFIER_COST * 3 + child2.getText().length(), estimator.getCost());
  }

  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(Node.newText("a"));
    estimator.add(Node.newText("b"));
    estimator.add(Node.newText("c"));

    assertNotEquals(0, estimator.getCost());
  }

  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(Node.newText("abcdef"));

    assertEquals('f', estimator.getLastChar());
  }
}