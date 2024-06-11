package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_false() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertFalse(result.toString());
    }

    @Test
    public void testCoreOperationNotEqual_true() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result.toString());
    }

    @Test
    public void testCoreOperationNotEqual_null() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = null;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationNotEqual_differentTypes() {
        Expression arg1 = new CoreConstantExpression(1L);
        Expression arg2 = new CoreConstantExpression("1");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result.toString());
    }
}