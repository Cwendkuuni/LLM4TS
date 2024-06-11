package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = null; // Validate the case when root is null
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node_Literal(Node.NOP); // Validate the case when root is an empty Node
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    // Test the getCost() method with a sample Node
    Node sampleNode = new Node_ExpressionStatement(new Node_Assignment(new Node_Name("x"), new Node_NumberLiteral(42)));
    cost = InlineCostEstimator.getCost(sampleNode);
    assertEquals(12, cost); // Estimated cost: 1 identifier (x) + 3 characters for "=" + 6 characters for "42" + 1 character for ";"
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = null; // Validate the case when root is null
    int costThreshold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshold);
    assertEquals(0, cost);

    root = new Node_Literal(Node.NOP); // Validate the case when root is an empty Node
    cost = InlineCostEstimator.getCost(root, costThreshold);
    assertEquals(0, cost);

    // Test the getCost() method with a sample Node and a cost threshold
    Node sampleNode = new Node_ExpressionStatement(new Node_Assignment(new Node_Name("x"), new Node_NumberLiteral(42)));
    cost = InlineCostEstimator.getCost(sampleNode, costThreshold);
    assertEquals(12, cost); // Estimated cost: 1 identifier (x) + 3 characters for "=" + 6 characters for "42" + 1 character for ";"

    // Test the case when the cost threshold is reached
    Node bigNode = new Node_Block(new Node_StatementList(), new Node_StatementList());
    for (int i = 0; i < costThreshold; i++) {
      bigNode.statements.add(new Node_ExpressionStatement(new Node_Assignment(new Node_Name("y" + i), new Node_NumberLiteral(i))));
    }
    cost = InlineCostEstimator.getCost(bigNode, costThreshold);
    assertEquals(costThreshold, cost);
  }
}