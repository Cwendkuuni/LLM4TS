package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorIterable;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(1));
        assertFalse(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(1));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(2));
        assertFalse(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationLessThan("1", new CoreConstantExpression(1));
        assertFalse(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationLessThan("1", new CoreConstantExpression(2));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.FALSE));

        expression = new CoreOperationLessThanOrEqual("1", new CoreConstantExpression(1));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationLessThanOrEqual("1", new CoreConstantExpression(2));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.FALSE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(2));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLessThan("1", new CoreConstantExpression(2));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLessThanOrEqual("1", new CoreConstantExpression(2));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2));
        assertTrue(expression.evaluateCompare(-1) == false);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == true);

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(2));
        assertTrue(expression.evaluateCompare(-1) == false);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == true);
        assertTrue(expression.evaluateCompare(2) == true);

        expression = new CoreOperationLessThan("1", new CoreConstantExpression(2));
        assertTrue(expression.evaluateCompare(-1) == true);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == false);

        expression = new CoreOperationLessThanOrEqual("1", new CoreConstantExpression(2));
        assertTrue(expression.evaluateCompare(-1) == true);
        assertTrue(expression.evaluateCompare(0) == true);
        assertTrue(expression.evaluateCompare(1) == false);
        assertTrue(expression.evaluateCompare(2) == true);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2));
        Object left = new SingletonIterator(1);
        Object right = new SingletonIterator(2);
        assertTrue(expression.compute(left, right));

        left = new IteratorIterable(new Integer[]{1, 2, 3});
        right = new SingletonIterator(2);
        assertFalse(expression.compute(left, right));

        left = new SingletonIterator(2);
        right = new IteratorIterable(new Integer[]{1, 2, 3});
        assertFalse(expression.compute(left, right));

        left = new IteratorIterable(new Integer[]{1, 2, 3, 4});
        right = new IteratorIterable(new Integer[]{2, 3, 4});
        assertTrue(expression.compute(left, right));

        left = new IteratorIterable(new Integer[]{1, 2, 3, 4});
        right = new IteratorIterable(new Integer[]{1, 2, 3, 4});
        assertTrue(expression.compute(left, right));

        left = new IteratorIterable(new Object[]{1, 2, 3, 4});
        right = new IteratorIterable(new Object[]{2, 3, 4});
        assertFalse(expression.compute(left, right));

        left = new IteratorIterable(new Object[]{1, 2, 3, 4});
        right = new IteratorIterable(new Object[]{1, 2, 3, 4});
        assertTrue(expression.compute(left, right));
    }

    private int assertEquals(int expected, int actual) {
        if (expected != actual) {
            fail("Expected: " + expected + ", but was: " + actual);
        }
        return expected;
    }
}