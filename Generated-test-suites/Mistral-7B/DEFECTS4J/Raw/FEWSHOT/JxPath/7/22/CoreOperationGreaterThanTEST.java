package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertNull(value);
    }

    @Test
    public void testCoreOperationGreaterThanWithEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertFalse(value instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanWithNotEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeValues() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertFalse(value instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroAndNegative() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(-1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithDoubleValues() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithFloatValues() {
        Expression arg1 = new LiteralExpression(1.5f);
        Expression arg2 = new LiteralExpression(1.0f);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanWithMixedTypes() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression("2.5");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanWithInvalidTypes() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(new Object());
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
    }
}