package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionParser;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ParserException;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
    }

    @org.junit.Test
    public void testComputeValue() {
        Expression leftExp = new CoreOperationConstantExpression(new Integer(5));
        Expression rightExp = new CoreOperationConstantExpression(new Integer(10));
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression(new CoreOperationUnaryExpression(leftExp), rightExp);

        EvalContext context = new EvalContext(new SelfContext(), new ArrayList<Object>());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        leftExp = new CoreOperationConstantExpression(new Integer(10));
        rightExp = new CoreOperationConstantExpression(new Integer(5));
        expr = new CoreOperationGtExpression(leftExp, rightExp);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        leftExp = new CoreOperationConstantExpression(new Integer(5));
        rightExp = new CoreOperationConstantExpression(new Integer(5));
        expr = new CoreOperationGeqExpression(leftExp, rightExp);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test(expected = ParserException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        Expression[] args = new Expression[1];
        new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        assertFalse(expr.isSymmetric());
    }

    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        assertNull(expr.evaluateCompare(0));

        expr = new CoreOperationGeqExpression(new Expression[0], new Expression[0]);
        assertNull(expr.evaluateCompare(0));

        expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        assertSame(-1, expr.evaluateCompare(1));

        expr = new CoreOperationGeqExpression(new Expression[0], new Expression[0]);
        assertSame(0, expr.evaluateCompare(0));

        expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        assertSame(1, expr.evaluateCompare(-1));

        expr = new CoreOperationGeqExpression(new Expression[0], new Expression[0]);
        assertSame(1, expr.evaluateCompare(1));

        expr = new CoreOperationGtExpression(new Expression[0], new Expression[0]);
        assertSame(-1, expr.evaluateCompare(0));

        expr = new CoreOperationGeqExpression(new Expression[0], new Expression[0]);
        assertSame(0, expr.evaluateCompare(0));
    }

    @org.junit.Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);

        CoreOperationRelationalExpression expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left),
                new CoreOperationConstantExpression(right));
        boolean result = expr.compute(left, right);
        assertFalse(result);

        left = new Integer(10);
        right = new Integer(5);
        expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertTrue(result);

        left = new Integer(5);
        right = new Integer(5);
        expr = new CoreOperationGeqExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertTrue(result);

        left = new Double(3.5);
        right = new Double(3.5);
        expr = new CoreOperationGeqExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertTrue(result);

        left = new Double(3.5);
        right = new Double(4.0);
        expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertFalse(result);

        left = new Double(4.0);
        right = new Double(3.5);
        expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertTrue(result);

        left = new Double(NaN);
        right = new Double(NaN);
        expr = new CoreOperationGeqExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertFalse(result);

        left = new Double(0);
        right = new Double(NaN);
        expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertFalse(result);

        left = new Double(NaN);
        right = new Double(0);
        expr = new CoreOperationGtExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertFalse(result);

        left = new Double(NaN);
        right = new Double(NaN);
        expr = new CoreOperationGeqExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertFalse(result);

        left = new Double(0);
        right = new Double(0);
        expr = new CoreOperationGeqExpression(new CoreOperationConstantExpression(left), new CoreOperationConstantExpression(right));
        result = expr.compute(left, right);
        assertTrue(result);
    }

    @org.junit.Test
    public void testReduce() {
        Object o = new InitialContext();
        Object result = new CoreOperationRelationalExpression(new Expression[0]).reduce(o);
        assertSame(o, result);

        o = new ArrayList<>();
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(o);
        assertNotNull(result);
        assertSame(o.iterator(), result);
    }

    @org.junit.Test
    public void testContainsMatch() {
        Iterator it = new ArrayList<Object>().iterator();
        Object value = new Integer(5);

        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value));

        it.add(value);
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value));

        it.add(new Integer(10));
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value));

        it.remove();
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value));
    }

    @org.junit.Test
    public void testFindMatch() {
        Collection left = new ArrayList<Object>();
        Collection right = new ArrayList<Object>();

        left.add(new Integer(5));
        right.add(new Integer(10));

        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(10));
        right.add(new Integer(5));

        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(5));
        right.add(new Integer(5));
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(5));
        right.add(new Integer(10));
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(10));
        right.add(new Integer(5));
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(5));
        right.add(new Integer(5));
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.clear();
        right.add(new Integer(5));
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.add(new Integer(5));
        right.clear();
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));

        left.clear();
        right.clear();
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(left.iterator(), right.iterator()));
    }
}