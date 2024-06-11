package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * JUnit test file for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression = null;

 @Test
 public void testConstructor() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertNotNull(expression);
 }

 @Test
 public void testComputeValue() {
 Object left = new Integer(5);
 Object right = new Integer(10);
 Expression[] args = {new CoreConstantExpression(left), new CoreConstantExpression(right)};
 expression = new CoreOperationRelationalExpression(args);
 EvalContext context = new EvalContext(new InitialContext(), new SelfContext(), null);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new Integer(1);
 right = new Integer(5);
 args = new Expression[] {new CoreConstantExpression(left), new CoreConstantExpression(right)};
 expression = new CoreOperationRelationalExpression(args);
 context = new EvalContext(new InitialContext(), new SelfContext(), null);
 result = expression.computeValue(context);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGetPrecedence() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertFalse(expression.isSymmetric());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEvaluateCompareNegative() {
 expression = new CoreOperationGtExpression(new Expression[0]);
 int compare = -1;
 boolean result = expression.evaluateCompare(compare);
 }

 @Test
 public void testEvaluateCompareZero() {
 expression = new CoreOperationGtExpression(new Expression[0]);
 int compare = 0;
 boolean result = expression.evaluateCompare(compare);
 assertFalse(result);
 }

 @Test
 public void testEvaluateComparePositive() {
 expression = new CoreOperationGtExpression(new Expression[0]);
 int compare = 1;
 boolean result = expression.evaluateCompare(compare);
 assertTrue(result);
 }

 @Test
 public void testEvaluateCompareEqual() {
 expression = new CoreOperationGeqExpression(new Expression[0]);
 int compare = 0;
 boolean result = expression.evaluateCompare(compare);
 assertTrue(result);
 }

 @Test
 public void testEvaluateCompareNotEqual() {
 expression = new CoreOperationGeqExpression(new Expression[0]);
 int compare = 1;
 boolean result = expression.evaluateCompare(compare);
 assertFalse(result);
 }

 @Test
 public void testComputeWithIterators() {
 Iterator<Object> itLeft = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 Iterator<Object> itRight = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(2);
 }
 };
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean result = expression.compute(itLeft.asList(), itRight.asList());
 assertFalse(result);

 itLeft = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 itRight = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 result = expression.compute(itLeft.asList(), itRight.asList());
 assertTrue(result);
 }

 @Test
 public void testContainsMatch() {
 Iterator<Object> it = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 Object value = new Integer(1);
 boolean result = expression.containsMatch(it, value);
 assertTrue(result);

 it = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(2);
 }
 };
 result = expression.containsMatch(it, value);
 assertFalse(result);
 }

 @Test
 public void testFindMatch() {
 Iterator<Object> lit = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 Iterator<Object> rit = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 boolean result = expression.findMatch(lit, rit);
 assertTrue(result);

 lit = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(1);
 }
 };
 rit = new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(2);
 }
 };
 result = expression.findMatch(lit, rit);
 assertFalse(result);
 }

 @Test
 public void testComputeWithDoubles() {
 double left = 5.5;
 double right = 10.0;
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean result = expression.compute(left, right);
 assertFalse(result);

 left = 1.0;
 right = 5.5;
 result = expression.compute(left, right);
 assertFalse(result);

 left = 5.5;
 right = 5.5;
 result = expression.compute(left, right);
 assertTrue(result);
 }

 @Test(expected = ClassCastException.class)
 public void testComputeWithDifferentTypes() {
 double left = 5.5;
 Object right = new Integer(5);
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean result = expression.compute(left, right);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeWithNull() {
 Object left = null;
 Object right = new Integer(5);
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean result = expression.compute(left, right);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeWithNull1() {
 Object left = new Integer(5);
 Object right = null;
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean result = expression.compute(left, right);
 }

}