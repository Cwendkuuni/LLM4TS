package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationGreaterThanTest {

 // Test case for constructor
 @Test
 public void testCoreOperationGreaterThanConstructor() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 assertNotNull(coreOperationGreaterThan);
 }

 // Test case for computeValue method with positive numbers
 @Test
 public void testComputeValueWithPositiveNumbers() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(3.0), new CoreConstantExpression(1.0));
 EvalContext context = new EvalContext();
 assertTrue(coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 // Test case for computeValue method with negative numbers
 @Test
 public void testComputeValueWithNegativeNumbers() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(-2.0), new CoreConstantExpression(-1.0));
 EvalContext context = new EvalContext();
 assertFalse(coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 // Test case for computeValue method with equal numbers
 @Test
 public void testComputeValueWithEqualNumbers() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(2.0), new CoreConstantExpression(2.0));
 EvalContext context = new EvalContext();
 assertFalse(coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 // Test case for computeValue method with one null argument
 @Test(expected = NullPointerException.class)
 public void testComputeValueWithNullArgument() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(1.0), new CoreNullExpression());
 EvalContext context = new EvalContext();
 coreOperationGreaterThan.computeValue(context);
 }

 // Test case for computeValue method with both null arguments
 @Test(expected = NullPointerException.class)
 public void testComputeValueWithBothNullArguments() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreNullExpression(), new CoreNullExpression());
 EvalContext context = new EvalContext();
 coreOperationGreaterThan.computeValue(context);
 }

 // Test case for getSymbol method
 @Test
 public void testGetSymbol() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 assertEquals(">", coreOperationGreaterThan.getSymbol());
 }
}