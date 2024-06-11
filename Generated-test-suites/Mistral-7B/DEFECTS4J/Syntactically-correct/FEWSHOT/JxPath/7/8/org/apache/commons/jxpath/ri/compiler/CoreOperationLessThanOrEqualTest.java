package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(null, null);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new LiteralExpression(1.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNegativeValues() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(null, null);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new LiteralExpression(-1.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(null, null);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg2 = new LiteralExpression(0.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(null, null);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNullArguments() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(null, null);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, null);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);
    }
}