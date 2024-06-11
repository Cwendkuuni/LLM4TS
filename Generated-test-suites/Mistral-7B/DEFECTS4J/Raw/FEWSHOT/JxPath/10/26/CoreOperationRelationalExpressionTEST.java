package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new CoreConstantExpression(new Integer(5))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new Object(), new Object[]{}, null);
        Object left = new Integer(10);
        Object right = new Integer(5);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        left = new Integer(4);
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testComputeWithNullContext() {
        assertNull(expression.computeValue(null));
    }

    @Test
    public void testComputeWithInitialContexts() {
        EvalContext context1 = new EvalContext(new Object(), new Object[]{}, new InitialContext());
        EvalContext context2 = new EvalContext(new Object(), new Object[]{}, new InitialContext());

        assertNull(expression.computeValue(context1));
        assertNull(expression.computeValue(context2));
    }

    @Test
    public void testComputeWithIterators() {
        List<Integer> left = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> right = Arrays.asList(5, 6, 7, 8, 9);

        EvalContext context = new EvalContext(new Object(), left.iterator(), right.iterator());

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        left = Arrays.asList(1, 2, 3, 4, 5);
        right = Arrays.asList(4, 5, 6, 7, 8);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithSingleValueIterators() {
        EvalContext context = new EvalContext(new Object(), new SingletonIterator<Object>(new Integer(5)), new SingletonIterator<Object>(new Integer(5)));

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext(new Object(), new SingletonIterator<Object>(new Integer(5)), new SingletonIterator<Object>(new Integer(4)));

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithEmptyIterators() {
        EvalContext context = new EvalContext(new Object(), new EmptyIterator(), new EmptyIterator());

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithDoubles() {
        EvalContext context = new EvalContext(new Object(), new Double[]{3.1, 5.2}, null);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext(new Object(), new Double[]{3.1, 4.2}, null);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithNaNValues() {
        EvalContext context = new EvalContext(new Object(), new Double[]{Double.NaN, Double.NaN}, null);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testReduce() {
        Object left = new SelfContext();
        Object right = new SelfContext();

        assertEquals(left, expression.reduce(left));
        assertEquals(right, expression.reduce(right));

        left = new HashSet<Object>();
        left.add(new Integer(1));
        right = new HashSet<Object>();
        right.add(new Integer(2));

        assertEquals(left.iterator(), expression.reduce(left));
        assertEquals(right.iterator(), expression.reduce(right));
    }

    @Test
    public void testContainsMatch() {
        Iterator<Object> left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Object value = new Integer(5);

        assertTrue(expression.containsMatch(left, value));

        left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        value = new Integer(4);

        assertFalse(expression.containsMatch(left, value));
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Iterator<Object> right = Arrays.asList(5, 6, 7, 8, 9).iterator();

        EvalContext context = new EvalContext(new Object(), left, right);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        right = Arrays.asList(4, 5, 6, 7, 8).iterator();

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }
}