package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.expr.Variable;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertNotNull(coreOperationNotEqual);
        assertTrue(coreOperationNotEqual instanceof CoreOperationNotEqual);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", 1);
        context.set("arg2", 2);

        Object result = coreOperationNotEqual.computeValue(context);

        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", 1);
        context.set("arg2", 1);

        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueNull() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", null);
        context.set("arg2", null);

        Object result = coreOperationNotEqual.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueArg1Null() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", null);
        context.set("arg2", 1);

        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueArg2Null() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("arg1", 1);
        context.set("arg2", null);

        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(((Boolean) result).booleanValue());
    }
}