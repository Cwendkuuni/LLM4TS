package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(1.0));
        context = new EvalContext(new InfoSet());
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(0.5));
        context = new EvalContext(new InfoSet());
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqualMismatchedTypes() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1), new ConstantExpression("2"));
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualNullArg() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                null, new ConstantExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualNullArg2() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), null);
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThanOrEqualNan() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.NaN, result);

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Double.NaN), new ConstantExpression(1.0));
        context = new EvalContext(new InfoSet());
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(Double.NaN));
        context = new EvalContext(new InfoSet());
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}