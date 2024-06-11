package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.JXPathContext;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;

/**
 * Test cases for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    private static Expression createExpression(String expression) {
        JXPathContext context = new JXPathContext();
        ExpressionFactory expressionFactory = context.getExpressionFactory();
        return expressionFactory.createExpression(expression);
    }

    @org.junit.Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("2");
        arg2 = createExpression("1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithNullArg() {
        Expression arg1 = null;
        Expression arg2 = createExpression("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithNullArg2() {
        Expression arg1 = createExpression("1");
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithSameValues() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithBooleanValues() {
        Expression arg1 = createExpression("true");
        Expression arg2 = createExpression("false");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression("false");
        arg2 = createExpression("true");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithStringValues() {
        Expression arg1 = createExpression("'a'");
        Expression arg2 = createExpression("'b'");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression("'b'");
        arg2 = createExpression("'a'");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNumberValues() {
        Expression arg1 = createExpression("1.0");
        Expression arg2 = createExpression("2.0");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("2.0");
        arg2 = createExpression("1.0");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithMixedValues() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("'a'");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("'a'");
        arg2 = createExpression("1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("true");
        arg2 = createExpression("1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("1");
        arg2 = createExpression("true");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNullValues() {
        Expression arg1 = createExpression(ExpressionUtil.NULL);
        Expression arg2 = createExpression(ExpressionUtil.NULL);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression(ExpressionUtil.NULL);
        arg2 = createExpression("1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression("1");
        arg2 = createExpression(ExpressionUtil.NULL);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression("true");
        arg2 = createExpression(ExpressionUtil.NULL);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = createExpression(ExpressionUtil.NULL);
        arg2 = createExpression("true");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithArrayValues() {
        Expression arg1 = createExpression("(1, 2, 3)");
        Expression arg2 = createExpression("(4, 5, 6)");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("(4, 5, 6)");
        arg2 = createExpression("(1, 2, 3)");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithListValues() {
        Expression arg1 = createExpression("[1, 2, 3]");
        Expression arg2 = createExpression("[4, 5, 6]");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = createExpression("[4, 5, 6]");
        arg2 = createExpression("[1, 2, 3]");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}