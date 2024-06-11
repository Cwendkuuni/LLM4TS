package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test cases for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanButNotEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new Node()); // simulate current node for side effect
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testNonNumericOperands() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreStringExpression("1"), new CoreStringExpression("2"));
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOperand() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(null), new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test
    public void testNegativeNumberLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(-2.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testPositiveNumberLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testZeroLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testInfinityLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.POSITIVE_INFINITY),
                new CoreConstantExpression(Double.POSITIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testMinusInfinityLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NEGATIVE_INFINITY),
                new CoreConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNaNLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.NaN, result);
    }

    @Test
    public void testNaNWithANumber() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNaNWithMinusInfinity() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNaNWithInfinity() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.POSITIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNegativeNumberGreaterThanZeroLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(0.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

}