package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(3.5);
        Expression right = new LiteralExpression(4.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new Object[] { new Object[] {} }, new Object[] {});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(3.5);
        right = new LiteralExpression(3.5);
        expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(3.5);
        right = new LiteralExpression(2.5);
        expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(null);
        right = new LiteralExpression(4.0);
        expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(4.0);
        right = new LiteralExpression(null);
        expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new CoreOperation[0]);
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testReduce() {
        Expression expression = new LiteralExpression(new Object[] { new Object[] { 1 } });
        Object result = ((CoreOperationRelationalExpression) expression).reduce(expression.getValue());
        assertTrue(result instanceof Iterator);
        assertTrue(((Iterator) result).hasNext());

        expression = new LiteralExpression(new Object[] {});
        result = ((CoreOperationRelationalExpression) expression).reduce(expression.getValue());
        assertTrue(result instanceof EmptyIterator);

        expression = new LiteralExpression(42);
        result = ((CoreOperationRelationalExpression) expression).reduce(expression.getValue());
        assertTrue(result instanceof SelfContext);
    }

    @Test
    public void testContainsMatch() {
        Iterator left = new SingletonIterator(new Object[] { 1.0 });
        Iterator right = new SingletonIterator(new Object[] { 2.0 });
        boolean result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).containsMatch(left, right);
        assertFalse(result);

        left = new SingletonIterator(new Object[] { 1.0 });
        right = new SingletonIterator(new Object[] { 1.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).containsMatch(left, right);
        assertTrue(result);

        left = new SingletonIterator(new Object[] { 3.0 });
        right = new SingletonIterator(new Object[] { 2.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).containsMatch(left, right);
        assertFalse(result);

        left = new SingletonIterator(new Object[] { 3.0 });
        right = new SingletonIterator(new Object[] { 3.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).containsMatch(left, right);
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator left = new SingletonIterator(new Object[] { 1.0 });
        Iterator right = new SingletonIterator(new Object[] { 2.0, 3.0 });
        boolean result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).findMatch(left, right);
        assertFalse(result);

        left = new SingletonIterator(new Object[] { 1.0 });
        right = new SingletonIterator(new Object[] { 1.0, 1.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).findMatch(left, right);
        assertTrue(result);

        left = new SingletonIterator(new Object[] { 3.0 });
        right = new SingletonIterator(new Object[] { 2.0, 3.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).findMatch(left, right);
        assertFalse(result);

        left = new SingletonIterator(new Object[] { 3.0 });
        right = new SingletonIterator(new Object[] { 1.0, 2.0, 3.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).findMatch(left, right);
        assertTrue(result);

        left = new SingletonIterator(new Object[] { 3.0 });
        right = new SingletonIterator(new Object[] { 3.0, 3.0 });
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreOperation[] { left, right })).findMatch(left, right);
        assertTrue(result);
    }

    private static final int RELATIONAL_EXPR_PRECEDENCE = 5;
}