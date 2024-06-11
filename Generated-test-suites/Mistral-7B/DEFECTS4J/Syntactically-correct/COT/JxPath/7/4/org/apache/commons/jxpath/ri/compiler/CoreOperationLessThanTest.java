package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;

public class CoreOperationLessThanTest {

 private CoreOperationLessThan operation;
 private Expression arg1, arg2;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = new CoreNumberExpression(1.0);
 arg2 = new CoreNumberExpression(2.0);
 operation = new CoreOperationLessThan(arg1, arg2);
 }

 @Test
 public void testCoreOperationLessThan_whenBothArgumentsAreNumbers_thenReturnExpectedResult() {
 InfoSet context = new InfoSet();
 double result = operation.computeValue(new EvalContext(context)).doubleValue();
 assertTrue(result);
 }

 @Test
 public void testCoreOperationLessThan_whenFirstArgumentIsGreaterThanSecond_thenReturnExpectedResult() {
 InfoSet context = new InfoSet();
 operation = new CoreOperationLessThan(new CoreNumberExpression(2.0), arg1);
 double result = operation.computeValue(new EvalContext(context)).doubleValue();
 assertFalse(result);
 }

 @Test
 public void testCoreOperationLessThan_whenBothArgumentsAreTheSameNumber_thenReturnExpectedResult() {
 InfoSet context = new InfoSet();
 operation = new CoreOperationLessThan(arg1, arg1);
 double result = operation.computeValue(new EvalContext(context)).doubleValue();
 assertFalse(result);
 }

 @Test(expected = ClassCastException.class)
 public void testCoreOperationLessThan_whenFirstArgumentIsNotANumber_thenThrowExpectedException() {
 InfoSet context = new InfoSet();
 arg1 = new CoreStringExpression("1");
 operation = new CoreOperationLessThan(arg1, arg2);
 operation.computeValue(new EvalContext(context));
 }

 @Test(expected = ClassCastException.class)
 public void testCoreOperationLessThan_whenSecondArgumentIsNotANumber_thenThrowExpectedException() {
 InfoSet context = new InfoSet();
 arg2 = new CoreStringExpression("2");
 operation = new CoreOperationLessThan(arg1, arg2);
 operation.computeValue(new EvalContext(context));
 }

 @Test
 public void testCoreOperationLessThan_whenOneArgumentIsANumberAndAnotherIsString_thenReturnFalse() {
 InfoSet context = new InfoSet();
 operation = new CoreOperationLessThan(arg1, new CoreStringExpression("2"));
 double result = operation.computeValue(new EvalContext(context)).doubleValue();
 assertFalse(result);
 }

}