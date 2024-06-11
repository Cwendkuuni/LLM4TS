package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 expression = new CoreOperationRelationalExpression(new CoreConstantExpression(new Integer(5))) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 }

 @Test
 public void testComputeValueWithTwoNumbers() {
 EvalContext context = new EvalContext();
 Object left = new Integer(3);
 Object right = new Integer(5);
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNonBooleanResult() {
 EvalContext context = new EvalContext();
 Object left = new Integer(3);
 Object right = new Integer(5);
 Expression constantExpression = new CoreConstantExpression(new Integer(6));
 CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new CoreOperation[] { expression, constantExpression }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 Object result = relationalExpression.computeValue(context);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test
 public void testComputeValueWithTwoIterators() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
 Iterator rightIterator = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1))).iterator();
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoIteratorsDifferentSize() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = new LinkedList<>(List.of(new Integer(1), new Integer(2))).iterator();
 Iterator rightIterator = new LinkedList<>(List.of(new Integer(3), new Integer(4), new Integer(5))).iterator();
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoIteratorsNoMatch() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
 Iterator rightIterator = new LinkedList<>(List.of(new Integer(4), new Integer(5), new Integer(6))).iterator();
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoIteratorsMatch() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
 Iterator rightIterator = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1))).iterator();
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoIteratorsEmpty() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = Collections.emptyIterator();
 Iterator rightIterator = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1))).iterator();
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoIteratorsLeftEmpty() {
 EvalContext context = new EvalContext();
 Iterator leftIterator = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1))).iterator();
 Iterator rightIterator = Collections.emptyIterator();
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoInitialContexts() {
 EvalContext context = new EvalContext();
 InitialContext leftInitialContext = new InitialContext();
 leftInitialContext.setCurrentNode(new Integer(3));
 InitialContext rightInitialContext = new InitialContext();
 rightInitialContext.setCurrentNode(new Integer(5));
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithTwoInitialContextsSameNode() {
 EvalContext context = new EvalContext();
 InitialContext leftInitialContext = new InitialContext();
 leftInitialContext.setCurrentNode(new Integer(3));
 InitialContext rightInitialContext = new InitialContext();
 rightInitialContext.setCurrentNode(leftInitialContext.getCurrentNode());
 Object result = expression.computeValue(context);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test
 public void testComputeValueWithTwoSelfContexts() {
 EvalContext context = new EvalContext();
 SelfContext leftSelfContext = new SelfContext();
 SelfContext rightSelfContext = new SelfContext();
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.FALSE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithTwoSelfContextsSameNode() {
 EvalContext context = new EvalContext();
 SelfContext leftSelfContext = new SelfContext();
 SelfContext rightSelfContext = new SelfContext();
 leftSelfContext.setNode(rightSelfContext.getNode());
 Object result = expression.computeValue(context);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test
 public void testComputeValueWithTwoCollections() {
 EvalContext context = new EvalContext();
 List leftCollection = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3)));
 List rightCollection = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1)));
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoCollectionsDifferentSize() {
 EvalContext context = new EvalContext();
 List leftCollection = new LinkedList<>(List.of(new Integer(1), new Integer(2)));
 List rightCollection = new LinkedList<>(List.of(new Integer(3), new Integer(4), new Integer(5)));
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoCollectionsNoMatch() {
 EvalContext context = new EvalContext();
 List leftCollection = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3)));
 List rightCollection = new LinkedList<>(List.of(new Integer(4), new Integer(5), new Integer(6)));
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoCollectionsMatch() {
 EvalContext context = new EvalContext();
 List leftCollection = new LinkedList<>(List.of(new Integer(1), new Integer(2), new Integer(3)));
 List rightCollection = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1)));
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoCollectionsEmpty() {
 EvalContext context = new EvalContext();
 List leftCollection = Collections.emptyList();
 List rightCollection = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1)));
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithTwoCollectionsLeftEmpty() {
 EvalContext context = new EvalContext();
 List leftCollection = new LinkedList<>(List.of(new Integer(3), new Integer(2), new Integer(1)));
 List rightCollection = Collections.emptyList();
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithDoubleAndNumber() {
 EvalContext context = new EvalContext();
 Object left = new Double(3.0);
 Object right = new Integer(5);
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithNumberAndDouble() {
 EvalContext context = new EvalContext();
 Object left = new Integer(5);
 Object right = new Double(3.0);
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithDoubleAndDouble() {
 EvalContext context = new EvalContext();
 Object left = new Double(3.0);
 Object right = new Double(5.0);
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithDoublesEqual() {
 EvalContext context = new EvalContext();
 Object left = new Double(3.0);
 Object right = new Double(3.0);
 Object result = expression.computeValue(context);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testComputeValueWithDoublesNotEqual() {
 EvalContext context = new EvalContext();
 Object left = new Double(3.0);
 Object right = new Double(5.0);
 Object result = expression.computeValue(context);
 assertFalse(result == Boolean.TRUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullLeft() {
 EvalContext context = new EvalContext();
 Object left = null;
 Object right = new Integer(5);
 Object result = expression.computeValue(context);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullRight() {
 EvalContext context = new EvalContext();
 Object left = new Integer(5);
 Object right = null;
 Object result = expression.computeValue(context);
 fail("Expected IllegalArgumentException but got: " + result);
 }

}