package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelpers;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = TestHelpers.createNumberLiteralExpression(1.5);
        Expression arg2 = TestHelpers.createNumberLiteralExpression(2.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertEquals(Boolean.FALSE, op.computeValue(new EvalContext()));

        arg2 = TestHelpers.createNumberLiteralExpression(1.5);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));

        arg1 = TestHelpers.createNumberLiteralExpression(Double.MAX_VALUE);
        arg2 = TestHelpers.createNumberLiteralExpression(Double.MAX_VALUE);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertEquals(Boolean.FALSE, op.computeValue(new EvalContext()));

        arg1 = TestHelpers.createNumberLiteralExpression(Double.MAX_VALUE);
        arg2 = TestHelpers.createNumberLiteralExpression(Double.MIN_VALUE);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertTrue(op.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(null, null);

        assertEquals("<=", op.getSymbol());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        Expression arg1 = TestHelpers.createNumberLiteralExpression(1.5);
        Expression arg2 = null;
        new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments2() {
        Expression arg1 = null;
        Expression arg2 = TestHelpers.createNumberLiteralExpression(2.0);
        new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testSymbolAndArgumentsToString() {
        Expression arg1 = TestHelpers.createNumberLiteralExpression(1.5);
        Expression arg2 = TestHelpers.createNumberLiteralExpression(2.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertEquals("(<= (1.5) (2.0))", op.toString());
    }
}