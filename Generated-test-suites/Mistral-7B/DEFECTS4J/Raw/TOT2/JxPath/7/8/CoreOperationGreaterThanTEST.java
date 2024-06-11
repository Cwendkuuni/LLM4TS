package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

public class CoreOperationGreaterThanTest {
 @Test
 public void testCoreOperationGreaterThan() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(3.0));
 assertTrue(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testComputeValue() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(3.0));
 Truth assertions = Truth.assertAbout("GreaterThan operation")
 .that(operation.computeValue(new EvalContext()));
 assertions.isTrue();
 }

 @Test
 public void testEdgeCaseEqualArguments() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(5.0));
 assertFalse(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testEdgeCaseNaNArgument() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(5.0));
 assertFalse(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testExceptionNonNumberArgument() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression("string"), new ConstantExpression(5.0));
 }

 @Test
 public void testEdgeCaseZeroArguments() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(0.0), new ConstantExpression(0.0));
 assertFalse(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testEdgeCasePositiveInfinityArgument() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(3.0));
 assertTrue(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testEdgeCaseNegativeArguments() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(-3.0), new ConstantExpression(-2.0));
 assertTrue(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testEdgeCaseNegativeInfinityArgument() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(-Double.POSITIVE_INFINITY), new ConstantExpression(3.0));
 assertFalse(operation.computeValue(new EvalContext()).booleanValue());
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionNullArgument() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, new ConstantExpression(5.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionSameInstanceArgument() {
 CoreOperationGreaterThan arg1 = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(3.0));
 CoreOperationGreaterThan arg2 = new CoreOperationGreaterThan(arg1, new ConstantExpression(3.0));
 }
}