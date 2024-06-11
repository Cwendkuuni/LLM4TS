package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression createExpression(Expression[] args) {
 return new CoreOperationRelationalExpression(args) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 }

 @Test
 public void testComputeValue() {
 Object left = new Integer(5);
 Object right = new Integer(10);
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new ConstantExpression(left), new ConstantExpression(right) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueWithEqualValues() {
 Object left = new Integer(5);
 Object right = new Integer(5);
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new ConstantExpression(left), new ConstantExpression(right) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result.equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithUnequalValues() {
 Object left = new Integer(5);
 Object right = new Integer(10);
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new ConstantExpression(left), new ConstantExpression(right) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithNullLeft() {
 Object left = null;
 Object right = new Integer(5);
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new ConstantExpression(left), new ConstantExpression(right) });
 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComputeValueWithNullRight() {
 Object left = new Integer(5);
 Object right = null;
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new ConstantExpression(left), new ConstantExpression(right) });
 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComputeValueWithIterators() {
 List left = new ArrayList();
 List right = new ArrayList();
 left.add(new Integer(5));
 left.add(new Integer(10));
 right.add(new Integer(5));
 right.add(new Integer(10));
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueWithIteratorsContainingMatchingValues() {
 List left = new ArrayList();
 List right = new ArrayList();
 left.add(new Integer(5));
 left.add(new Integer(10));
 right.add(new Integer(5));
 right.add(new Integer(10));
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result.equals(Boolean.FALSE));
 }

 @Test
 public void testComputeValueWithIteratorsContainingMatchingElement() {
 List left = new ArrayList();
 List right = new ArrayList();
 left.add(new Integer(5));
 left.add(new Integer(10));
 right.add(new HashSet());
 right.get(0).add(new Integer(5));
 EvalContext context = new EvalContext(new SelfContext(null));

 CoreOperationRelationalExpression expression = createExpression(new Expression[] { new IteratorExpression(left.iterator()), new IteratorExpression(right.iterator()) });
 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullArguments() {
 CoreOperationRelationalExpression expression = createExpression(null);
 EvalContext context = new EvalContext(new SelfContext(null));

 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithLessThanTwoArguments() {
 CoreOperationRelationalExpression expression = createExpression(new Expression[0]);
 EvalContext context = new EvalContext(new SelfContext(null));

 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}