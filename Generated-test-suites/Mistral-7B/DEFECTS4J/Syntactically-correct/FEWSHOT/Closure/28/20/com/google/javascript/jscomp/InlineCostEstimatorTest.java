package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final Node TEST_ROOT = new Node(); // place holder for root node

 @Test
 public void testGetCost() {
  int cost = InlineCostEstimator.getCost(TEST_ROOT);
  assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  estimator.add(TEST_ROOT);
  int cost = estimator.getCost();
  assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithLargeThreshold() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
  estimator.add(TEST_ROOT);
  int cost = estimator.getCost();
  assertEquals(0, cost);
 }

 @Test
 public void testAddNode() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  estimator.add(TEST_ROOT);
  Node node = new Node();
  estimator.add(node);
  int cost = estimator.getCost();
  assertEquals(0, cost);
 }

 @Test
 public void testAddIdentifier() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  estimator.addIdentifier("identifier");
  int cost = estimator.getCost();
  assertEquals(ESTIMATED_IDENTIFIER_COST * 2, cost); // 2 characters for "identifier" and "ab"
 }

 @Test
 public void testAddString() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  estimator.add("testString");
  int cost = estimator.getCost();
  assertEquals(ESTIMATED_IDENTIFIER_COST * 2 + 7, cost); // 7 characters for "testString"
 }

 @Test
 public void testAddStringWithLongerString() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
  estimator.add("longTestString");
  int cost = estimator.getCost();
  assertEquals(ESTIMATED_IDENTIFIER_COST * 2 + 11, cost); // 11 characters for "longTestString"
 }

 @Test
 public void testAddStringWithCostThresholdExceeded() {
  CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
  estimator.add("longTestString");
  assertNotEquals(ESTIMATED_IDENTIFIER_COST * 2 + 11, estimator.getCost());
 }
}