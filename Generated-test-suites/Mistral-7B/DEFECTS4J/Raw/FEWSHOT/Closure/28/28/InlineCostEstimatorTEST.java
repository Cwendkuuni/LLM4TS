package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    Node root = new Node(1, null, null);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostNull() {
    int cost = InlineCostEstimator.getCost(null);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostCostThreshold() {
    Node root = new Node(1, null, null);
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostCostThresholdExceeded() {
    String longString = "a".repeat(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 100);
    Node root = new Node(1, new Node("S", null, longString), null);
    int costThreshhold = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertTrue(cost > costThreshhold);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(new Node(1, null, "abc"));
    assertEquals(3, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("ab");
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorContinueProcessing() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(new Node(1, null, "abcd"));
    estimator.addIdentifier("ef");
    estimator.addIdentifier("gh");
    assertTrue(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostReached() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(new Node(1, null, "abcd"));
    estimator.addIdentifier("ef");
    estimator.addIdentifier("gh");
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorLastChar() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
    estimator.add(new Node(1, null, "abcd"));
    assertEquals('d', estimator.getLastChar());
  }
}