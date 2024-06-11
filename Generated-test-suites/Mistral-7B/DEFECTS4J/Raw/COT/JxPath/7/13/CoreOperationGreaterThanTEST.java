package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.0)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithInvalidArguments() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Double(5.0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithInvalidArguments2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(null));
    }

    @Test
    public void testCoreOperationGreaterThanWithMixedTypes() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(Integer.valueOf(3)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNaN() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(Double.NaN),
                new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithInf() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(Double.POSITIVE_INFINITY),
                new CoreConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroAndNegativeZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(0.0)),
                new CoreConstantExpression(new Double(-0.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroAndZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(0.0)),
                new CoreConstantExpression(new Double(0.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeZeroAndZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(-0.0)),
                new CoreConstantExpression(new Double(0.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroAndPositiveNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(0.0)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeNumberAndZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(0.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeNumberAndNegativeNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(-3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeNumberAndPositiveNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithPositiveNumberAndZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(0.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithPositiveNumberAndNegativeNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(-3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithPositiveNumberAndPositiveNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }
}