package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.expr.ExpressionTestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = ExpressionTestHelper.createConstantExpression(1);
        Expression arg2 = ExpressionTestHelper.createConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes1() {
        Expression arg1 = ExpressionTestHelper.createConstantExpression(1);
        Expression arg2 = ExpressionTestHelper.createConstantExpression("one");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes2() {
        Expression arg1 = ExpressionTestHelper.createConstantExpression(1.0);
        Expression arg2 = ExpressionTestHelper.createConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationEqualFalse() {
        Expression arg1 = ExpressionTestHelper.createConstantExpression(1);
        Expression arg2 = ExpressionTestHelper.createConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualTrue() {
        Expression arg1 = ExpressionTestHelper.createConstantExpression("one");
        Expression arg2 = ExpressionTestHelper.createConstantExpression("one");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}