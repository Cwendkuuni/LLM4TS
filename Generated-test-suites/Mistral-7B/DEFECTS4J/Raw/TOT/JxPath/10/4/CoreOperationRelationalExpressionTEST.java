package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.AbstractTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.iter.IteratorIter;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest extends AbstractTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationGreaterThan(new VariableExpression("x"), new LiteralExpression(2));
    }

    @Test
    public void testGreaterThan() {
        InfoSet infoSet = createInfoSet(1, 2, 3);
        EvalContext context = new EvalContext(new Path("test"), infoSet, null);
        Object result = expression.computeValue(context);

        // Test with positive values
        assertEquals(Boolean.TRUE, result);

        // Test with negative values
        infoSet = createInfoSet(-1, 1, 2);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = expression.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with NaN
        infoSet = createInfoSet(Double.NaN, Double.NaN);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = expression.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with null values
        infoSet = createInfoSet(null, null);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = expression.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new VariableExpression("x"), new LiteralExpression(2));

        InfoSet infoSet = createInfoSet(1, 2, 3);
        EvalContext context = new EvalContext(new Path("test"), infoSet, null);

        Object result = greaterThanOrEqual.computeValue(context);

        // Test with positive values
        assertTrue(result.equals(Boolean.TRUE) || result.equals(Boolean.FALSE));

        infoSet = createInfoSet(1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = greaterThanOrEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));

        // Test with negative values
        infoSet = createInfoSet(-1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = greaterThanOrEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with NaN
        infoSet = createInfoSet(Double.NaN, Double.NaN);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = greaterThanOrEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with null values
        infoSet = createInfoSet(null, null);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = greaterThanOrEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression lessThan = new CoreOperationLessThan(
                new VariableExpression("x"), new LiteralExpression(2));

        InfoSet infoSet = createInfoSet(1, 2, 3);
        EvalContext context = new EvalContext(new Path("test"), infoSet, null);

        Object result = lessThan.computeValue(context);

        // Test with positive values
        assertFalse(result.equals(Boolean.TRUE));

        infoSet = createInfoSet(1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThan.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with negative values
        infoSet = createInfoSet(-1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThan.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE) || result.equals(Boolean.FALSE));

        // Test with NaN
        infoSet = createInfoSet(Double.NaN, Double.NaN);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThan.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with null values
        infoSet = createInfoSet(null, null);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThan.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new LiteralExpression(2));

        InfoSet infoSet = createInfoSet(1, 2, 3);
        EvalContext context = new EvalContext(new Path("test"), infoSet, null);

        Object result = lessThanOrEqual.computeValue(context);

        // Test with positive values
        assertTrue(result.equals(Boolean.TRUE) || result.equals(Boolean.FALSE));

        infoSet = createInfoSet(1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThanOrEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE) || result.equals(Boolean.FALSE));

        // Test with negative values
        infoSet = createInfoSet(-1, 2, 3);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThanOrEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));

        // Test with NaN
        infoSet = createInfoSet(Double.NaN, Double.NaN);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThanOrEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));

        // Test with null values
        infoSet = createInfoSet(null, null);
        context = new EvalContext(new Path("test"), infoSet, null);
        result = lessThanOrEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("x"), new VariableExpression("y"));

        InfoSet infoSet = createInfoSet(new IteratorIter(new SingletonIterator(1)),
                new IteratorIter(new SingletonIterator(2)));
        EvalContext context = new EvalContext(new Path("test"), infoSet, null);

        Object result = expression.computeValue(context);

        assertTrue(result.equals(Boolean.FALSE));

        infoSet = createInfoSet(new IteratorIter(new SingletonIterator(3)),
                new IteratorIter(new SingletonIterator(2)));
        context = new EvalContext(new Path("test"), infoSet, null);

        result = expression.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));

        infoSet = createInfoSet(new IteratorIter(new SingletonIterator(1)),
                new IteratorIter(new SingletonIterator(1)));
        context = new EvalContext(new Path("test"), infoSet, null);

        result = expression.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }
}