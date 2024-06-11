package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);

        left = new Integer(10);
        right = new Integer(5);
        expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        result = expression.computeValue(context);
        assertTrue(result == Boolean.TRUE);

        left = new Integer(5);
        right = new Integer(5);
        expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValueWithNaN() {
        Object left = Double.NaN;
        Object right = Double.NaN;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValueWithNull() {
        Object left = null;
        Object right = new Integer(5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);

        left = new Integer(5);
        right = null;
        expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValueWithSelfContext() {
        Object left = new SelfContext();
        Object right = new SelfContext();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        assertTrue(result == Boolean.FALSE);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithDifferentTypes() {
        Object left = new Integer(5);
        Object right = new String("5");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionConstant(left), new CoreExpressionConstant(right));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        fail("Should not reach this point");
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        int precedence = expression.getPrecedence();
        assertTrue(precedence == CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        boolean symmetric = expression.isSymmetric();
        assertTrue(symmetric == false);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        boolean result = expression.evaluateCompare(0);
        assertTrue(result == false);

        result = expression.evaluateCompare(-1);
        assertTrue(result == true);

        result = expression.evaluateCompare(1);
        assertTrue(result == true);
    }

    @Test
    public void testReduce() {
        Object value = new Integer(5);
        Object result = new CoreOperationRelationalExpression(new CoreExpressionConstant(value), new CoreExpressionConstant(value))
                .reduce(value);
        assertTrue(result == value);

        value = new SelfContext();
        result = new CoreOperationRelationalExpression(new CoreExpressionConstant(value), new CoreExpressionConstant(value))
                .reduce(value);
        assertTrue(result instanceof InitialContext);

        value = new Collection<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        result = new CoreOperationRelationalExpression(new CoreExpressionConstant(value), new CoreExpressionConstant(value))
                .reduce(value);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        List<Object> values = Arrays.asList(new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        Iterator<Object> it = values.iterator();
        Object valueToMatch = new Integer(2);
        boolean result = new CoreOperationRelationalExpression(it, new CoreExpressionConstant(valueToMatch))
                .containsMatch(it, valueToMatch);
        assertTrue(result == true);

        it = Collections.emptyIterator();
        result = new CoreOperationRelationalExpression(it, new CoreExpressionConstant(valueToMatch))
                .containsMatch(it, valueToMatch);
        assertTrue(result == false);

        it = values.iterator();
        valueToMatch = new String("2");
        result = new CoreOperationRelationalExpression(it, new CoreExpressionConstant(valueToMatch))
                .containsMatch(it, valueToMatch);
        assertTrue(result == false);
    }

    @Test
    public void testFindMatch() {
        List<Object> leftValues = Arrays.asList(new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        List<Object> rightValues = Arrays.asList(new Integer(2), new Integer(3), new Integer(4), new Integer(5));
        Iterator<Object> leftIt = leftValues.iterator();
        Iterator<Object> rightIt = rightValues.iterator();
        boolean result = new CoreOperationRelationalExpression(leftIt, rightIt)
                .findMatch(leftIt, rightIt);
        assertTrue(result == true);

        leftValues = Arrays.asList(new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5));
        rightValues = Arrays.asList(new Integer(2), new Integer(3), new Integer(4), new Integer(5));
        leftIt = leftValues.iterator();
        rightIt = rightValues.iterator();
        result = new CoreOperationRelationalExpression(leftIt, rightIt)
                .findMatch(leftIt, rightIt);
        assertTrue(result == true);

        leftValues = Arrays.asList(new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5));
        rightValues = Arrays.asList(new Integer(6), new Integer(7), new Integer(8), new Integer(9));
        leftIt = leftValues.iterator();
        rightIt = rightValues.iterator();
        result = new CoreOperationRelationalExpression(leftIt, rightIt)
                .findMatch(leftIt, rightIt);
        assertTrue(result == false);
    }

}