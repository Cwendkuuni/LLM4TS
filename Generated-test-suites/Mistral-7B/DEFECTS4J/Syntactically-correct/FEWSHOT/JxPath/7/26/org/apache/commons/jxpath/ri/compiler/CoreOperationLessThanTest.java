package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanFalse() {
        Expression arg1 = new LiteralExpression(2.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanZero() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanZeroFalse() {
        Expression arg1 = new LiteralExpression(-2.0);
        Expression arg2 = new LiteralExpression(-1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanNullArgument() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }
}