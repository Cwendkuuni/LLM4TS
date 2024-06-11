package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int MAX_COST = 10;

 @Test
 public void testGetCost() {
 Node root = new Node(); // create an empty node
 int cost = InlineCostEstimator.getCost(root);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = new Node(); // create an empty node
 int cost = InlineCostEstimator.getCost(root, MAX_COST);
 assertEquals(0, cost);
 }

 @Test
 public void testCompiledSizeEstimator() {
 // Create a test node with some string values
 Node testNode = new Node();
 testNode.setType(Node.STRING);
 testNode.setString("Test string");

 // Test the CompiledSizeEstimator with a single string node
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 estimator.add(testNode);
 int cost = estimator.getCost();
 assertEquals(14, cost); // "Test string".length() + 1 (for the final null character)
 }

 @Test
 public void testCompiledSizeEstimatorWithMultipleNodes() {
 // Create test nodes with some string values
 Node testNode1 = new Node();
 testNode1.setType(Node.STRING);
 testNode1.setString("Test1 string");

 Node testNode2 = new Node();
 testNode2.setType(Node.STRING);
 testNode2.setString("Test2 string");

 // Test the CompiledSizeEstimator with multiple string nodes
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 estimator.add(testNode1);
 estimator.add(testNode2);
 int cost = estimator.getCost();
 assertEquals(32, cost); // (2 * ("Test1 string".length() + 1)) + (2 * ("Test2 string".length() + 1))
 }

 @Test
 public void testCompiledSizeEstimatorWithIdentifiers() {
 // Create test nodes with some identifiers
 Node testNode1 = new Node();
 testNode1.setType(Node.IDENTIFIER);
 testNode1.setIdentifier("testIdentifier1");

 Node testNode2 = new Node();
 testNode2.setType(Node.IDENTIFIER);
 testNode2.setIdentifier("testIdentifier2");

 // Test the CompiledSizeEstimator with multiple identifier nodes
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 estimator.add(testNode1);
 estimator.add(testNode2);
 int cost = estimator.getCost();
 assertEquals(6, cost); // (2 * ESTIMATED_IDENTIFIER.length())
 }

 @Test
 public void testCompiledSizeEstimatorWithThreshold() {
 // Create test nodes with some string values and identifiers
 Node testNode1 = new Node();
 testNode1.setType(Node.STRING);
 testNode1.setString("Test1 string");

 Node testNode2 = new Node();
 testNode2.setType(Node.IDENTIFIER);
 testNode2.setIdentifier("testIdentifier");

 // Test the CompiledSizeEstimator with multiple string and identifier nodes
 // with a cost threshold
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(MAX_COST);
 estimator.add(testNode1);
 estimator.add(testNode2);
 int cost = estimator.getCost();
 assertEquals(MAX_COST, cost); // The threshold is reached, so no further processing
 }
}