package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), new LiteralExpression(new Double(4.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_false() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(4.5)), new LiteralExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_null() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), null);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_null_arg1() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                null, new LiteralExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_null_arg2() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), null);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_doubles() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), new LiteralExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_doubles_equal() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), new LiteralExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_doubles_not_equal() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5.5)), new LiteralExpression(new Double(4.5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_integers() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(6)), new LiteralExpression(new Double(5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_integers_equal() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5)), new LiteralExpression(new Double(5)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_integers_not_equal() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(new Double(5)), new LiteralExpression(new Double(4)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}