package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(5.0)),
                new CoreNumberExpression(new DoubleExpression(4.0)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualFalse() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(4.0)),
                new CoreNumberExpression(new DoubleExpression(5.0)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualNullArg1() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                null,
                new CoreNumberExpression(new DoubleExpression(5.0)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertNull(value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualNullArg2() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(5.0)),
                null);

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertNull(value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualBothNull() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                null,
                null);

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertNull(value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualDifferentTypes() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreStringExpression(new StringExpression("5")),
                new CoreNumberExpression(new DoubleExpression(4.0)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertNull(value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualDoubleAndDouble() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(5.5)),
                new CoreNumberExpression(new DoubleExpression(4.4)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualDoubleAndInt() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(5.5)),
                new CoreNumberExpression(new NumberExpression(new Integer(4))));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualDoubleAndString() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new DoubleExpression(5.5)),
                new CoreStringExpression(new StringExpression("4.5")));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualIntAndDouble() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new NumberExpression(new Integer(5))),
                new CoreNumberExpression(new DoubleExpression(4.4)));

        EvalContext context = new EvalContext();
        Object value = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }
}