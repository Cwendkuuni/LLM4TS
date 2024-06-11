package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test case 1: Basic functionality of the constructor and computeValue method with valid arguments.
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanWithNaN() {
        // Test case 2: Edge case - Check for NaN values.
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanWithInfinities() {
        // Test case 3: Edge case - Check for infinities.
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new LiteralExpression(-Double.POSITIVE_INFINITY);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    // Test case 4: Error case - Check for null arguments.
    public void testCoreOperationLessThanWithNullArgs() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }
}