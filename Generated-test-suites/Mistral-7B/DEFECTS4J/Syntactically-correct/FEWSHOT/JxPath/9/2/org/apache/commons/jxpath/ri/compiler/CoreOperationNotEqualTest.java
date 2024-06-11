package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(2);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = null;
        arg2 = new LiteralExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(context));

        arg1 = new LiteralExpression(1);
        arg2 = null;
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(context));

        arg1 = new LiteralExpression("foo");
        arg2 = new LiteralExpression("bar");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression("foo");
        arg2 = new LiteralExpression("foo");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(true);
        arg2 = new LiteralExpression(false);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(false);
        arg2 = new LiteralExpression(true);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(new byte[] { 1 });
        arg2 = new LiteralExpression(new byte[] { 2 });
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(new byte[] { 1 });
        arg2 = new LiteralExpression(new byte[] { 1 });
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new LiteralExpression(1), new LiteralExpression(2));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}