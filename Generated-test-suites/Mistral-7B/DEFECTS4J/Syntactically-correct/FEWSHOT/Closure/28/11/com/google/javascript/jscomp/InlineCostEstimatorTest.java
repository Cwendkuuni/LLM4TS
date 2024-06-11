package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int MAX_COST = 10;

 @Test
 public void testGetCostRoot() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node root = null; // test case for null root
 int cost = InlineCostEstimator.getCost(root);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostRootEmpty() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node root = new Node_Empty();
 int cost = InlineCostEstimator.getCost(root, MAX_COST);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostSimpleNode() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node node = new Node_Literal(123);
 int cost = InlineCostEstimator.getCost(node, MAX_COST);
 assertEquals(3, cost); // 1 for "Node_Literal", 2 for '(' and ')'
 }

 @Test
 public void testGetCostStatement() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node statement = new Node_Statement(new Node_ExpressionStatement(new Node_Assignment(new Node_Identifier("x"), new Node_Literal(42))));
 int cost = InlineCostEstimator.getCost(statement, MAX_COST);
 assertEquals(8, cost); // 2 for "Node_Statement", 1 for "Node_ExpressionStatement", 1 for "Node_Assignment", 1 for "Node_Identifier", 3 for "Node_Literal"
 }

 @Test
 public void testGetCostComplexNode() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node complexNode = new Node_BinaryOp(new Node_Identifier("x"), Node_Type.ADD, new Node_BinaryOp(new Node_Literal(1), Node_Type.SUB, new Node_Identifier("y")));
 int cost = InlineCostEstimator.getCost(complexNode, MAX_COST);
 assertEquals(10, cost); // 1 for "Node_BinaryOp", 1 for "Node_Identifier", 3 for "Node_Literal", 1 for '+', 1 for '-'
 }

 @Test
 public void testGetCostWithThreshold() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node root = new Node_Statement(new Node_ExpressionStatement(new Node_Assignment(new Node_Identifier("x"), new Node_BinaryOp(new Node_Identifier("a"), Node_Type.ADD, new Node_Identifier("b")))));
 int cost = InlineCostEstimator.getCost(root, MAX_COST / 2);
 assertEquals(5, cost); // 2 for "Node_Statement", 1 for "Node_ExpressionStatement", 1 for "Node_Assignment", 1 for "Node_Identifier", 1 for "Node_BinaryOp"
 }

 @Test (expected = RuntimeException.class)
 public void testGetCostExceedingThreshold() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node root = new Node_Statement(new Node_ExpressionStatement(new Node_Assignment(new Node_Identifier("x"), new Node_BinaryOp(new Node_Identifier("a"), Node_Type.ADD, new Node_BinaryOp(new Node_Identifier("b"), Node_Type.ADD, new Node_Literal(100))))));
 int cost = InlineCostEstimator.getCost(root, MAX_COST);
 }

 @Test
 public void testCompiledSizeEstimator() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 CompiledSizeEstimator compiledEstimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 Node node = new Node_Literal(123);
 compiledEstimator.add(node);
 int cost = compiledEstimator.getCost();
 assertEquals(3, cost); // 1 for "Node_Literal", 2 for '(' and ')'
 }

 @Test
 public void testCompiledSizeEstimatorAddIdentifier() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 CompiledSizeEstimator compiledEstimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 String identifier = "identifier";
 compiledEstimator.addIdentifier(identifier);
 int cost = compiledEstimator.getCost();
 assertEquals(identifier.length() + 2, cost); // 1 for "addIdentifier" and 1 for '(' and ')'
 }
}