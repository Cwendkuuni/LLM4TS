package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int DEFAULT_COST_THRESHOLD = 10;
 private static final Node DUMMY_NODE = new Node();

 @Test
 public void testGetCostRoot() {
   int cost = InlineCostEstimator.getCost(DUMMY_NODE);
   assertEquals(0, cost);
 }

 @Test
 public void testGetCostRootWithThreshold() {
   int cost = InlineCostEstimator.getCost(DUMMY_NODE, DEFAULT_COST_THRESHOLD);
   assertEquals(0, cost);
 }

 @Test
 public void testGetCostNodeWithThreshold() {
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD);
   estimator.add(DUMMY_NODE);
   int cost = estimator.getCost();
   assertEquals(0, cost);
 }

 @Test
 public void testGetCostSimpleExpression() {
   Node root = new NumberLiteral(123);
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(2, cost); // 1 for the number literal and 1 for the ';' terminator
 }

 @Test
 public void testGetCostComplexExpression() {
   Node root = new BinaryOperator("+", new NumberLiteral(1), new NumberLiteral(2));
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(4, cost); // 2 for the '+' operator and 2 for each number literal
 }

 @Test
 public void testGetCostIdentifiers() {
   Node root = new Assignment("x", new Identifier("a"), new NumberLiteral(1));
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(5, cost); // 2 for 'x', 2 for 'a', and 1 for the '=' operator
 }

 @Test
 public void testGetCostLongIdentifiers() {
   Node root = new Assignment("fooBarXyz", new Identifier("longIdentifier1"), new NumberLiteral(1));
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(21, cost); // 19 for 'fooBarXyz', 1 for the '=', and 1 for the ';' terminator
 }

 @Test
 public void testGetCostLongIdentifiersWithThreshold() {
   Node root = new Assignment("fooBarXyz", new Identifier("longIdentifier1"), new NumberLiteral(1));
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD - 16);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(5, cost); // Since the threshold is lower than the estimated identifier cost, only the identifier name is considered
 }

 @Test
 public void testGetCostLongIdentifiersWithThresholdAndLongerIdentifier() {
   Node root = new Assignment("fooBarXyzLongerIdentifier", new Identifier("longIdentifier1"), new NumberLiteral(1));
   CompiledSizeEstimator estimator = new CompiledSizeEstimator(DEFAULT_COST_THRESHOLD - 15);
   estimator.add(root);
   int cost = estimator.getCost();
   assertEquals(6, cost); // Since the threshold is lower than the estimated identifier cost, only the identifier name is considered, and an additional character for the longer identifier is added
 }
}