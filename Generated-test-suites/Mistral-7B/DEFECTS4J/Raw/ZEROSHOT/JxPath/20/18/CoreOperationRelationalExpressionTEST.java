package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InitialContext;
import org.apache.commons.jxpath.ri.SelfContext;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new ArrayExpression(new LiteralExpression(new Object[]{1}));
        args[1] = new ArrayExpression(new LiteralExpression(new Object[]{2}));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertEquals("CoreOperationGreaterThan", expression.getClass().getSimpleName());
    }

    /**
     * Test computeValue method with Boolean operands.
     */
    @Test
    public void testComputeValueBoolean() {
        Expression left = new LiteralExpression(Boolean.TRUE);
        Expression right = new LiteralExpression(Boolean.FALSE);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        EvalContext context = new EvalContext(Axis.SELF);
        Object result = expression.computeValue(context);
        assertTrue("Result must be false", result == Boolean.FALSE);

        left = new LiteralExpression(Boolean.FALSE);
        right = new LiteralExpression(Boolean.TRUE);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);

        left = new LiteralExpression(Boolean.TRUE);
        right = new LiteralExpression(Boolean.TRUE);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);

        left = new LiteralExpression(Boolean.FALSE);
        right = new LiteralExpression(Boolean.FALSE);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertTrue("Result must be true", result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with Number operands.
     */
    @Test
    public void testComputeValueNumber() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        EvalContext context = new EvalContext(Axis.SELF);
        Object result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);

        left = new LiteralExpression(2.0);
        right = new LiteralExpression(1.0);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertTrue("Result must be true", result == Boolean.TRUE);

        left = new LiteralExpression(1.0);
        right = new LiteralExpression(1.0);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);

        left = new LiteralExpression(Double.NaN);
        right = new LiteralExpression(Double.NaN);
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with Iterator operands.
     */
    @Test
    public void testComputeValueIterator() {
        Expression left = new ArrayExpression(new LiteralExpression(new Object[]{1, 2, 3, 4}));
        Expression right = new ArrayExpression(new LiteralExpression(new Object[]{2, 3, 4, 5}));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        EvalContext context = new EvalContext(Axis.SELF);
        Object result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);

        left = new ArrayExpression(new LiteralExpression(new Object[]{1, 2, 3, 4, 5}));
        right = new ArrayExpression(new LiteralExpression(new Object[]{2, 3, 4, 5}));
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertTrue("Result must be true", result == Boolean.TRUE);

        left = new ArrayExpression(new LiteralExpression(new Object[]{1, 2, 3, 4}));
        right = new ArrayExpression(new LiteralExpression(new Object[]{1, 2, 3, 4}));
        expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        context = new EvalContext(Axis.SELF);
        result = expression.computeValue(context);
        assertFalse("Result must be false", result == Boolean.TRUE);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals("Precedence must be RELATIONAL_EXPR_PRECEDENCE", RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertFalse("Operation must not be symmetric", expression.isSymmetric());
    }

    /**
     * Test evaluateCompare method with Integer compare.
     */
    private boolean evaluateCompareInt(int compare) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        return expression.evaluateCompare(compare);
    }

    @Test
    public void testEvaluateCompareIntGreaterThan() {
        assertTrue("Compare result must be true for compare > 0", evaluateCompareInt(1) == true);
    }

    @Test
    public void testEvaluateCompareIntLessThan() {
        assertFalse("Compare result must be false for compare < 0", evaluateCompareInt(-1) == true);
    }

    @Test
    public void testEvaluateCompareIntEqual() {
        assertFalse("Compare result must be false for compare = 0", evaluateCompareInt(0) == true);
    }

    /**
     * Test evaluateCompare method with Double compare.
     */
    private boolean evaluateCompareDouble(double compare) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        return expression.evaluateCompare(Double.compare(0.0, compare));
    }

    @Test
    public void testEvaluateCompareDoubleGreaterThan() {
        assertTrue("Compare result must be true for compare > 0", evaluateCompareDouble(1.0) == true);
    }

    @Test
    public void testEvaluateCompareDoubleLessThan() {
        assertFalse("Compare result must be false for compare < 0", evaluateCompareDouble(-1.0) == true);
    }

    @Test
    public void testEvaluateCompareDoubleEqual() {
        assertTrue("Compare result must be true for compare = 0", evaluateCompareDouble(0.0) == true);
    }

}