package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int TEST_THRESHOLD = 10;

 @Test
 public void testGetCostRoot() {
  Node root = new Node_Program();
  int cost = InlineCostEstimator.getCost(root);
  assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
  Node root = new Node_Program();
  int cost = InlineCostEstimator.getCost(root, TEST_THRESHOLD);
  assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThresholdSimpleNode() {
  Node root = new Node_Identifier("a");
  int cost = InlineCostEstimator.getCost(root, TEST_THRESHOLD);
  assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
 }

 @Test
 public void testGetCostWithThresholdComplexNode() {
  Node root = new Node_BinaryOperator(Node.OP_ADD, new Node_Number(1), new Node_Number(2));
  int cost = InlineCostEstimator.getCost(root, TEST_THRESHOLD);
  assertEquals(1 + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, cost);
 }

 @Test
 public void testGetCostWithThresholdNestedNodes() {
  Node root = new Node_BinaryOperator(Node.OP_ADD, new Node_BinaryOperator(Node.OP_MUL, new Node_Number(1), new Node_Number(2)), new Node_BinaryOperator(Node.OP_SUB, new Node_Number(3), new Node_Number(4)));
  int cost = InlineCostEstimator.getCost(root, TEST_THRESHOLD);
  assertEquals(3 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST + 2 + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, cost);
 }

 @Test
 public void testGetCostWithThresholdLimitReached() {
  String longString = "x".repeat(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * TEST_THRESHOLD);
  Node root = new Node_StringLiteral(longString);
  int cost = InlineCostEstimator.getCost(root, TEST_THRESHOLD);
  assertNotEquals(0, cost);
 }
}