package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertNotNull(operation);
        assertEquals("=", operation.getSymbol());
    }

    @Test
    public void testComputeValue_true() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "5");
        arg2.setValue(context, "5");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_false() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "5");
        arg2.setValue(context, "6");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_null() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, null);
        arg2.setValue(context, null);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_objectAndNumber() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, new Object());
        arg2.setValue(context, 5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValue_numberAndObject() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, 5);
        arg2.setValue(context, new Object());
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValue_stringAndNumber() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "5");
        arg2.setValue(context, 5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_numberAndString() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, 5);
        arg2.setValue(context, "5");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_stringAndString() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "foo");
        arg2.setValue(context, "foo");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_stringAndDifferentString() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "foo");
        arg2.setValue(context, "bar");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_nullAndString() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, null);
        arg2.setValue(context, "foo");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_stringAndNull() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, "foo");
        arg2.setValue(context, null);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_invalidType() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        arg1.setValue(context, new Object());
        arg2.setValue(context, new Object());
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        operation.computeValue(context);
    }
}