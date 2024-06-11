package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        // test with boolean arguments
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", Boolean.TRUE);
        context.set("arg2", Boolean.TRUE);
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", Boolean.TRUE);
        context.set("arg2", Boolean.FALSE);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", Boolean.FALSE);
        context.set("arg2", Boolean.TRUE);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", Boolean.FALSE);
        context.set("arg2", Boolean.FALSE);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // test with nullable arguments
        arg1 = new Variable("arg1");
        arg2 = new Variable("arg2");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        context.set("arg1", null);
        context.set("arg2", null);
        result = coreOperationNotEqual.computeValue(context);
        assertNull(result);

        context.set("arg1", null);
        context.set("arg2", Boolean.FALSE);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);

        context.set("arg1", Boolean.FALSE);
        context.set("arg2", null);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);

        context.set("arg1", Boolean.TRUE);
        context.set("arg2", null);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);

        // test with different types of arguments
        arg1 = new Variable("arg1");
        arg2 = new Variable("arg2");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        context.set("arg1", "1");
        context.set("arg2", "2");
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", 1);
        context.set("arg2", 2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", 1);
        context.set("arg2", "2");
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", "1");
        context.set("arg2", 2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", new Object());
        context.set("arg2", new Object());
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", new Object());
        context.set("arg2", new String());
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", new String());
        context.set("arg2", new Object());
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.set("arg1", new String());
        context.set("arg2", new String());
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);

        context.set("arg1", new Integer(1));
        context.set("arg2", new Integer(2));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", new Integer(1));
        context.set("arg2", new Integer(1));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", new Integer(1));
        context.set("arg2", new Double(1.0));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", new Double(1.0));
        context.set("arg2", new Integer(1));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("arg1", new Double(1.0));
        context.set("arg2", new Double(1.0));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualInvalidArguments() {
        // test with invalid number of arguments
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null);
        assertNull(coreOperationNotEqual);

        coreOperationNotEqual = new CoreOperationNotEqual();
        assertNull(coreOperationNotEqual);

        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2, new Variable("arg3"));
        assertNotSame(coreOperationNotEqual1.getClass(), CoreOperationNotEqual.class);
    }
}