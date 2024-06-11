package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);

        assertTrue(value.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationEqualFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);

        assertFalse(value.equals(Boolean.TRUE));
        assertEquals(value, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationEqualNull() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);

        assertFalse(value.equals(Boolean.TRUE));
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualNullValue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg2, arg1);

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);

        assertFalse(value.equals(Boolean.TRUE));
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression("1");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);

        assertFalse(value.equals(Boolean.TRUE));
        assertNull(value);
    }
}