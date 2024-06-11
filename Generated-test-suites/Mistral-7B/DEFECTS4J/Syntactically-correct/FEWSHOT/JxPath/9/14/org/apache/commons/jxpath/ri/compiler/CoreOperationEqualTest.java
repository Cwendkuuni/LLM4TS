package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Values;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_BooleanValues() {
        Expression arg1 = Values.createBooleanValue(true);
        Expression arg2 = Values.createBooleanValue(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg2 = Values.createBooleanValue(false);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationEqual_NumberValues() {
        Expression arg1 = Values.createNumberValue(1.0);
        Expression arg2 = Values.createNumberValue(1.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg2 = Values.createNumberValue(2.0);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationEqual_StringValues() {
        Expression arg1 = Values.createStringValue("foo");
        Expression arg2 = Values.createStringValue("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg2 = Values.createStringValue("bar");
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationEqual_MixedValues() {
        Expression arg1 = Values.createNumberValue(1.0);
        Expression arg2 = Values.createStringValue("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = Values.createStringValue("foo");
        arg2 = Values.createNumberValue(1.0);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }
}