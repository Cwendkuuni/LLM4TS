package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = null;
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Void();
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_String("foo");
    cost = InlineCostEstimator.getCost(root);
    assertEquals(3, cost);

    root = new Node_Int(123);
    cost = InlineCostEstimator.getCost(root);
    assertEquals(1, cost);

    root = new Node_Bool(true);
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Null();
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = null;

    // Test with no threshold
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Void();
    cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(0, cost);

    root = new Node_String("foo");
    cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(3, cost);

    root = new Node_Int(123);
    cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(1, cost);

    root = new Node_Bool(true);
    cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(0, cost);

    root = new Node_Null();
    cost = InlineCostEstimator.getCost(root, 1);
    assertEquals(0, cost);

    // Test with threshold
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(0, cost);

    root = new Node_String("foo");
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(3, cost);

    root = new Node_Int(123);
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(1, cost);

    root = new Node_Bool(true);
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(0, cost);

    root = new Node_Null();
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(0, cost);

    root = new Node_String("foo bar");
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(5, cost);

    root = new Node_Int(12345);
    cost = InlineCostEstimator.getCost(root, 3);
    assertEquals(5, cost);

    root = new Node_Bool(true);
    Node left = new Node_Bool(false);
    Node right = new Node_Bool(false);
    Node condition = new Node_BinaryOp("&&", left, right);
    Node ifStatement = new Node_If(condition, new Node_Void(), root);
    cost = InlineCostEstimator.getCost(ifStatement, 3);
    assertEquals(6, cost); // &&, (), If, Void
  }

  @Test
  public void testCompiledSizeEstimator() {
    Node root = null;

    // Test with no threshold
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_Void();
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_String("foo");
    estimator.add(root);
    assertEquals(3, estimator.getCost());

    root = new Node_Int(123);
    estimator.add(root);
    assertEquals(1, estimator.getCost());

    root = new Node_Bool(true);
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_Null();
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_String("foo bar baz");
    estimator.add(root);
    assertEquals(10, estimator.getCost());

    root = new Node_Int(12345);
    estimator.add(root);
    assertEquals(5, estimator.getCost());

    root = new Node_Bool(true);
    Node left = new Node_Bool(false);
    Node right = new Node_Bool(false);
    Node condition = new Node_BinaryOp("&&", left, right);
    Node ifStatement = new Node_If(condition, new Node_Void(), root);
    estimator.add(ifStatement);
    assertEquals(6, estimator.getCost()); // &&, (), If, Void

    // Test with threshold
    estimator = new CompiledSizeEstimator(5);
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_String("foo");
    estimator.add(root);
    assertEquals(3, estimator.getCost());

    root = new Node_Int(123);
    estimator.add(root);
    assertEquals(1, estimator.getCost());

    root = new Node_Bool(true);
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_Null();
    estimator.add(root);
    assertEquals(0, estimator.getCost());

    root = new Node_String("foo bar");
    estimator.add(root);
    assertEquals(5, estimator.getCost());

    root = new Node_Int(12345);
    estimator.add(root);
    assertEquals(5, estimator.getCost());

    root = new Node_Bool(true);
    left = new Node_Bool(false);
    right = new Node_Bool(false);
    condition = new Node_BinaryOp("&&", left, right);
    ifStatement = new Node_If(condition, new Node_Void(), root);
    estimator.add(ifStatement);
    assertEquals(6, estimator.getCost()); // &&, (), If, Void

    estimator.continueProcessing = false;
    estimator.add(root);
    assertTrue(estimator.continueProcessing);
    assertEquals(6, estimator.getCost()); // &&, (), If, Void

    estimator.continueProcessing = true;
    estimator.add(root);
    assertEquals(6, estimator.getCost()); // &&, (), If, Void

    estimator.last = 'a';
    estimator.add(new Node_String("foo"));
    assertEquals(9, estimator.getCost()); // &&, (), If, Void, fo
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.addIdentifier("identifier");
    assertEquals(ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testContinueProcessing() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    assertTrue(estimator.continueProcessing());

    estimator.addIdentifier("identifier");
    assertFalse(estimator.continueProcessing());
  }
}