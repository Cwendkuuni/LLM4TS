package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterators;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTestIterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;
    private NodeIterator left;
    private NodeIterator right;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(new Expression[] { new CoreConstantExpression(new SelfContext()), new CoreConstantExpression(new SelfContext()) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        context = new EvalContext(new InitialContext());
        left = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        right = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
    }

    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_false() {
        context = new EvalContext(new InitialContext());
        left = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        right = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        context.setCurrentNode(right.nextNode());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_invalidArgs() {
        expression = new CoreOperationRelationalExpression(new Expression[0]);
        expression.computeValue(context);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testFindMatch() {
        NodeIterator left1 = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        NodeIterator right1 = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        IterateIterators leftIter = new IterateIterators(left1);
        IterateIterators rightIter = new IterateIterators(right1);

        expression = new CoreOperationRelationalExpression(new Expression[] { leftIter, rightIter }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        NodeIterator common = new NodeTestIterator(new SingletonIterator<>(new SelfContext()), null);
        IterateIterators commonIter = new IterateIterators(common);

        assertTrue(expression.compute(leftIter.next(), commonIter.next()));
        assertTrue(expression.compute(commonIter.next(), leftIter.next()));
        assertFalse(expression.compute(leftIter.next(), rightIter.next()));
        assertFalse(expression.compute(rightIter.next(), leftIter.next()));

        Iterator<Object> left2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext();
            }
        };

        Iterator<Object> right2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext();
            }
        };

        expression = new CoreOperationRelationalExpression(new Expression[] { left2, right2 }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression.compute(left2.next(), right2.next()));
        assertTrue(expression.compute(right2.next(), left2.next()));
    }

    @Test
    public void testContainsMatch() {
        Iterator<Object> left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext();
            }
        };

        Object right = new SelfContext();

        expression = new CoreOperationRelationalExpression(new Expression[] { left, right }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression.compute(left.next(), right));
        assertFalse(expression.compute(left.next(), left.next()));

        Object left2 = new SelfContext();
        Iterator<Object> right2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return left2;
            }
        };

        expression = new CoreOperationRelationalExpression(new Expression[] { left, right2 }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression.compute(left.next(), right2.next()));
        assertFalse(expression.compute(left.next(), left.next()));
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext();
        Object reduced = expression.reduce(o);
        assertEquals(o, reduced);

        o = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext();
            }
        };

        reduced = expression.reduce(o);
        assertEquals(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext();
            }
        }, reduced);
    }
}