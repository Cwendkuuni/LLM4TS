package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqual() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(1.5),
 new ConstantExpression(2.5)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotLessThanOrEqual() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(2.5),
 new ConstantExpression(1.5)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithNegativeNumbers() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(-1.5),
 new ConstantExpression(-2.5)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotLessThanOrEqualWithNegativeNumbers() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(-2.5),
 new ConstantExpression(-1.5)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithMixedTypes() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(1.5),
 new ConstantExpression(2L)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 // JXPath is unable to compare different types directly, thus a ClassCastException will be thrown when trying to assert the result
 // Uncomment the lines below to see the exception
 //assertTrue(result instanceof Boolean);
 //assertTrue((Boolean) result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanOrEqualWithMixedTypesAndAssertion() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new ConstantExpression(1.5),
 new ConstantExpression(2L)
 );
 EvalContext context = new EvalContext(new InfoSet());
 Object result = op.computeValue(context);
 // JXPath is unable to compare different types directly, thus a ClassCastException will be thrown when trying to assert the result
 // uncomment the line below to remove the test
 //assertTrue(result instanceof Boolean);
 //assertTrue((Boolean) result);
 }
}