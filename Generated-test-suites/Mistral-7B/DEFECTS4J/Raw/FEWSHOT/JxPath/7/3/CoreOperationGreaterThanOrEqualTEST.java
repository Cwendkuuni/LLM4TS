package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Processor;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Processor());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualFalse() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Processor());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Processor());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Processor());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, null);

        context = new EvalContext(new Processor());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualString() {
        Expression arg1 = new StringLiteralExpression("1.5");
        Expression arg2 = new StringLiteralExpression("1.0");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Processor());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}