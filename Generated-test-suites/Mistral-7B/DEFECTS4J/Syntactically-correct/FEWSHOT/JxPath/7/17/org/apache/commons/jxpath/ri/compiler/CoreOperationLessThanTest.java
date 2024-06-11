package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestSupport;

/**
 * Test cases for {@link CoreOperationLessThan} class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new ConstantExpression(2.0));
        assertEquals("<", coreOperationLessThan.getSymbol());

        EvalContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        context = TestSupport.newEvaluationContext();
        context.setCurrentInfoItem(TestSupport.createInfoItem(3.0));
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithDifferentTypes() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new StringExpression("2"));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanWithNullValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new ConstantExpression(null));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(null),
                new ConstantExpression(1.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithSameValue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new ConstantExpression(1.0));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithZero() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(0.0),
                new ConstantExpression(0.0));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(0.0),
                new ConstantExpression(1.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNaN() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.NaN),
                new ConstantExpression(Double.NaN));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.NaN),
                new ConstantExpression(Double.POSITIVE_INFINITY));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.POSITIVE_INFINITY),
                new ConstantExpression(Double.NaN));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithInfinities() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.POSITIVE_INFINITY),
                new ConstantExpression(Double.POSITIVE_INFINITY));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.NEGATIVE_INFINITY),
                new ConstantExpression(Double.POSITIVE_INFINITY));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertTrue(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(Double.POSITIVE_INFINITY),
                new ConstantExpression(Double.NEGATIVE_INFINITY));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNegativeValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(-1.0),
                new ConstantExpression(-2.0));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(-2.0),
                new ConstantExpression(-1.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithMixedTypes() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new StringExpression("2.5"));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new StringExpression("1.5"),
                new ConstantExpression(2.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new ArrayExpression(new Expression[] {
                        new ConstantExpression(2.0),
                        new ConstantExpression(1.0)
                }));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ArrayExpression(new Expression[] {
                        new ConstantExpression(1.0),
                        new ConstantExpression(2.0)
                }),
                new ConstantExpression(1.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new ConstantExpression(1.0),
                new MapExpression("key", new ConstantExpression(2.0)));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new MapExpression("key", new ConstantExpression(1.0)),
                new ConstantExpression(2.0));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithComplexExpressions() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new AddExpression(new ConstantExpression(1.0),
                        new MultiplyExpression(new ConstantExpression(2.0),
                                new ConstantExpression(3.0))),
                new MultiplyExpression(new ConstantExpression(4.0),
                        new ConstantExpression(5.0)));

        EvaluationContext context = TestSupport.newEvaluationContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(((Boolean) result).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(
                new MultiplyExpression(new ConstantExpression(2.0),
                        new ConstantExpression(3.0)),
                new AddExpression(new ConstantExpression(1.0),
                        new ConstantExpression(4.0)));

        context = TestSupport.newEvaluationContext();
        result = coreOperationLessThan.computeValue(context);
        assertTrue(((Boolean) result).booleanValue());
    }
}