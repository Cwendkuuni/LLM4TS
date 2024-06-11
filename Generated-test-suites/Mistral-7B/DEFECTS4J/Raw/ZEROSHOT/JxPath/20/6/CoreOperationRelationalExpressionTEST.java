package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 *
 * @author John Doe
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(left), new LiteralExpression(right));

        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(new Integer(5)), new LiteralExpression(new Integer(10)));
        int precedence = expression.getPrecedence();

        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(new Integer(5)), new LiteralExpression(new Integer(10)));

        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(new Integer(5)), new LiteralExpression(new Integer(10)));

        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(left), new LiteralExpression(right));

        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        Object leftResult = expression.args[0].computeValue(context);
        Object rightResult = expression.args[1].computeValue(context);

        assertTrue(expression.compute(leftResult, rightResult));
        assertTrue(expression.compute(left, rightResult));
        assertFalse(expression.compute(leftResult, right));
        assertFalse(expression.compute(left, right));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}.
     */
    @Test
    public void testReduce() {
        Object left = new Integer(5);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(left), new LiteralExpression(new Integer(10)));

        Object result = expression.reduce(left);

        assertSame(left, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}.
     */
    @Test
    public void testContainsMatch() {
        Object[] elements = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
        Iterator iter = new ArrayIterator(elements);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(iter, new LiteralExpression(new Integer(3)));

        assertTrue(expression.containsMatch(iter, new Integer(3)));
        assertTrue(expression.containsMatch(iter, new Integer(4)));
        assertFalse(expression.containsMatch(iter, new Integer(2)));
        assertFalse(expression.containsMatch(iter, new Integer(1)));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}.
     */
    @Test
    public void testFindMatch() {
        Object[] leftElements = new Object[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
        Iterator leftIter = new ArrayIterator(leftElements);
        Object[] rightElements = new Object[]{new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6)};
        Iterator rightIter = new ArrayIterator(rightElements);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(leftIter, rightIter);

        assertTrue(expression.findMatch(leftIter, rightIter));
    }
}