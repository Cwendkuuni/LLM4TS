package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(2.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(1.5);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.5);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(1.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(null);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new ConstantExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new VariableExpression("foo");
        arg2 = new LiteralExpression(2.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertNull(result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new VariableExpression("foo");
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertNull(result);

        arg1 = new ConstantExpression(2.0);
        arg2 = new VariableExpression("foo");
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new VariableExpression("foo");
        arg2 = new VariableExpression("bar");
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        op = new CoreOperationLessThan(arg1, arg2);

        assertEquals(op.getSymbol(), "<");
    }

    @Test
    public void testCoreOperationLessThanSymbol() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        assertEquals(op.getSymbol(), "<");
    }
}