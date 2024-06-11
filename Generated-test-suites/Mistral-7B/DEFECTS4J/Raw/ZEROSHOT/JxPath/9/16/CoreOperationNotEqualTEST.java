package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CoreOperationNotEqual} class.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test empty context.
     */
    @Test
    public void testEmptyContext() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(null);
        Expression arg2 = ExpressionFactory.newLiteralExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    /**
     * Test equal values.
     */
    @Test
    public void testEqualValues() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    /**
     * Test not equal values.
     */
    @Test
    public void testNotEqualValues() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    /**
     * Test different types of expressions.
     */
    @Test
    public void testDifferentTypes() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(Integer.valueOf(1));
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newLiteralExpression("str1");
        arg2 = ExpressionFactory.newLiteralExpression(Integer.valueOf(1));
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newVariableExpression("var1");
        arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newLiteralExpression(Integer.valueOf(1));
        arg2 = ExpressionFactory.newVariableExpression("var1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNull(coreOperationCompare.computeValue(new EvalContext()));
    }

    /**
     * Test null arguments.
     */
    @Test
    public void testNullArguments() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(null);
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newVariableExpression("var1");
        arg2 = ExpressionFactory.newLiteralExpression(null);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newLiteralExpression(null);
        arg2 = ExpressionFactory.newVariableExpression("var1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    /**
     * Test null expression.
     */
    @Test
    public void testNullExpression() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, ExpressionFactory.newLiteralExpression(Boolean.TRUE));
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = ExpressionFactory.newVariableExpression("var1");
        coreOperationNotEqual = new CoreOperationNotEqual(null, ExpressionFactory.newLiteralExpression(Boolean.TRUE));
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    /**
     * Test invalid expression.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression() {
        Expression arg1 = ExpressionFactory.newInvalidExpression();
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        new CoreOperationNotEqual(arg1, arg2);
    }

    /**
     * Test invalid context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidContext() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        Expression arg2 = ExpressionFactory.newLiteralExpression(Boolean.TRUE);
        new CoreOperationNotEqual(arg1, arg2);
        assertFalse(EvalContext.class.isInstance(null));
    }
}