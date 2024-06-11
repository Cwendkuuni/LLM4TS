package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InlineCostEstimatorTest {
  @Test
  public void testGetCost() {
    Node root = new Node(); // Create an empty Node for testing
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(); // Create an empty Node for testing
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimator() {
    Node root = new Node(); // Create an empty Node for testing
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddString() {
    Node root = new Node(); // Create an empty Node for testing
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String str = "test string";
    estimator.add(new Node("Identifier", "s", str));
    estimator.add(new Node("String", null, str));
    int cost = estimator.getCost();
    assertEquals(str.length(), cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    Node root = new Node(); // Create an empty Node for testing
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String identifier = "testIdentifier";
    estimator.addIdentifier(identifier);
    int cost = estimator.getCost();
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddNodeWithChildren() {
    Node root = new Node(); // Create an empty Node for testing
    Node child1 = new Node("Identifier", "c1");
    Node child2 = new Node("Identifier", "c2");
    Node nodeToAdd = new Node("BinaryOperator", "op", child1, child2);
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(nodeToAdd);
    int cost = estimator.getCost();
    assertEquals(costThreshhold, cost); // The cost threshold should be exceeded
  }
}