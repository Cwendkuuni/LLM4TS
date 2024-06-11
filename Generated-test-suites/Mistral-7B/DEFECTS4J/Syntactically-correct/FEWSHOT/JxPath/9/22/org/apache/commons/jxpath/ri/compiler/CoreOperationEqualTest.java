package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testComputeValueWithEqualValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNotEqualValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullValues() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertSame(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNullAndValidValue() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithValidAndNullValue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringLiteralExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithTypesConvertedToEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new NumberLiteralExpression(1.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithInvalidTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new BooleanLiteralExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }
}