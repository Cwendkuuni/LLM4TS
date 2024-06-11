package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanFalse() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(3.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanNull() {
        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(null);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(3.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanZeroAndPositive() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(3.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanZeroAndNegative() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(-3.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanPositiveAndPositive() {
        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(4.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanPositiveAndZero() {
        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}