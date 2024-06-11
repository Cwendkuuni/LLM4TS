package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with primitive types
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(InfoSetUtil.EMPTY_NODE_SET);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(1.0);
        arg2 = new CoreConstantExpression(1.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression(2.0);
        arg2 = new CoreConstantExpression(1.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with complex expressions
        arg1 = new CoreVariableExpression("x");
        arg2 = new CoreConstantExpression(2.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(InfoSetUtil.EMPTY_NODE_SET);
        context.setVariable("x", 1.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context.setVariable("x", 1.5);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreVariableExpression("x");
        arg2 = new CoreVariableExpression("y");
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(InfoSetUtil.EMPTY_NODE_SET);
        context.setVariable("x", 2.0);
        context.setVariable("y", 1.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context.setVariable("x", 2.0);
        context.setVariable("y", 2.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context.setVariable("x", 3.0);
        context.setVariable("y", 2.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context.setVariable("x", 3.0);
        context.setVariable("y", 3.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}