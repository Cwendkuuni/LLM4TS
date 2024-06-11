package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionFactory;
import org.apache.commons.jxpath.ri.functions.FunctionEvaluator;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createNumber(1);
        Expression arg2 = factory.createNumber(1);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", operation.getSymbol());
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext ctx = new EvalContext();
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createNumber(1);
        Expression arg2 = factory.createNumber(1);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext ctx = new EvalContext();
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createNumber(1);
        Expression arg2 = factory.createNumber(2);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueNull() {
        EvalContext ctx = new EvalContext();
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createNumber(1);
        Expression arg2 = factory.createVariable("null");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(ctx);
        assertNull(result);
    }

    @Test
    public void testComputeValueFunction() {
        EvalContext ctx = new EvalContext();
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createFunctionCall(new FunctionEvaluator.FunctionName("sqrt"), factory.createNumber(4));
        Expression arg2 = factory.createNumber(2);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(ctx);
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputeValueUnsupported() {
        EvalContext ctx = new EvalContext();
        ExpressionFactory factory = new ExpressionFactory();
        Expression arg1 = factory.createNumber(1);
        Expression arg2 = factory.createFunctionCall(new FunctionEvaluator.FunctionName("sqrt"), factory.createNumber(2));
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        operation.computeValue(ctx);
    }
}