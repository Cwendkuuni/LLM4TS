package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertSame(CoreOperationEqual.class, coreOperationEqual.getClass());
    }

    @Test
    public void testComputeValueWithEqualValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithUnequalValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullAndNonNullValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testComputeValueWithFunctions() {
        EvalContext context = new EvalContext();
        Expression arg1 = new FunctionCallExpression("fn:string-join", new Expression[] { new ConstantExpression("Hello"), new ConstantExpression("World") });
        Expression arg2 = Functions.eq(new ConstantExpression("1"), new ConstantExpression(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentNullException() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, new ConstantExpression(1));
    }
}