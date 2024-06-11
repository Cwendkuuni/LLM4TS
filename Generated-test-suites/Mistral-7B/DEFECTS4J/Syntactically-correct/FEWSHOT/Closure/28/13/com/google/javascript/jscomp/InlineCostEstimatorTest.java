package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node createNode(String value) {
 return new Node_LowerCaseExpression_2(value);
 }

 @Test
 public void testGetCost() {
 Node root = new Node_Program();
 root.setBody(new Node_BlockStatement(new Node_ExpressionStatement(createNode("1"))));
 int cost = InlineCostEstimator.getCost(root);
 assertEquals(1, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = new Node_Program();
 root.setBody(new Node_BlockStatement(new Node_ExpressionStatement(createNode("1"))));
 int costThreshold = 10;
 int cost = InlineCostEstimator.getCost(root, costThreshold);
 assertEquals(1, cost);
 }

 @Test
 public void testGetCostWithLargeThreshold() {
 Node root = new Node_Program();
 root.setBody(new Node_BlockStatement(new Node_ExpressionStatement(createNode("1"))));
 int costThreshold = Integer.MAX_VALUE;
 int cost = InlineCostEstimator.getCost(root, costThreshold);
 assertEquals(1, cost);
 }

 @Test
 public void testCompiledSizeEstimator() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(createNode("ab"));
 estimator.add(createNode("cd"));
 estimator.add(createNode("ef"));
 estimator.add(createNode(""));
 int cost = estimator.getCost();
 assertEquals(6, cost);
 }

 @Test
 public void testCompiledSizeEstimatorWithMaxCost() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
 estimator.add(createNode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{};\",:.?/<>>>"));
 assertNotNull(estimator.continueProcessing());
 int cost = estimator.getCost();
 assertEquals(52, cost);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompiledSizeEstimatorWithNegativeCostThreshold() {
 new InlineCostEstimator.CompiledSizeEstimator(-1);
 }
}