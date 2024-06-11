package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Nodeops;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfRootNode() {
    String jsCode = "function test() {}";
    Node rootNode = Nodeops.parseTemplate(jsCode);
    int cost = InlineCostEstimator.getCost(rootNode);
    // Assuming the estimated identifier cost is 2, the length of jsCode is 23,
    // and the root node itself has a cost of 1, the expected cost should be 24.
    assertEquals("Test case failed: Incorrect cost estimation for root node.", 24, cost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    String jsCode = "true";
    Node rootNode = Nodeops.parseTemplate(jsCode);
    int cost = InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);
    assertEquals("Test case failed: Incorrect cost estimation for simple expression.", 1, cost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String jsCode = "function test(a, b, c) { return a + b * c; }";
    Node rootNode = Nodeops.parseTemplate(jsCode);
    int cost = InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);
    // Assuming the estimated identifier cost is 2, the length of jsCode is 53,
    // and the root node itself has a cost of 1, the expected cost should be 55.
    assertEquals("Test case failed: Incorrect cost estimation for complex expression.", 55, cost);
  }

  @Test
  public void testGetCostOfEmptyStatement() {
    String jsCode = "";
    Node rootNode = Nodeops.parseTemplate(jsCode);
    int cost = InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);
    assertEquals("Test case failed: Incorrect cost estimation for empty statement.", 0, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    String jsCode = "function test() {}";
    Node rootNode = Nodeops.parseTemplate(jsCode);
    InlineCostEstimator.getCost(rootNode, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNullRootNode() {
    InlineCostEstimator.getCost(null, Integer.MAX_VALUE);
  }
}