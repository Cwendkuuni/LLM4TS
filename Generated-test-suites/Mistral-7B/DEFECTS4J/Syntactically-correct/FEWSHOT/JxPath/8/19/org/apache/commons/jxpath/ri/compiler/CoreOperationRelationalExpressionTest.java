package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression leftExp = new LiteralExpression(new Integer(5), new Object[0]);
        Expression rightExp = new LiteralExpression(new Integer(10), new Object[0]);
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[] {leftExp, rightExp});

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return new ArrayList(InitialContext.EMPTY_ITERATOR).iterator();
            }

            @Override
            public Object getValue(Object node, String name) {
                return null;
            }
        });

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftExp = new LiteralExpression(new Integer(10), new Object[0]);
        rightExp = new LiteralExpression(new Integer(5), new Object[0]);
        expression = new GreaterExpression(new CoreOperation[] {leftExp, rightExp});

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        leftExp = new LiteralExpression(new Double(3.14), new Object[0]);
        rightExp = new LiteralExpression(new Double(3.14), new Object[0]);
        expression = new GreaterExpression(new CoreOperation[] {leftExp, rightExp});

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftExp = new LiteralExpression(new Double(3.14), new Object[0]);
        rightExp = new LiteralExpression(new Double(3.15), new Object[0]);
        expression = new GreaterExpression(new CoreOperation[] {leftExp, rightExp});

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        boolean result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(1);
        assertTrue(result);

        result = expression.evaluateCompare(-1);
        assertFalse(result);

        expression = new GreaterOrEqualExpression(new CoreOperation[0]);
        result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(1);
        assertTrue(result);

        result = expression.evaluateCompare(-1);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[] {new LiteralExpression(left, new Object[0]), new LiteralExpression(right, new Object[0])});

        boolean result = expression.compute(left, right);
        assertTrue(result);

        left = new Double(3.14);
        right = new Double(3.14);
        expression = new GreaterExpression(new CoreOperation[] {new LiteralExpression(left, new Object[0]), new LiteralExpression(right, new Object[0])});

        result = expression.compute(left, right);
        assertTrue(result);

        left = new Double(3.14);
        right = new Double(3.15);
        expression = new GreaterExpression(new CoreOperation[] {new LiteralExpression(left, new Object[0]), new LiteralExpression(right, new Object[0])});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new LiteralExpression(new Integer(5), new Object[0]);
        right = new LiteralExpression(new Integer[0], new Object[0]);
        expression = new GreaterExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new LiteralExpression(new Integer(5), new Object[0]);
        right = new LiteralExpression(new Integer(5), new Object[0]);
        expression = new GreaterOrEqualExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertTrue(result);

        left = new LiteralExpression(new Integer(5), new Object[0]);
        right = new LiteralExpression(new Integer(4), new Object[0]);
        expression = new GreaterOrEqualExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertTrue(result);

        left = new LiteralExpression(new Integer(5), new Object[0]);
        right = new LiteralExpression(new Integer(6), new Object[0]);
        expression = new GreaterOrEqualExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new InitialContext();
        right = new SelfContext();
        expression = new GreaterExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new InitialContext();
        right = new InitialContext();
        expression = new GreaterExpression(new CoreOperation[] {left, right});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new Iterator[] {new InitialContext()};
        right = new Iterator[] {new SelfContext()};
        expression = new GreaterExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new Iterator[] {new InitialContext()};
        right = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], right[0])};
        expression = new GreaterExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], left[0])};
        right = new Iterator[] {new InitialContext()};
        expression = new GreaterExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], left[0])};
        right = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], right[0])};
        expression = new GreaterExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertFalse(result);

        left = new Iterator[] {new InitialContext()};
        right = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], right[0])};
        expression = new GreaterOrEqualExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertTrue(result);

        left = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], left[0])};
        right = new Iterator[] {new InitialContext()};
        expression = new GreaterOrEqualExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertTrue(result);

        left = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], left[0])};
        right = new Iterator[] {new IteratorExpression(new Object[0], new Object[0], right[0])};
        expression = new GreaterOrEqualExpression(new CoreOperation[] {new IteratorExpression(new Object[0], new Object[0], left[0]), new IteratorExpression(new Object[0], new Object[0], right[0])});

        result = expression.compute(left, right);
        assertTrue(result);
    }

    private void assertEquals(int expected, int actual) {
        assertEquals(Integer.valueOf(expected), Integer.valueOf(actual));
    }

    private void assertEquals(double expected, double actual) {
        assertEquals(Double.valueOf(expected), Double.valueOf(actual));
    }

    private void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual);
    }

}