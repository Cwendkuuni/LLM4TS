package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.StringExpression;
import org.junit.Test;

public class CoreOperationLessThanTest {

 CoreOperationLessThan coreOperationLessThan;

 @Test
 public void testCoreOperationLessThanConstruction() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 }

 @Test
 public void testComputeValueTypicalUsage() {
 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 assertTrue(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test
 public void testEdgeCaseSameValue() {
 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
 assertFalse(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionNullContext() {
 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 coreOperationLessThan.computeValue(null);
 }

 @Test
 public void testEdgeZeroNegative() {
 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(0.0), new ConstantExpression(-1.0));
 assertTrue(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionInvalidArgumentTypes() {
 coreOperationLessThan = new CoreOperationLessThan(new StringExpression("1.0"), new StringExpression("2.0"));
 }

 @Test
 public void testConstructor() {
 coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 }

 @Test
 public void testComputeValueTypicalUsage() {
 assertTrue(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test
 public void testEdgeCaseSameValue() {
 assertFalse(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionNullContext() {
 coreOperationLessThan.computeValue(null);
 }

 @Test
 public void testEdgeZeroNegative() {
 assertTrue(coreOperationLessThan.computeValue(new EvalContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionInvalidArgumentTypes() {
 coreOperationLessThan = new CoreOperationLessThan(new StringExpression("1.0"), new StringExpression("2.0"));
 }

 @Test
 public void testNullArgument() {
 assertNull(new CoreOperationLessThan(null, new ConstantExpression(2.0)));
 }
}