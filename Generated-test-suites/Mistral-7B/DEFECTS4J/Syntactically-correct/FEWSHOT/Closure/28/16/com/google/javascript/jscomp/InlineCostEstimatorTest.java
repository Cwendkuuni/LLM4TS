package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator.CompiledSizeEstimator;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(); // An empty Node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    // Test with a simple Node
    String identifier = "identifier";
    Node identifierNode = new Node_Name(identifier);
    cost = InlineCostEstimator.getCost(identifierNode, 10);
    assertEquals(identifier.length() + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);

    // Test with a complex Node
    Node complexNode = new Node_Call(Identifier.create("someFunction")),
        argumentsNode = new Node_Array();
    Node argument1 = new Node_Number(1);
    Node argument2 = new Node_String("string");
    argumentsNode.appendChild(argument1);
    argumentsNode.appendChild(argument2);
    complexNode.setChild(0, argumentsNode);
    cost = InlineCostEstimator.getCost(complexNode, 100);
    int expectedCost = "someFunction".length() + 1 + // Function name
        argumentsNode.getChildCount() * (InlineCostEstimator.ESTIMATED_IDENTIFIER_COST + 1) + // Arguments identifiers
        "(".length() + // Open parenthesis
        ")".length(); // Close parenthesis
    assertEquals(expectedCost, cost);
  }

  @Test
  public void testGetCostWithMaxCost() {
    Node root = new Node_ExpressionStatement(new Node_Call(Identifier.create("largestFunctionName")));

    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1000);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(1 + "largestFunctionName".length(), cost);
  }

  @Test
  public void testContinueProcessing() {
    Node root = new Node_ExpressionStatement(new Node_Call(Identifier.create("largestFunctionName")));

    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    assertTrue(estimator.continueProcessing());

    estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(root);
    assertFalse(estimator.continueProcessing());
  }
}