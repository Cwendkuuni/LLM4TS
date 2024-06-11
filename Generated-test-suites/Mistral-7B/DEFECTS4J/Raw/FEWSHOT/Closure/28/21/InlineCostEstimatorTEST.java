package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(); // empty node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(); // empty node
    int costThreshold = 5;
    int cost = InlineCostEstimator.getCost(root, costThreshold);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddNode() {
    Node root = new Node(); // empty node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddString() {
    Node root = new Node(); // empty node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    String str = "abcdef";
    estimator.add(new Node("StringLiteral", null, null, str));
    int cost = estimator.getCost();
    assertEquals(6, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    Node root = new Node(); // empty node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    String identifier = "identifier";
    estimator.addIdentifier(identifier);
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostReached() {
    Node root = new Node(); // empty node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(2);
    String str1 = "abc";
    String str2 = "def";
    estimator.add(new Node("StringLiteral", null, null, str1));
    estimator.addIdentifier("identifier1");
    estimator.add(new Node("StringLiteral", null, null, str2));
    estimator.addIdentifier("identifier2");
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostNotReached() {
    Node root = new Node(); // empty node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    String str1 = "abc";
    String str2 = "def";
    estimator.add(new Node("StringLiteral", null, null, str1));
    estimator.addIdentifier("identifier1");
    estimator.add(new Node("StringLiteral", null, null, str2));
    estimator.addIdentifier("identifier2");
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST * 3 + 6, cost);
  }

  @Test(expected = NullPointerException.class)
  public void testCompiledSizeEstimatorNullNode() {
    Node root = null;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
  }
}