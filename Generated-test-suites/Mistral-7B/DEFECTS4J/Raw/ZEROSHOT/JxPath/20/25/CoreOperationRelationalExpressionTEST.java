package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 * @author John Doe
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}.
     */
    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        args[0] = new LiteralExpression(1);
        args[1] = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(left, right));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(2);
        right = new LiteralExpression(1);
        expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(left, right));
        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(2);
        right = new LiteralExpression(2);
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(1);
        right = new LiteralExpression(2);
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(new LiteralExpression(1), new LiteralExpression(2)));
        int precedence = expression.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(new LiteralExpression(1), new LiteralExpression(2)));
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(new LiteralExpression(1), new LiteralExpression(2)));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @Test
    public void testCompute() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(left, right));

        Object result = expression.compute(new LiteralExpression(1), new LiteralExpression(2));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(2);
        result = expression.compute(new LiteralExpression(2), new LiteralExpression(2));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(2);
        result = expression.compute(new LiteralExpression(1), new LiteralExpression(2));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(Collections.singletonList(new LiteralExpression(1)));
        right = new LiteralExpression(Collections.singletonList(new LiteralExpression(2)));
        expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(Collections.singletonList(new LiteralExpression(2)));
        right = new LiteralExpression(Collections.singletonList(new LiteralExpression(1)));
        expression = new CoreOperationGreaterExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(Collections.singletonList(new LiteralExpression(2)));
        right = new LiteralExpression(Collections.singletonList(new LiteralExpression(2)));
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(Collections.singletonList(new LiteralExpression(1)));
        right = new LiteralExpression(Collections.singletonList(new LiteralExpression(2)));
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(Collections.singletonList(new LiteralExpression(1)));
        right = new LiteralExpression(new ArrayList<>());
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new ArrayList<>());
        right = new LiteralExpression(Collections.singletonList(new LiteralExpression(1)));
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new ArrayList<>());
        right = new LiteralExpression(new ArrayList<>());
        expression = new CoreOperationGreaterOrEqualExpression(new CoreOperationRelationalExpressionArgs(left, right));
        result = expression.compute(new LiteralExpression(new ArrayList<>()), new LiteralExpression(new ArrayList<>()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}.
     */
    @Test
    public void testReduce() {
        Expression expression = new LiteralExpression(1);
        Object result = expression.reduce(expression);
        assertNotNull(result);
        assertEquals(expression, result);

        expression = new InitialContext();
        result = expression.reduce(expression);
        assertNotNull(result);
        assertNotSame(expression, result);

        expression = new ArrayList<>();
        result = expression.reduce(expression);
        assertNotNull(result);
        assertNotSame(expression, result);

        expression = new Iterator() {
            private boolean hasNext = true;
            private Object next;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                Object object = next;
                next = null;
                hasNext = false;
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        result = expression.reduce(expression);
        assertNotNull(result);
        assertNotSame(expression, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}.
     */
    @Test
    public void testContainsMatch() {
        Iterator iterator = new Iterator() {
            private boolean hasNext = true;
            private Object next;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                Object object = next;
                next = null;
                hasNext = false;
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        Object value = new LiteralExpression(1);
        boolean result = CoreOperationRelationalExpression.containsMatch(iterator, value);
        assertFalse(result);

        next = new LiteralExpression(1);
        hasNext = true;
        result = CoreOperationRelationalExpression.containsMatch(iterator, value);
        assertTrue(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}.
     */
    @Test
    public void testFindMatch() {
        Iterator leftIterator = new Iterator() {
            private boolean hasNext = true;
            private Object next;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                Object object = next;
                next = null;
                hasNext = false;
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        Iterator rightIterator = new Iterator() {
            private boolean hasNext = true;
            private Object next;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                Object object = next;
                next = null;
                hasNext = false;
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        boolean result = CoreOperationRelationalExpression.findMatch(leftIterator, rightIterator);
        assertFalse(result);

        next = new LiteralExpression(1);
        hasNext = true;
        next = new LiteralExpression(1);
        hasNext = true;
        result = CoreOperationRelationalExpression.findMatch(leftIterator, rightIterator);
        assertTrue(result);
    }
}