package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimator() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddString() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String str = "test";
    estimator.add(root);
    estimator.append(str);
    int cost = estimator.getCost();
    assertEquals(3, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(5, cost);
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostReached() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(2);
    String str = "test";
    estimator.add(root);
    estimator.append(str);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(5, cost);
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorContinueProcessing() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String str1 = "test1";
    String str2 = "test2";
    estimator.add(root);
    estimator.append(str1);
    estimator.addIdentifier("identifier1");
    estimator.append(str2);
    estimator.addIdentifier("identifier2");
    int cost = estimator.getCost();
    assertEquals(12, cost);
    assertTrue(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorNullInput() {
    Node root = null;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    int cost = estimator.getCost();
    assertNull(cost);
  }
}