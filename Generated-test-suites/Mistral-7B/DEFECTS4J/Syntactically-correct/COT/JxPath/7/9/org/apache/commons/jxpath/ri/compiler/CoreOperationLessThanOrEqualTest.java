package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_false() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(2.0), new CoreConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_zero() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_nonNumeric() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression("str"), new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext();
        coreOperationLessThanOrEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_nonNumeric2() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(2.0), new CoreConstantExpression("str"));
        EvalContext context = new EvalContext();
        coreOperationLessThanOrEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_nullArg() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                null, new CoreConstantExpression(2.0));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_negative() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(-2.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_negativeAndPositive() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_veryLargeNegativeAndPositive() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-Double.MAX_VALUE),
                new CoreConstantExpression(Double.MAX_VALUE));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_veryLargePositiveAndNegative() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.MAX_VALUE),
                new CoreConstantExpression(-Double.MAX_VALUE));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_NaN() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_NaN2() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);
    }
}