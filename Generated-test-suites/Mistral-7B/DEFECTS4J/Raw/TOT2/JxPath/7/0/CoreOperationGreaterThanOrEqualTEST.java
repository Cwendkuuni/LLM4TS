package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqualConstructor() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationGreaterThanOrEqual);
    }

    @Test
    public void testComputeValueWithValidValues() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testComputeValueWithInvalidValues() {
        Expression arg1 = new LiteralExpression(2.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testGetSymbolMethod() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new LiteralExpression(1.0),new LiteralExpression(2.0));
        String result = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", result);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanOrEqualConstructorWithNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanOrEqualConstructorWithNullArg2() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueWithNegativeValues() {
        Expression arg1 = new LiteralExpression(-5.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testComputeValueWithNegativeAndPositiveValues() {
        Expression arg1 = new LiteralExpression(-2.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

}