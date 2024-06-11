package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(4.0)));

        EvalContext context = new EvalContext();
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[1] = new CoreConstantExpression(new Double(5.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        operation.args[1] = new CoreConstantExpression(new Double(4.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithMixedTypes() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Integer(4)));

        EvalContext context = new EvalContext();
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        operation.args[0] = new CoreConstantExpression(new Integer(4));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));

        operation.args[1] = new CoreConstantExpression(new Double(5.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        operation.args[1] = new CoreConstantExpression(new String("5"));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new String("5"));
        operation.args[1] = new CoreConstantExpression(new Double(4.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithNullValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Double(4.0)));

        EvalContext context = new EvalContext();
        assertNull(operation.computeValue(context));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[1] = new CoreConstantExpression(null);
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testComputeValueWithNaNAndFiniteValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(Double.NaN)),
                new CoreConstantExpression(new Double(4.0)));

        EvalContext context = new EvalContext();
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithNegativeValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(-4.0)));

        EvalContext context = new EvalContext();
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(-4.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[1] = new CoreConstantExpression(new Double(-5.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithInfiniteValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(Double.POSITIVE_INFINITY)),
                new CoreConstantExpression(new Double(4.0)));

        EvalContext context = new EvalContext();
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext();
        operation.args[0] = new CoreConstantExpression(new Double(4.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(4.0)));

        assertEquals(">=", operation.getSymbol());
    }
}