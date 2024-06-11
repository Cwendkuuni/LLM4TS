package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

 private CoreOperationEqual coreOperationEqual;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = new LiteralExpression(TestUtils.createJXPathContext(), 5);
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), 5);
 coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 context = TestUtils.createEmptyContext();
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreSame_thenReturnTrue() {
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsDifferentFromSecondExpression_thenReturnFalse() {
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), 6);
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsNullAndSecondExpressionIsNotNull_thenReturnFalse() {
 arg1 = new LiteralExpression(TestUtils.createJXPathContext(), null);
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsNotNullAndSecondExpressionIsNull_thenReturnFalse() {
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), null);
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsNumberAndSecondExpressionIsString_thenReturnFalse() {
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), "5");
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsStringAndSecondExpressionIsNumber_thenReturnFalse() {
 arg1 = new LiteralExpression(TestUtils.createJXPathContext(), "5");
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreDifferentTypes_thenReturnFalse() {
 arg1 = new LiteralExpression(TestUtils.createJXPathContext(), 5);
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), "5");
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreSameTypeButDifferentValues_thenReturnFalse() {
 arg1 = new LiteralExpression(TestUtils.createJXPathContext(), 5);
 arg2 = new LiteralExpression(TestUtils.createJXPathContext(), 6);
 Object result = coreOperationEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGetSymbol_whenInvoked_thenReturnEqualSign() {
 String symbol = coreOperationEqual.getSymbol();
 Assert.assertEquals("=", symbol);
 }
}