package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new CoreConstantExpression(1);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result instanceof Number);
    }

    @Test
    public void testCoreOperationNotEqualWithComplexExpressions() {
        Expression arg1 = new CoreVariableExpression("x");
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setVariable("x", new CoreConstantExpression(1));
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("x", new CoreConstantExpression(2));
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result instanceof Number);

        arg2 = new CoreVariableExpression("y");
        context.setVariable("y", new CoreConstantExpression(1));
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result instanceof Number);

        context.setVariable("y", new CoreConstantExpression(2));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}