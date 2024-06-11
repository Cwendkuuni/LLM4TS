package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

 static final int ESTIMATED_IDENTIFIER_COST = "ab".length();

 @Test
 public void testGetCost() {
 // Test empty case
 assertEquals(0, InlineCostEstimator.getCost(null));

 // Test single node
 Node root = new Node_Literal(null, "1");
 assertEquals(ESTIMATED_IDENTIFIER_COST, InlineCostEstimator.getCost(root));

 // Test multiple nodes
 Node root1 = new Node_Literal(null, "1");
 Node root2 = new Node_Literal(null, "2");
 Node root3 = new Node_BinaryOp("+", root1, root2);
 assertEquals(2 * ESTIMATED_IDENTIFIER_COST, InlineCostEstimator.getCost(root3));
 }

 @Test
 public void testGetCostWithThreshold() {
 // Test empty case
 assertEquals(0, InlineCostEstimator.getCost(null, 0));

 // Test single node
 Node root = new Node_Literal(null, "1");
 assertEquals(ESTIMATED_IDENTIFIER_COST, InlineCostEstimator.getCost(root, ESTIMATED_IDENTIFIER_COST * 2));

 // Test multiple nodes
 Node root1 = new Node_Literal(null, "1");
 Node root2 = new Node_Literal(null, "2");
 Node root3 = new Node_BinaryOp("+", root1, root2);
 assertEquals(2 * ESTIMATED_IDENTIFIER_COST, InlineCostEstimator.getCost(root3, 3 * ESTIMATED_IDENTIFIER_COST));

 // Test threshold is reached
 Node root1 = new Node_Literal(null, "1");
 Node root2 = new Node_Literal(null, "1");
 Node root3 = new Node_BinaryOp("+", root1, root2);
 Node root4 = new Node_BinaryOp("+", root3, root3);
 assertEquals(3 * ESTIMATED_IDENTIFIER_COST, InlineCostEstimator.getCost(root4, 3 * ESTIMATED_IDENTIFIER_COST));
 }

 @Test
 public void testCompiledSizeEstimator() {
 // Test empty case
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(0);
 assertTrue(estimator.continueProcessing());
 assertEquals(0, estimator.getCost());

 // Test single character
 estimator = new InlineCostEstimator.CompiledSizeEstimator(ESTIMATED_IDENTIFIER_COST);
 estimator.append("a");
 assertTrue(estimator.continueProcessing());
 assertEquals(ESTIMATED_IDENTIFIER_COST, estimator.getCost());

 // Test multiple characters
 estimator = new InlineCostEstimator.CompiledSizeEstimator(ESTIMATED_IDENTIFIER_COST * 2);
 estimator.append("ab");
 estimator.append("cd");
 assertTrue(estimator.continueProcessing());
 assertEquals(2 * ESTIMATED_IDENTIFIER_COST, estimator.getCost());

 // Test threshold is reached
 estimator = new InlineCostEstimator.CompiledSizeEstimator(ESTIMATED_IDENTIFIER_COST * 3);
 estimator.append("ab");
 estimator.append("cd");
 estimator.append("ef");
 assertFalse(estimator.continueProcessing());
 assertEquals(3 * ESTIMATED_IDENTIFIER_COST, estimator.getCost());
 }
}