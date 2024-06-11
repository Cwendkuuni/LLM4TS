package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new NumberNode(1.5);
        Expression arg2 = new NumberNode(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new NumberNode(1.0);
        arg2 = new NumberNode(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        Expression arg1 = new NumberNode(0.0);
        Expression arg2 = new NumberNode(0.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new NumberNode(-0.0);
        arg2 = new NumberNode(0.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new NumberNode(0.0);
        arg2 = new NumberNode(-0.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new NumberNode(0.0);
        arg2 = new NumberNode(NaN);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new NumberNode(NaN);
        arg2 = new NumberNode(0.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        Expression arg1 = new NumberNode(Double.NaN);
        Expression arg2 = new NumberNode(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new NumberNode(Double.NaN);
        arg2 = new NumberNode(1.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new NumberNode(1.0);
        arg2 = new NumberNode(Double.NaN);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidArguments() {
        Expression arg1 = new NumberNode(1.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}