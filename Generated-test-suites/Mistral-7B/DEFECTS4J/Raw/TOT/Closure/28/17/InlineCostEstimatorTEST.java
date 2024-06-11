package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleNode() {
    Node root = Node.IDENT("x");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostOfComplexNode() {
    Node root = Node.newObject("Math", Node.newArray(
        Node.IDENT("sin"), Node.NUMBER(Math.PI), Node.IDENT("pow"), Node.NUMBER(2), Node.IDENT("pi")
    ));
    int cost = InlineCostEstimator.getCost(root, 100); // Set a cost threshold for testing
    int expectedCost = 15; // Estimated cost based on the structure of the node
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    Node root = Node.IDENT("x");
    InlineCostEstimator.getCost(root, -1);
  }
}