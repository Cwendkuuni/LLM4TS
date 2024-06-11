package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private static final int COST_THRESHOLD = 10;
 private static final Node ROOT_NODE = new Node();

 @Test
 public void testGetCost() {
 assertEquals(0, InlineCostEstimator.getCost(ROOT_NODE));
 }

 @Test
 public void testGetCostWithThreshold() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.add(ROOT_NODE);
 estimator.getCost(); // this call consumes the node
 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testGetCostSimpleIdentifier() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.addIdentifier("ab");
 estimator.getCost(); // this call consumes the identifier
 assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
 }

 @Test
 public void testGetCostMultipleIdentifiers() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.addIdentifier("ab");
 estimator.addIdentifier("cd");
 estimator.getCost(); // this call consumes the identifiers
 assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2, estimator.getCost());
 }

 @Test
 public void testGetCostLongerIdentifier() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 estimator.addIdentifier("longIdentifier");
 estimator.getCost(); // this call consumes the identifier
 assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
 }

 @Test
 public void testGetCostString() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 String str = "this is a test string";
 estimator.append(str);
 estimator.getCost(); // this call consumes the string
 assertEquals(str.length(), estimator.getCost());
 }

 @Test
 public void testGetCostLongString() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 String str = "this is a very long test string that will exceed the cost threshold";
 estimator.append(str);
 estimator.getCost(); // this call consumes the string
 assertNotEquals(str.length(), estimator.getCost());
 }

 @Test
 public void testGetCostWithCostThreshold() {
 CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(COST_THRESHOLD);
 String str = "this is a very long test string that will exceed the cost threshold";
 estimator.append(str);
 estimator.getCost(); // this call consumes the string
 assertNotEquals(str.length(), estimator.getCost());
 estimator.addIdentifier("ab");
 estimator.addIdentifier("cd");
 estimator.getCost(); // this call consumes the identifiers
 assertEquals(COST_THRESHOLD, estimator.getCost());
 }
}