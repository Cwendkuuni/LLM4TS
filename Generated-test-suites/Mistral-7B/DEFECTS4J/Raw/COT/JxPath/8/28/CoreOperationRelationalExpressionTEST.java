package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iterator.EmptyIterator;
import org.apache.commons.jxpath.ri.iterator.SingletonIterator;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreConstantExpression(new Integer(5));
        Expression arg2 = new CoreConstantExpression(new Integer(10));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationBinaryExpression(arg1, arg2));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression(new Integer(5));
        arg2 = new CoreConstantExpression(new Integer(5));
        expression = new CoreOperationGreaterThan(new CoreOperationBinaryExpression(arg1, arg2));
        result = expression.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = new CoreConstantExpression(new Integer(5));
        Expression arg2 = new CoreConstantExpression(new Integer(10));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationBinaryExpression(arg1, arg2));
        expression.computeValue(null);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null);
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null);
        expression.evaluateCompare(-1);
    }

    @Test
    public void testComputeWithIterators() {
        Iterator iterator1 = new SingletonIterator(new Integer(1));
        Iterator iterator2 = new SingletonIterator(new Integer(2));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(iterator1), new CoreConstantExpression(iterator2)));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        iterator1 = new SingletonIterator(new Integer(2));
        iterator2 = new SingletonIterator(new Integer(1));
        expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(iterator1), new CoreConstantExpression(iterator2)));
        result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        iterator1 = new EmptyIterator();
        iterator2 = new SingletonIterator(new Integer(1));
        expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(iterator1), new CoreConstantExpression(iterator2)));
        result = expression.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result instanceof Boolean);
        assertNull(result);

        iterator1 = new SingletonIterator(new Integer(1));
        iterator2 = new EmptyIterator();
        expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(iterator1), new CoreConstantExpression(iterator2)));
        result = expression.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeWithDoubles() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(new Double(5.5)),
                        new CoreConstantExpression(new Double(10.5))));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThan(
                new CoreOperationBinaryExpression(new CoreConstantExpression(new Double(5.5)),
                        new CoreConstantExpression(new Double(5.5))));
        result = expression.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

}