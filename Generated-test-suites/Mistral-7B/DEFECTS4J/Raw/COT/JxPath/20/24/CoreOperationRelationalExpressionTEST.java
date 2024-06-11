package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link CoreOperationRelationalExpression} class
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);

        EvalContext context = new EvalContext(new InitialContext(Collections.emptyList()));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        Expression[] argsNegative = new Expression[2];
        argsNegative[0] = new CoreOperationLiteralExpression(2);
        argsNegative[1] = new CoreOperationLiteralExpression(1);
        CoreOperationRelationalExpression expressionNegative = new CoreOperationGtExpression(argsNegative);

        Object resultNegative = expressionNegative.computeValue(context);
        assertTrue(resultNegative instanceof Boolean);
        assertFalse((Boolean) resultNegative);

        Expression[] argsSame = new Expression[2];
        argsSame[0] = new CoreOperationLiteralExpression(1);
        argsSame[1] = new CoreOperationLiteralExpression(1);
        CoreOperationRelationalExpression expressionSame = new CoreOperationGtExpression(argsSame);

        Object resultSame = expressionSame.computeValue(context);
        assertTrue(resultSame instanceof Boolean);
        assertFalse((Boolean) resultSame);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        int precedence = expression.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = AssertionError.class)
    public void testEvaluateCompareThrowsAssertionErrorForInvalidCompare() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        expression.evaluateCompare(0);
    }

    @Test
    public void testEvaluateCompareReturnsTrueForGreaterThan() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(2);
        args[1] = new CoreOperationLiteralExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompareReturnsFalseForLessThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationLiteralExpression(1);
        args[1] = new CoreOperationLiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(args);
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeWithIterators() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(2, 3, 4).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 4)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithIteratorsDifferentSizes() {
        Iterator left = Arrays.asList(1, 2).iterator();
        Iterator right = Arrays.asList(2, 3, 4).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 4)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeWithIteratorsSameElements() {
        Iterator left = Arrays.asList(1, 2).iterator();
        Iterator right = Arrays.asList(2, 1).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 2, 1)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeWithIteratorsSameElementsInReverseOrder() {
        Iterator left = Arrays.asList(2, 1).iterator();
        Iterator right = Arrays.asList(1, 2).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 2, 1)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeWithIteratorsSameElementsInDifferentOrders() {
        Iterator left = Arrays.asList(2, 1).iterator();
        Iterator right = Arrays.asList(1, 3).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 1, 2)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeWithIteratorsMatchingElement() {
        Iterator left = Arrays.asList(2, 1).iterator();
        Iterator right = Arrays.asList(1, 2).iterator();

        Expression[] args = new Expression[2];
        args[0] = new CoreOperationInitialContextExpression();
        args[1] = new CoreOperationRelationalExpression(new CoreOperationIteratorExpression(left),
                new CoreOperationIteratorExpression(right), new CoreOperationGtExpression());

        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 2, 1)));
        Object result = ((CoreOperationRelationalExpression) args[1]).computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeWithNaN() {
        Expression left = new CoreOperationLiteralExpression(Double.NaN);
        Expression right = new CoreOperationLiteralExpression(Double.NaN);
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(new Expression[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(Collections.emptyList()));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

}