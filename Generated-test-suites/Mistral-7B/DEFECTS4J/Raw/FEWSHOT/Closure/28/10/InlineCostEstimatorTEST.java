package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

 static final Node rootNode = new Node(); // Create an empty Node for testing

 @Test
 public void testGetCost() {
  int cost = InlineCostEstimator.getCost(rootNode);
  assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
  int cost = InlineCostEstimator.getCost(rootNode, 10);
  assertEquals(0, cost);
 }

 @Test
 public void testCompiledSizeEstimator() {
  InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
  String testString = "this is a test string";

  estimator.add(new TextNode("var x = "));
  estimator.addIdentifier("ab"); // checks for estimated identifier length
  estimator.add(new TextNode(" = "));
  estimator.add(new TextNode(testString));
  estimator.add(new TextNode(";\n"));

  int cost = estimator.getCost();
  int expectedCost = ESTIMATED_IDENTIFIER_COST * 2 + testString.length() + 5; // 5 for "var x = ", " = ", ";" and "\n"
  assertEquals(expectedCost, cost);
 }

 @Test
 public void testGetCostWithLongerString() {
  InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(20);
  String testString = "this is a long test string that will test the getCost method with a longer string length";

  estimator.add(new TextNode("var x = "));
  estimator.addIdentifier("ab");
  estimator.add(new TextNode(" = "));
  estimator.add(new TextNode(testString));
  estimator.add(new TextNode(";\n"));

  int cost = estimator.getCost();
  int expectedCost = ESTIMATED_IDENTIFIER_COST * 2 + testString.length() + 5;
  assertEquals(expectedCost, cost);
 }

 @Test
 public void testGetCostWithLongThreshold() {
  InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
  String testString = "this is a test string that will test the getCost method with a large threshold";

  estimator.add(new TextNode("var x = "));
  estimator.addIdentifier("ab");
  estimator.add(new TextNode(" = "));
  estimator.add(new TextNode(testString));
  estimator.add(new TextNode(";\n"));

  int cost = estimator.getCost();
  int expectedCost = ESTIMATED_IDENTIFIER_COST * 2 + testString.length() + 5;
  assertEquals(expectedCost, cost);
 }
}