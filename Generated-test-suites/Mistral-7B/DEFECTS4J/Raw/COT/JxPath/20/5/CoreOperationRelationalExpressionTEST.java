package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression
 *
 * @author your.name
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Before
    public void setup() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        context = new EvalContext();
    }

    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompareLessThan() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompareLessThanOrEquals() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(1) });
        assertTrue(expression.evaluateCompare(0));
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(3) });
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareGreaterThan() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(1) });
        assertFalse(expression.evaluateCompare(-1));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        assertFalse(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(1) });
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareGreaterThanOrEquals() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(1) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        assertFalse(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(1) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(2) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(3) });
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testComputeWithInitialContext() {
        Expression left = new LiteralExpression(new InitialContext());
        Expression right = new LiteralExpression(new InitialContext());
        expression = new TestCoreOperationRelationalExpression(new Expression[] { left, right });
        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeWithSelfContext() {
        Expression left = new LiteralExpression(new SelfContext());
        Expression right = new LiteralExpression(new SelfContext());
        expression = new TestCoreOperationRelationalExpression(new Expression[] { left, right });
        Object result = expression.computeValue(context);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithIterators() {
        List<Object> leftIterable = new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }};
        Iterator<Object> leftIterator = leftIterable.iterator();

        List<Object> rightIterable = new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }};
        Iterator<Object> rightIterator = rightIterable.iterator();

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) });
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        leftIterable = new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }};
        leftIterator = leftIterable.iterator();

        rightIterable = new ArrayList<Object>() {{
            add(4);
            add(5);
            add(3);
        }};
        rightIterator = rightIterable.iterator();

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeWithDoubleValues() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1.1), new LiteralExpression(1.2) });
        assertFalse(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1.1), new LiteralExpression(1.1) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1.1), new LiteralExpression(1.2) });
        assertFalse(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1.2), new LiteralExpression(1.1) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(1.1), new LiteralExpression(1.1) });
        assertTrue(expression.evaluateCompare(0));

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN) });
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testFindMatch() {
        List<Object> left = new ArrayList<Object>() {{
            add("one");
            add("two");
            add("three");
        }};
        Iterator<Object> leftIterator = left.iterator();

        List<Object> right = new ArrayList<Object>() {{
            add("one");
            add("two");
            add("two");
        }};
        Iterator<Object> rightIterator = right.iterator();

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) });
        assertTrue(expression.findMatch(leftIterator, rightIterator));

        left = new ArrayList<Object>() {{
            add("one");
            add("two");
            add("three");
        }};
        leftIterator = left.iterator();

        right = new ArrayList<Object>() {{
            add("two");
            add("three");
            add("four");
        }};
        rightIterator = right.iterator();

        expression = new TestCoreOperationRelationalExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) });
        assertFalse(expression.findMatch(leftIterator, rightIterator));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindMatchWithNullIterators() {
        expression = new TestCoreOperationRelationalExpression(new Expression[] { new NullIteratorExpression(), new NullIteratorExpression() });
        expression.findMatch(null, null);
    }

    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return Arrays.asList(1, 0, -1).contains(compare);
        }
    }

}

class NullIteratorExpression extends IteratorExpression {

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}