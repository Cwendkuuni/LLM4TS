package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with valid double values
        Expression arg1 = new ConstantExpression(1.5);
        Expression arg2 = new ConstantExpression(2.5);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        // Test with invalid double values
        arg1 = new ConstantExpression(Double.NaN);
        arg2 = new ConstantExpression(Double.NaN);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(Double.NaN);
        arg2 = new ConstantExpression(1.5);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(Double.NaN);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(Double.NaN);
        arg2 = new ConstantExpression(Double.MIN_VALUE);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(Double.MIN_VALUE);
        arg2 = new ConstantExpression(Double.NaN);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(Double.MIN_VALUE);
        arg2 = new ConstantExpression(Double.MIN_VALUE);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(Double.MIN_VALUE);
        arg2 = new ConstantExpression(Double.MIN_VALUE);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with positive values
        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(2.5);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.2);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        // Test with zero
        arg1 = new ConstantExpression(0.0);
        arg2 = new ConstantExpression(1.5);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(0.0);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        arg1 = new ConstantExpression(0.0);
        arg2 = new ConstantExpression(0.0);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with negative values
        arg1 = new ConstantExpression(-1.5);
        arg2 = new ConstantExpression(-2.5);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        arg1 = new ConstantExpression(-1.5);
        arg2 = new ConstantExpression(-1.2);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        arg1 = new ConstantExpression(-1.5);
        arg2 = new ConstantExpression(-1.5);
        op = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(1.5), new ConstantExpression(2.5));
        assertEquals("<", op.getSymbol());
    }
}