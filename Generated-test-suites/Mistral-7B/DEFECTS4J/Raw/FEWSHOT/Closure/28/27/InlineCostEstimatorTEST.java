package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.IDENT("identifier");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.IDENT("identifier1");
    Node root2 = Node.IDENT("identifier2");
    Node root3 = Node.IDENT("identifier3");

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
    estimator.add(root2);
    estimator.add(root3);

    int cost = estimator.getCost();
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, cost);
  }

  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.add(Node.IDENT("identifier1"));
    estimator.add(Node.IDENT("identifier2"));
    estimator.add(Node.IDENT("identifier3"));

    assertTrue(estimator.continueProcessing());

    estimator.add(Node.IDENT("identifier4"));
    estimator.add(Node.IDENT("identifier5"));
    estimator.add(Node.IDENT("identifier6"));

    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.add(Node.IDENT("identifier1"));

    estimator.append("someString");

    assertEquals('t', estimator.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.addIdentifier("identifier1");

    estimator.append("someString");

    estimator.append("someOtherString");

    assertEquals('O', estimator.getLastChar());
  }

  @Test
  public void testAppendNull() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.addIdentifier("identifier1");

    estimator.append(null);

    estimator.append("someString");

    assertNull(estimator.getLastChar());
  }

  @Test
  public void testAppendEmptyString() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.addIdentifier("identifier1");

    estimator.append("");

    estimator.append("someString");

    assertEquals('s', estimator.getLastChar());
  }
}