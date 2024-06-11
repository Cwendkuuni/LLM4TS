package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression createInstance(Object... args) {
 return new CoreOperationGreaterThan(args);
 }

 @Test
 public void testComputeValue() {
 EvalContext context = new EvalContext();
 Object left = new Integer(1);
 Object right = new Integer(2);
 CoreOperationRelationalExpression expression = createInstance(left, right);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValue_false() {
 EvalContext context = new EvalContext();
 Object left = new Integer(1);
 Object right = new Integer(2);
 CoreOperationRelationalExpression expression = createInstance(right, left);
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_invalidArgs() {
 EvalContext context = new EvalContext();
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan();
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(2));
 int precedence = expression.getPrecedence();
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(2));
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare_lessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThan(new Integer(1), new Integer(2));
 assertTrue(expression.evaluateCompare(-1));
 }

 @Test
 public void testEvaluateCompare_equal() {
 CoreOperationRelationalExpression expression = new CoreOperationEquals(new Integer(1), new Integer(1));
 assertTrue(expression.evaluateCompare(0));
 }

 @Test
 public void testEvaluateCompare_greaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Integer(2), new Integer(1));
 assertTrue(expression.evaluateCompare(1));
 }

 @Test
 public void testCompute_double_lessThan() {
 CoreOperationRelationalExpression expression = createInstance(new Double(1.0), new Double(2.0));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_double_lessThan_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Double(1.0), new Double(2.0));
 assertFalse(expression.compute(new Object(), new Object(1.5)));
 }

 @Test
 public void testCompute_double_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Double(1.0), new Double(1.0));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_double_notEqual() {
 CoreOperationRelationalExpression expression = createInstance(new Double(1.0), new Double(2.0));
 assertFalse(expression.compute(new Object(1.5), new Object()));
 }

 @Test
 public void testCompute_double_greaterThan() {
 CoreOperationRelationalExpression expression = createInstance(new Double(2.0), new Double(1.0));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_double_greaterThan_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Double(2.0), new Double(1.5));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_int_lessThan() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(2));
 assertFalse(expression.compute(new Object(), new Object(1)));
 }

 @Test
 public void testCompute_int_lessThan_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(2));
 assertFalse(expression.compute(new Object(1), new Object()));
 }

 @Test
 public void testCompute_int_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(1));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_int_notEqual() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(1), new Integer(2));
 assertFalse(expression.compute(new Object(1), new Object()));
 }

 @Test
 public void testCompute_int_greaterThan() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(2), new Integer(1));
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_int_greaterThan_equal() {
 CoreOperationRelationalExpression expression = createInstance(new Integer(2), new Integer(1));
 assertTrue(expression.compute(new Object(1), new Object()));
 }

 @Test
 public void testCompute_iterator_containsMatch() {
 Iterator left = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 Iterator right = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 CoreOperationRelationalExpression expression = createInstance(left, right);
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_iterator_notContainsMatch() {
 Iterator left = Collections.singletonList(new Integer(1)).iterator();
 Iterator right = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 CoreOperationRelationalExpression expression = createInstance(left, right);
 assertFalse(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_iterator_intersectionEmpty() {
 Iterator left = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 Iterator right = Arrays.asList(new Integer(3), new Integer(4)).iterator();
 CoreOperationRelationalExpression expression = createInstance(left, right);
 assertFalse(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testCompute_iterator_intersection() {
 Iterator left = Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator();
 Iterator right = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 CoreOperationRelationalExpression expression = createInstance(left, right);
 assertTrue(expression.compute(new Object(), new Object()));
 }

 @Test
 public void testReduce_context() {
 Object context = new InitialContext();
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(context, context);
 assertNotNull(expression.reduce(context));
 }

 @Test
 public void testReduce_collection() {
 Object collection = Arrays.asList(new Integer(1), new Integer(2)).iterator();
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(collection, new Integer(2));
 assertNotNull(expression.reduce(collection));
 }

 @Test
 public void testReduce_integer() {
 Object integer = new Integer(1);
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(integer, integer);
 assertNull(expression.reduce(integer));
 }

 @Test
 public void testReduce_double() {
 Object doubleValue = new Double(1.0);
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(doubleValue, doubleValue);
 assertNull(expression.reduce(doubleValue));
 }

 @Test
 public void testReduce_selfContext() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new SelfContext(), new SelfContext());
 assertNotNull(expression.reduce(new SelfContext()));
 }
}