package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = null; // test for null input
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node(); // test for empty node
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    String testString = "function testFunction() {}";
    Node testNode = new ParParser().parse(testString);
    cost = InlineCostEstimator.getCost(testNode);
    assertEquals(testString.length(), cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    String testString = "function testFunction1() {} function testFunction2() {} function testFunction3() {}";
    Node testNode = new ParParser().parse(testString);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(testNode);
    int cost = estimator.getCost();
    assertEquals(10, cost);
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.addIdentifier("identifier1");
    estimator.addIdentifier("identifier2");
    estimator.addIdentifier("identifier3");

    int cost = estimator.getCost();
    assertEquals(3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    String testString = "function testFunction() { var a = 1, b = 2, c = 3; }";
    Node testNode = new ParParser().parse(testString);

    estimator.add(testNode);

    assertTrue(estimator.continueProcessing());

    testString += "function testFunction2() { var d = 4; }";
    Node testNode2 = new ParParser().parse(testString);

    estimator.add(testNode2);

    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    String testString = "function testFunction() { var a = 1, b = 2, c = 3; }";
    Node testNode = new ParParser().parse(testString);

    estimator.add(testNode);

    assertEquals(';', estimator.getLastChar());

    testString += "function testFunction2() { var d = 4; }";
    Node testNode2 = new ParParser().parse(testString);

    estimator.add(testNode2);

    assertEquals('\n', estimator.getLastChar());
  }

  @Test
  public void testNullInput() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

    estimator.add(null);

    assertNull(estimator);
  }
}