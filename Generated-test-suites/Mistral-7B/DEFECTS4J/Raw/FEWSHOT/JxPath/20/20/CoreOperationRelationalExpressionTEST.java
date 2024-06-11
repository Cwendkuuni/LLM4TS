package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testComputeValue() {
        Expression left = mock(Expression.class);
        Expression right = mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{ left, right });

        when(left.compute(mock(EvalContext.class))).thenReturn(Integer.valueOf(5));
        when(right.compute(mock(EvalContext.class))).thenReturn(Integer.valueOf(10));

        Object result = expression.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.TRUE, result);

        when(left.compute(mock(EvalContext.class))).thenReturn(Integer.valueOf(5));
        when(right.compute(mock(EvalContext.class))).thenReturn(Integer.valueOf(5));

        result = expression.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);

        when(left.compute(mock(EvalContext.class))).thenReturn(Double.valueOf(3.14));
        when(right.compute(mock(EvalContext.class))).thenReturn(Double.valueOf(3.1415926535897932384626433832795));

        result = expression.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);

        when(left.compute(mock(EvalContext.class))).thenReturn(Double.valueOf(3.14));
        when(right.compute(mock(EvalContext.class))).thenReturn(Double.valueOf(3.14));

        result = expression.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertEquals(6, expression.getPrecedence());
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(2, 3, 4).iterator();

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertTrue(expression.findMatch(leftIterator, rightIterator));

        leftIterator = Arrays.asList(1, 2, 3).iterator();
        rightIterator = Arrays.asList(1, 2, 3).iterator();

        assertFalse(expression.findMatch(leftIterator, rightIterator));

        leftIterator = Arrays.asList(1).iterator();
        rightIterator = Arrays.asList(1, 2, 3).iterator();

        assertFalse(expression.findMatch(leftIterator, rightIterator));

        leftIterator = Arrays.asList(1, 2, 3).iterator();
        rightIterator = Arrays.asList(1).iterator();

        assertFalse(expression.findMatch(leftIterator, rightIterator));

        leftIterator = Arrays.asList(1, 2, 3).iterator();
        rightIterator = Arrays.asList(1, 3, 2).iterator();

        assertTrue(expression.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = Arrays.asList(1, 2, 3).iterator();
        Object value = Integer.valueOf(2);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertFalse(expression.containsMatch(iterator, value));

        iterator = Arrays.asList(1, 2, 3, 2, 3).iterator();
        assertTrue(expression.containsMatch(iterator, value));

        iterator = Arrays.asList(1, 2, 3).iterator();
        value = Integer.valueOf(4);
        assertFalse(expression.containsMatch(iterator, value));
    }

    @Test
    public void testReduce() {
        Object value = Integer.valueOf(5);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{ new SelfContext() });
        Expression reduced = expression.reduce(value);
        assertEquals(value, reduced);

        value = new InitialContext();
        reduced = expression.reduce(value);
        assertNotNull(reduced);

        value = Arrays.asList(1, 2, 3).iterator();
        reduced = expression.reduce(value);
        assertNotNull(reduced);
    }

    @Test
    public void testCompute() {
        Object left = Double.valueOf(3.14);
        Object right = Double.valueOf(3.14);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{ new Expression[]{ left }, right });
        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.1415926535897932384626433832795);

        assertFalse(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.14);

        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.13);

        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.14D);

        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.14F);

        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.valueOf(3.14L);

        assertTrue(expression.compute(left, right));

        left = Double.valueOf(3.14);
        right = Double.NaN;

        assertFalse(expression.compute(left, right));

        left = Double.NaN;
        right = Double.valueOf(3.14);

        assertFalse(expression.compute(left, right));

        left = Double.NaN;
        right = Double.NaN;

        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testCoreOperationGreaterThan() {
        Expression[] args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertEquals("gt", expression.getOperator());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(args);
        assertEquals("gte", expression.getOperator());
    }

    @Test
    public void testCoreOperationLessThan() {
        Expression[] args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(args);
        assertEquals("lt", expression.getOperator());
    }

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(args);
        assertEquals("lte", expression.getOperator());
    }
}