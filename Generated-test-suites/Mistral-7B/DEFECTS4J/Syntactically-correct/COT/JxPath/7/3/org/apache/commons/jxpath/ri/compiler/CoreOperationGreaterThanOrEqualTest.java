package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    // Test methods for CoreOperationGreaterThanOrEqual class

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(5.0),
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithIncompatibleTypes() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(5.0),
                new CoreExpressionConstant("5.0"));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg1() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                null,
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        coreOperation.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg2() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(5.0),
                null);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperation.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(-3.0),
                new CoreExpressionConstant(-2.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(0.0),
                new CoreExpressionConstant(0.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithPositiveValues() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(5.0),
                new CoreExpressionConstant(4.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEqualValues() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(5.0),
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(Double.NaN),
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithInf() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(Double.POSITIVE_INFINITY),
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegInf() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionConstant(Double.NEGATIVE_INFINITY),
                new CoreExpressionConstant(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

}