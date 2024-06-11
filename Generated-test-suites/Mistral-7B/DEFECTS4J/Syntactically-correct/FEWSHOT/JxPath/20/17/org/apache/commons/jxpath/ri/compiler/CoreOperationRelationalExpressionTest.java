package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression leftExp = new LiteralExpression(1.0);
        Expression rightExp = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new GreaterExpression(leftExp, rightExp);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        leftExp = new LiteralExpression(2.0);
        rightExp = new LiteralExpression(1.0);
        expression = new GreaterExpression(leftExp, rightExp);

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext();
        leftExp = new LiteralExpression(1.0);
        rightExp = new LiteralExpression(1.0);
        expression = new GreaterExpression(leftExp, rightExp);

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext();
        leftExp = new LiteralExpression(1.0);
        rightExp = new LiteralExpression(1.0);
        expression = new GreaterEqualExpression(leftExp, rightExp);

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new LiteralExpression(1.0), new LiteralExpression(2.0));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));

        expression = new GreaterEqualExpression(new LiteralExpression(1.0), new LiteralExpression(1.0));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(-1));

        expression = new LessExpression(new LiteralExpression(1.0), new LiteralExpression(2.0));
        assertFalse(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));

        expression = new LessEqualExpression(new LiteralExpression(1.0), new LiteralExpression(1.0));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeWithIterators() {
        Object left = new ArrayIterator(new Object[]{1, 2, 3, 4, 5});
        Object right = new SingletonIterator(1);

        CoreOperationRelationalExpression expression = new GreaterExpression(left, right);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new SingletonIterator(1);
        right = new ArrayIterator(new Object[]{2, 3, 4, 5, 6});

        expression = new GreaterExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SingletonIterator(1);
        right = new SingletonIterator(1);

        expression = new GreaterEqualExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new ArrayIterator(new Object[]{1, 2, 3, 4, 5});
        right = new SingletonIterator(5);

        expression = new LessExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SingletonIterator(5);
        right = new ArrayIterator(new Object[]{1, 2, 3, 4, 5});

        expression = new LessExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new ArrayIterator(new Object[]{1, 2, 3, 4, 5});
        right = new SingletonIterator(5);

        expression = new LessEqualExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new SingletonIterator(5);
        right = new ArrayIterator(new Object[]{1, 2, 3, 4, 5});

        expression = new LessEqualExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new SingletonIterator(5);
        right = new SingletonIterator(5);

        expression = new LessEqualExpression(left, right);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithDoubles() {
        Expression leftExp = new LiteralExpression(1.1);
        Expression rightExp = new LiteralExpression(2.2);

        CoreOperationRelationalExpression expression = new GreaterExpression(leftExp, rightExp);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(2.2);
        rightExp = new LiteralExpression(1.1);

        expression = new GreaterExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftExp = new LiteralExpression(1.1);
        rightExp = new LiteralExpression(1.1);

        expression = new GreaterEqualExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftExp = new LiteralExpression(1.1);
        rightExp = new LiteralExpression(1.1);

        expression = new LessExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(1.1);
        rightExp = new LiteralExpression(1.1);

        expression = new LessEqualExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftExp = new LiteralExpression(Double.NaN);
        rightExp = new LiteralExpression(1.1);

        expression = new GreaterExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(1.1);
        rightExp = new LiteralExpression(Double.NaN);

        expression = new GreaterExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(Double.NaN);
        rightExp = new LiteralExpression(Double.NaN);

        expression = new GreaterExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(Double.NaN);
        rightExp = new LiteralExpression(1.1);

        expression = new LessExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(1.1);
        rightExp = new LiteralExpression(Double.NaN);

        expression = new LessExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftExp = new LiteralExpression(Double.NaN);
        rightExp = new LiteralExpression(Double.NaN);

        expression = new LessExpression(leftExp, rightExp);

        context = new EvalContext();
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}