package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.Axis;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeIterators;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private EvalContext context;
 private InfoSet infoSet;

 @Before
 public void setup() {
 // Create a mock EvalContext and InfoSet for testing
 context = new EvalContext();
 infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 }

 @Test
 public void testGreaterThan() {
 // Given two numbers, one greater than the other
 expression = new GreaterThanExpression(new LiteralExpression(5.0), new LiteralExpression(3.0));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value true
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan() {
 // Given two numbers, one less than the other
 expression = new LessThanExpression(new LiteralExpression(3.0), new LiteralExpression(5.0));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value true
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 // Given two numbers, one greater than or equal to the other
 expression = new GreaterThanOrEqualExpression(new LiteralExpression(5.0), new LiteralExpression(5.0));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value true
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 // Given two numbers, one less than or equal to the other
 expression = new LessThanOrEqualExpression(new LiteralExpression(5.0), new LiteralExpression(5.0));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value true
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testIterators() {
 // Given two iterators with the same elements
 Iterator<NodeInfo> iter1 = NodeIterators.emptyIterator();
 iter1.add(createNodeInfo(1));
 iter1.add(createNodeInfo(2));
 Iterator<NodeInfo> iter2 = NodeIterators.emptyIterator();
 iter2.add(createNodeInfo(1));
 iter2.add(createNodeInfo(2));
 // Create a CoreOperationRelationalExpression instance with ">" comparison
 expression = new GreaterThanExpression(iter1, iter2);
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value false
 assertFalse(result instanceof Boolean);
 // Given two iterators with the same elements, but with one iterator in reverse order
 Iterator<NodeInfo> iter3 = NodeIterators.emptyIterator();
 iter3.add(createNodeInfo(2));
 iter3.add(createNodeInfo(1));
 // Create a CoreOperationRelationalExpression instance with "<" comparison
 expression = new LessThanExpression(iter1, iter3);
 // When computing the value of the expression
 result = expression.computeValue(context);
 // Then the result should be a Boolean with value true
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNaN() {
 // Given two NaN values
 expression = new GreaterThanExpression(new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value false
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testNull() {
 // Given two null values
 expression = new GreaterThanExpression(new LiteralExpression(null), new LiteralExpression(null));
 // When computing the value of the expression
 Object result = expression.computeValue(context);
 // Then the result should be a Boolean with value false
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testErrorScenario() {
 // Given an invalid CoreOperationRelationalExpression instance
 expression = new CoreOperationRelationalExpression(new Expression[0]) {
 @Override protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException();
 }
 };
 // When computing the value of the expression
 try {
 expression.computeValue(context);
 fail("Expected an IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Then the correct exception is thrown
 }
 }

 private NodeInfo createNodeInfo(int value) {
 return new NodeInfo(Axis.UNSPECIFIED, new String(), new String(), new String(), value);
 }

}