package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node createNode(String value) {
 return new Node_literal(value, null);
 }

 @Test
 public void testGetCost() {
 Node root = new Node_program();
 root.addChild(createNode("var x = 1;"));
 root.addChild(createNode("var y = 2;"));
 root.addChild(createNode("x + y;"));

 assertEquals(ESTIMATED_IDENTIFIER_COST * 6, InlineCostEstimator.getCost(root));
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = new Node_program();
 root.addChild(createNode("var x = 1;"));
 root.addChild(createNode("var y = 2;"));
 root.addChild(createNode("x + y;"));

 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
 estimator.add(root);

 assertEquals(ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
 }

 @Test
 public void testAppend() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.append("ab");
 estimator.append("cd");
 estimator.append("ef");

 assertEquals(3, estimator.getCost());
 }

 @Test
 public void testAddIdentifier() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.addIdentifier("xyz");

 assertEquals(ESTIMATED_IDENTIFIER_COST + 3, estimator.getCost());
 }

 @Test
 public void testContinueProcessing() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
 estimator.append("a");
 estimator.append("b");
 estimator.append("c");

 assertEquals(3, estimator.getCost());
 assertNotEquals(0, estimator.getCost());
 }

 @Test
 public void testCostConsumerMethods() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(new Node_literal("x = 1;", null));
 estimator.add(new Node_literal("y = 2;", null));
 estimator.add(new Node_literal("x + y;", null));

 assertEquals(ESTIMATED_IDENTIFIER_COST * 6, estimator.getCost());
 assertEquals(';', estimator.getLastChar());
 }
}