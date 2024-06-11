package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(1);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueNullArg() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueDifferentTypes() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }
}