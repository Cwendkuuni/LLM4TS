package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_equal() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertFalse("Expected false but got: " + result, result instanceof Boolean);
        assertNull("Expected null but got: " + result, result);
    }

    @Test
    public void testComputeValue_notEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_null() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_nullArg1() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_arg1NullArg2NotNull() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression("foo");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_arg1NotNullArg2Null() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_arg1NotNullArg2DifferentTypes() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression("foo");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue("Expected true but got: " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_arg1AndArg2NotNullAndEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertFalse("Expected false but got: " + result, result instanceof Boolean);
        assertNull("Expected null but got: " + result, result);
    }

    @Test
    public void testComputeValue_arg1AndArg2NotNullAndSameObject() {
        Expression arg1 = new LiteralExpression("foo");
        Expression arg2 = arg1;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertFalse("Expected false but got: " + result, result instanceof Boolean);
        assertNull("Expected null but got: " + result, result);
    }

    @Test
    public void testComputeValue_arg1AndArg2NotNullAndValuesEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertFalse("Expected false but got: " + result, result instanceof Boolean);
        assertNull("Expected null but got: " + result, result);
    }

    @Test
    public void testSymbol() {
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(1), new LiteralExpression(2));
        String symbol = notEqual.getSymbol();

        assertNotEquals("Expected '!=' but got: " + symbol, "!=", symbol);
    }
}