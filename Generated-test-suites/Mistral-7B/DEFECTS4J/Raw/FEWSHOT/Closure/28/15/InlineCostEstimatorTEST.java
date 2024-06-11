package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int COST_THRESHOLD = 10;

 @Test
 public void testGetCost() {
 Node root = Node.objectLiteral();
 int cost = InlineCostEstimator.getCost(root);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = NodeOperations.createBinaryOperator("+", NodeOperations.createNumber(1), NodeOperations.createNumber(2));
 int cost = InlineCostEstimator.getCost(root, COST_THRESHOLD);
 assertEquals(3, cost);
 }

 @Test
 public void testCompiledSizeEstimator() {
 // Test empty string
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.add("");
 estimator.add("");
 estimator.add(Node.IDENTIFIER, "identifier");
 assertEquals(0, estimator.getCost());

 // Test single character strings
 estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.add("a");
 estimator.add("b");
 estimator.add("c");
 assertEquals(3, estimator.getCost());

 // Test multiple character strings
 estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.add("abc");
 estimator.add("def");
 estimator.add("ghi");
 assertEquals(9, estimator.getCost());

 // Test long string
 estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD * 2);
 estimator.add("abcdefghijklmnopqrstuvwxyz");
 assertEquals(26, estimator.getCost());

 // Test threshold reached
 estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.add("abcdefghijklmnopqrstuvwxyz");
 assertNotEquals(26, estimator.getCost());
 }
}