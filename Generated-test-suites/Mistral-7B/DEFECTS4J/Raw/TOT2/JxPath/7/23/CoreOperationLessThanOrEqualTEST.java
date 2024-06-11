package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(2.5), new ConstantExpression(3.0));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testLessThanOrEqualWithZero() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(0.0), new ConstantExpression(0.0));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testLessThanOrEqualWithNegativeNumbers() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-3.0), new ConstantExpression(-2.0));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testLessThanOrEqualWithNonNumbers() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression("2.5"), new ConstantExpression("3.0"));
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithNull() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                null, new ConstantExpression(3.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithNull2() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(3.0), null);
    }
}