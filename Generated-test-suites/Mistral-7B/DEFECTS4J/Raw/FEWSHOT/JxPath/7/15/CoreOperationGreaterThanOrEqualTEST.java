package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        Expression arg1 = new Const(5.5);
        Expression arg2 = new Const(4.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Const(4.5);
        arg2 = new Const(5.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(5.5);
        arg2 = new Const(5.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with negative double values
        arg1 = new Const(-5.5);
        arg2 = new Const(-6.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Const(-6.5);
        arg2 = new Const(-5.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(-5.5);
        arg2 = new Const(-5.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with nan and infinity
        arg1 = new Const(Double.NaN);
        arg2 = new Const(Double.POSITIVE_INFINITY);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new Const(Double.POSITIVE_INFINITY);
        arg2 = new Const(Double.NaN);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new Const(Double.POSITIVE_INFINITY);
        arg2 = new Const(Double.POSITIVE_INFINITY);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Const(Double.NEGATIVE_INFINITY);
        arg2 = new Const(Double.NEGATIVE_INFINITY);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}