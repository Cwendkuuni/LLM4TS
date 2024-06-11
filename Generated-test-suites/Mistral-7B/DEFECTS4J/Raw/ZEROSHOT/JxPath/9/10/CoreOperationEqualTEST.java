package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_sameObjects() {
        Expression arg1 = new LiteralExpression(new Integer(5));
        Expression arg2 = new LiteralExpression(new Integer(5));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object actual = coreOperationEqual.computeValue(context);
        assertTrue(actual.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_differentObjects() {
        Expression arg1 = new LiteralExpression(new Integer(5));
        Expression arg2 = new LiteralExpression(new Integer(6));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object actual = coreOperationEqual.computeValue(context);
        assertFalse(actual.equals(Boolean.TRUE));
        assertTrue(actual.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_oneNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Integer(5));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object actual = coreOperationEqual.computeValue(context);
        assertFalse(actual.equals(Boolean.TRUE));
        assertTrue(actual.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_bothNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object actual = coreOperationEqual.computeValue(context);
        assertTrue(actual.equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(new Integer(5)), new LiteralExpression(new Integer(5)));
        String actual = coreOperationEqual.getSymbol();
        String expected = "=";
        assertEquals(expected, actual);
    }
}