package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 static final Node ROOT_NODE = new Node_Literal(new Node_StringLiteral("root"));

 @Test
 public void testGetCostRoot() {
 int cost = InlineCostEstimator.getCost(ROOT_NODE);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostRootWithThreshold() {
 int cost = InlineCostEstimator.getCost(ROOT_NODE, 10);
 assertEquals(0, cost);
 }

 @Test
 public void testCompiledSizeEstimatorAddString() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add("hello");
 estimator.add("world");

 estimator.add(ROOT_NODE); // added for testing

 assertEquals(11, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddIdentifier() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.addIdentifier("identifier");

 estimator.add(ROOT_NODE); // added for testing

 assertEquals(3, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddBooleanLiteralTrue() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(new Node_Literal(new Node_Bool(true)));

 estimator.add(ROOT_NODE); // added for testing

 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddBooleanLiteralFalse() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(new Node_Literal(new Node_Bool(false)));

 estimator.add(ROOT_NODE); // added for testing

 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddNullLiteral() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(NodeFactory.nullLiteral());

 estimator.add(ROOT_NODE); // added for testing

 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorMaxCost() {
 InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
 estimator.add("hello");
 estimator.add("world");
 estimator.addIdentifier("identifier");
 estimator.add(ROOT_NODE);

 assertEquals(8, estimator.getCost()); // actual cost is 11, but maxcost is 5, so it stops
 }
}