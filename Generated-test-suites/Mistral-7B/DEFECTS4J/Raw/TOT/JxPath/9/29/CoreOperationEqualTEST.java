package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CoreOperationEqual class.
 */
public class CoreOperationEqualTest {

    @Test
    public void testEqualTrue() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualFalse() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualSameInstance() {
        Expression arg1 = new LiteralExpression(new Object());
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualNullAndNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullAndNotNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression("not null");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualDifferentTypes() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression("not a number");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentNull() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }
}