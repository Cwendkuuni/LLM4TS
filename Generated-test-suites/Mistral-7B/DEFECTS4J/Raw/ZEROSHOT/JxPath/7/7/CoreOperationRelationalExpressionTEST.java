package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.ComparisonType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeValue;
import org.apache.commons.jxpath.ri.compiler.Value;

/**
 * Test for CoreOperationRelationalExpression class.
 *
 * @author John Doe
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertNotNull(expression);
        assertEquals(ComparisonType.GT, expression.getComparisonType());
        assertEquals(args.length, expression.getArguments().length);
        assertArrayEquals(args, expression.getArguments());
        assertEquals(getPrecedence(expression), 3);
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test getPrecedence method.
     */
    @org.junit.Test
    public void testGetPrecedence() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertEquals(3, expression.getPrecedence());

        Expression expression2 = new CoreOperationLessThan(args);
        assertEquals(3, expression2.getPrecedence());
    }

    /**
     * Test isSymmetric method.
     */
    @org.junit.Test
    public void testIsSymmetric() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertFalse(expression.isSymmetric());

        CoreOperationRelationalExpression expression2 = new CoreOperationLessThan(args);
        assertFalse(expression2.isSymmetric());

        CoreOperationRelationalExpression expression3 = new CoreOperationGreaterThanOrEqual(args);
        assertFalse(expression3.isSymmetric());

        CoreOperationRelationalExpression expression4 = new CoreOperationLessThanOrEqual(args);
        assertFalse(expression4.isSymmetric());
    }

    /**
     * Test evaluate method for CoreOperationGreaterThan expression.
     */
    @org.junit.Test
    public void testEvaluateGreaterThan() {
        Expression arg1 = new LiteralExpression(3);
        Expression arg2 = new LiteralExpression(2);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        JXPathContext context = new JXPathContext();
        Value result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg2, context), new NodeValue(arg1, context) });
        result = expression.evaluate(context, new NodeValue(arg2, context));
        assertFalse(result.getValue() instanceof Boolean);
        assertNull(result.getValue());
    }

    /**
     * Test evaluate method for CoreOperationGreaterThanOrEqual expression.
     */
    @org.junit.Test
    public void testEvaluateGreaterThanOrEqual() {
        Expression arg1 = new LiteralExpression(3);
        Expression arg2 = new LiteralExpression(2);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(args);
        JXPathContext context = new JXPathContext();
        Value result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg2, context), new NodeValue(arg1, context) });
        result = expression.evaluate(context, new NodeValue(arg2, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg1, context), new NodeValue(arg2, context) });
        result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());
    }

    /**
     * Test evaluate method for CoreOperationLessThan expression.
     */
    @org.junit.Test
    public void testEvaluateLessThan() {
        Expression arg1 = new LiteralExpression(2);
        Expression arg2 = new LiteralExpression(3);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(args);
        JXPathContext context = new JXPathContext();
        Value result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg2, context), new NodeValue(arg1, context) });
        result = expression.evaluate(context, new NodeValue(arg2, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((Boolean) result.getValue());
    }

    /**
     * Test evaluate method for CoreOperationLessThanOrEqual expression.
     */
    @org.junit.Test
    public void testEvaluateLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(2);
        Expression arg2 = new LiteralExpression(3);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(args);
        JXPathContext context = new JXPathContext();
        Value result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg2, context), new NodeValue(arg1, context) });
        result = expression.evaluate(context, new NodeValue(arg2, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());

        context.setValues(new Value[] { new NodeValue(arg1, context), new NodeValue(arg2, context) });
        result = expression.evaluate(context, new NodeValue(arg1, context));
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((Boolean) result.getValue());
    }

    /**
     * Get precedence for given expression.
     *
     * @param expression expression
     * @return int
     */
    private int getPrecedence(CoreOperationRelationalExpression expression) {
        return expression.getPrecedence();
    }

    /**
     * Assert that arrays are equal.
     *
     * @param expected expected array
     * @param actual actual array
     */
    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Arrays have different lengths.");
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Arrays are not equal.");
            }
        }
    }

}