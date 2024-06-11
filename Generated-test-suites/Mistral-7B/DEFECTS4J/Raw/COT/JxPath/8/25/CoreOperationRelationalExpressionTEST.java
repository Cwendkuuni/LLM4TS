package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * JUnit test file for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    private static final CoreOperation LEFT_EXPRESSION = new CoreOperation(){};
    private static final CoreOperation RIGHT_EXPRESSION = new CoreOperation(){};

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object leftValue = Boolean.TRUE;
        Object rightValue = Boolean.FALSE;
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = Boolean.FALSE;
        rightValue = Boolean.TRUE;
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = Boolean.TRUE;
        rightValue = Boolean.TRUE;
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        leftValue = new Integer(5);
        rightValue = new Integer(10);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Double(5.5);
        rightValue = new Double(10.5);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Double(5.5);
        rightValue = new Double(5.5);
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        leftValue = Collections.singletonList("one");
        rightValue = Collections.singletonList("two");
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new ArrayList<>();
        leftValue.add("one");
        rightValue = new ArrayList<>();
        rightValue.add("two");
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new ArrayList<>();
        leftValue.add("one");
        rightValue = new ArrayList<>();
        rightValue.add("one");
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        assertEquals(3, expression.getPrecedence());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @Test(expected = IllegalStateException.class)
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        expression.evaluateCompare(5);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        Object leftValue = Boolean.TRUE;
        Object rightValue = Boolean.FALSE;
        Object result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = Boolean.FALSE;
        rightValue = Boolean.TRUE;
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Integer(5);
        rightValue = new Integer(10);
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Double(5.5);
        rightValue = new Double(10.5);
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Double(5.5);
        rightValue = new Double(5.5);
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.TRUE, result);

        leftValue = new String[]{"one"};
        rightValue = new String[]{"two"};
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        rightValue = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);

        leftValue = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        rightValue = new HashSet<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.TRUE, result);

        leftValue = new HashSet<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        rightValue = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        result = expression.compute(leftValue, rightValue, context);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}.
     */
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        Object input = new InitialContext();
        Object result = expression.reduce(input);
        assertEquals(input, result);

        input = new SelfContext();
        result = expression.reduce(input);
        assertEquals(input, result);

        input = new ArrayList<>();
        result = expression.reduce(input);
        assertSame(input.iterator(), result);

        input = new HashSet<Object>();
        result = expression.reduce(input);
        assertSame(input.iterator(), result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}.
     */
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        Iterator<Object> leftIterator = new ArrayList<>().iterator();
        Object rightValue = new Integer(5);
        boolean result = expression.containsMatch(leftIterator, rightValue);
        assertFalse(result);

        leftIterator = new ArrayList<>().iterator();
        rightValue = new Integer(5);
        result = expression.containsMatch(leftIterator, rightValue);
        assertTrue(result);

        leftIterator = new ArrayList<>().iterator();
        rightValue = new Integer(6);
        result = expression.containsMatch(leftIterator, rightValue);
        assertFalse(result);

        leftIterator = new HashSet<Object>().iterator();
        rightValue = new Integer(5);
        result = expression.containsMatch(leftIterator, rightValue);
        assertFalse(result);

        leftIterator = new HashSet<Object>().iterator();
        rightValue = new Integer(5);
        result = expression.containsMatch(leftIterator, rightValue);
        assertTrue(result);

        leftIterator = new ArrayList<>().iterator();
        rightValue = new ArrayList<>().iterator();
        result = expression.containsMatch(leftIterator, rightValue);
        assertFalse(result);

        leftIterator = new ArrayList<>().iterator();
        rightValue = new ArrayList<>().iterator();
        result = expression.containsMatch(leftIterator, rightValue);
        assertTrue(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}.
     */
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(LEFT_EXPRESSION, RIGHT_EXPRESSION);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        Iterator<Object> leftIterator1 = new ArrayList<>().iterator();
        Iterator<Object> rightIterator1 = new ArrayList<>().iterator();
        assertTrue(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.next();
        rightIterator1.next();
        leftIterator1.remove();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.next();
        rightIterator1.next();
        leftIterator1.next();
        rightIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new HashSet<Object>().iterator();
        leftIterator1.next();
        rightIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new HashSet<Object>().iterator();
        leftIterator1.next();
        rightIterator1.next();
        leftIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new HashSet<Object>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.next();
        rightIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new HashSet<Object>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.next();
        leftIterator1.next();
        rightIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new HashSet<Object>().iterator();
        rightIterator1 = new HashSet<Object>().iterator();
        leftIterator1.next();
        leftIterator1.next();
        rightIterator1.next();
        leftIterator1.next();
        rightIterator1.next();
        assertTrue(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.add(new Integer(1));
        rightIterator1.add(new Integer(1));
        assertTrue(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.add(new Integer(1));
        rightIterator1.add(new Integer(2));
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.add(new Integer(1));
        rightIterator1.add(new Integer(1));
        leftIterator1.remove();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));

        leftIterator1 = new ArrayList<>().iterator();
        rightIterator1 = new ArrayList<>().iterator();
        leftIterator1.add(new Integer(1));
        rightIterator1.add(new Integer(1));
        leftIterator1.next();
        rightIterator1.next();
        assertFalse(expression.findMatch(leftIterator1, rightIterator1));
    }

    private static class MyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public MyCoreOperationRelationalExpression(CoreOperation leftExpression, CoreOperation rightExpression) {
            super(new CoreOperation[]{leftExpression, rightExpression});
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            throw new UnsupportedOperationException();
        }
    }
}