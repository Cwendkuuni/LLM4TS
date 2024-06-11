package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertTrue(result.booleanValue());

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(3.0), new ConstantExpression(2.0));
        result = operation.computeValue(context);
        assertFalse(result.booleanValue());

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-2.0));
        result = operation.computeValue(context);
        assertTrue(result.booleanValue());

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-2.0));
        result = operation.computeValue(context);
        assertTrue(result.booleanValue());

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(1.0));
        result = operation.computeValue(context);
        assertTrue(result.booleanValue());

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(null), new ConstantExpression(2.0));
        try {
            result = operation.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression("str"), new ConstantExpression(2.0));
        try {
            result = operation.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected, since String cannot be converted to Number
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }

    @Test
    public void testToString() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        String toString = operation.toString();
        assertEquals("(<= (1.0) (2.0))",toString);
    }
}