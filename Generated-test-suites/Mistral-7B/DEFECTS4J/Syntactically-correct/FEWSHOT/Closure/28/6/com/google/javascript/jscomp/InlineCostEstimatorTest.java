package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.objectLiteral();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.objectLiteral();
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimator() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(Node.stringLiteral(""));
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddString() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(Node.stringLiteral("test"));
    int cost = estimator.getCost();
    assertEquals(5, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddStringWithLongLength() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    String longString = "test";
    for (int i = 0; i < 1000; i++) {
      longString += "test";
    }
    estimator.add(Node.stringLiteral(longString));
    int cost = estimator.getCost();
    assertEquals(1005, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(3, cost);
  }

  @Test
  public void testCompiledSizeEstimatorMaxCost() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    estimator.addIdentifier("identifier");
    estimator.addIdentifier("identifier2");
    estimator.addIdentifier("identifier3");
    int cost = estimator.getCost();
    assertEquals(9, cost);
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorNullInput() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    assertNull(estimator.getLastChar());
    estimator.add(null);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
}