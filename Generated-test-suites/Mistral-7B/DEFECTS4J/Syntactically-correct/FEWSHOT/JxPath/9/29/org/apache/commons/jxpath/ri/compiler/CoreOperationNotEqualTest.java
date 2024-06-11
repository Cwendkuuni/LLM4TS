package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(false);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean_True() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(true);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean_False() {
        Expression arg1 = new CoreConstantExpression(false);
        Expression arg2 = new CoreConstantExpression(false);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean_MismatchedTypes() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreVariableExpression("x");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof RuntimeException);
        assertTrue(result.getMessage().contains("Types do not match"));
    }

    @Test
    public void testCoreOperationNotEqual_Number_Number() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantExpression(2.0);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Number_Number_NotEqual() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantExpression(2.0);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Number_Number_Equal() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantExpression(1.0);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_Number_String() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreStringExpression("two");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof RuntimeException);
        assertTrue(result.getMessage().contains("Types do not match"));
    }

    @Test
    public void testCoreOperationNotEqual_String_String() {
        Expression arg1 = new CoreStringExpression("one");
        Expression arg2 = new CoreStringExpression("two");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_String_String_Equal() {
        Expression arg1 = new CoreStringExpression("one");
        Expression arg2 = new CoreStringExpression("one");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqual_String_String_MismatchedTypes() {
        Expression arg1 = new CoreStringExpression("one");
        Expression arg2 = new CoreNumberExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof RuntimeException);
        assertTrue(result.getMessage().contains("Types do not match"));
    }

    @Test
    public void testCoreOperationNotEqual_String_Variable() {
        Expression arg1 = new CoreStringExpression("one");
        Expression arg2 = new CoreVariableExpression("x");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.set("x", "two");
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

}