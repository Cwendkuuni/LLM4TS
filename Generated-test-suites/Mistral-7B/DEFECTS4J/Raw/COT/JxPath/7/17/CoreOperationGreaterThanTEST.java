package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

 // Test all public methods in CoreOperationGreaterThan class

 // Test constructor method
 @Test
 public void testCoreOperationGreaterThanConstructor() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(1.0), TestUtil.createExpression(2.0));
 }

 // Test computeValue method with double values
 @Test
 public void testComputeValueDoubleDoublePositive() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(3.0), TestUtil.createExpression(2.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 Object result = coreOperationGreaterThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueDoubleDoubleNegative() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(2.0), TestUtil.createExpression(3.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 Object result = coreOperationGreaterThan.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse(result.equals(Boolean.TRUE));
 }

 // Test getSymbol method
 @Test
 public void testGetSymbol() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(1.0), TestUtil.createExpression(2.0));
 assertEquals(">", coreOperationGreaterThan.getSymbol());
 }

 // Edge cases and exception handling scenarios

 // Test computeValue method with double NaN values
 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueDoubleNaN() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(Double.NaN), TestUtil.createExpression(Double.NaN));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 coreOperationGreaterThan.computeValue(context);
 }

 // Test computeValue method with double NaN and positive number
 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueDoubleNaNAndPositive() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(Double.NaN), TestUtil.createExpression(3.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 coreOperationGreaterThan.computeValue(context);
 }

 // Test computeValue method with double NaN and negative number
 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueDoubleNaNAndNegative() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(Double.NaN), TestUtil.createExpression(-3.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 coreOperationGreaterThan.computeValue(context);
 }

 // Test computeValue method with double negative numbers
 @Test
 public void testComputeValueDoubleNegativeNegative() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(-3.0), TestUtil.createExpression(-2.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 Object result = coreOperationGreaterThan.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse(result.equals(Boolean.TRUE));
 }

 // Test computeValue method with double positive numbers and one is zero
 @Test
 public void testComputeValueDoublePositiveZero() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(TestUtil.createExpression(3.0), TestUtil.createExpression(0.0));
 EvalContext context = new EvalContext();
 InfoSet infoSet = TestUtil.createInfoSet();
 context.setInfoSet(infoSet);
 Object result = coreOperationGreaterThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }
}