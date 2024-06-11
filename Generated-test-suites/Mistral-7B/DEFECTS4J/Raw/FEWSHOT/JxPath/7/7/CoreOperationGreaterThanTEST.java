package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(3.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        Expression arg1 = new LiteralExpression(2.0);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNull() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanNullArg2() {
        Expression arg1 = new LiteralExpression(3.0);
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanZeroZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanZeroPositive() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanPositiveZero() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanPositivePositive() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanPositiveNegative() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(-5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNegativeZero() {
        Expression arg1 = new LiteralExpression(-5.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNegativeNegative() {
        Expression arg1 = new LiteralExpression(-5.0);
        Expression arg2 = new LiteralExpression(-5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNegativePositive() {
        Expression arg1 = new LiteralExpression(-5.0);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertNotNull(result);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanNaN2() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertNotNull(result);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanNullLiteral() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet());

        Object result = coreOperationGreaterThan.computeValue(context);

        assertNull(result);
    }
}