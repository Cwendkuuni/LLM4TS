package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_differentValues() {
        Expression arg1 = new ConstantExpression(2.5);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_sameValues() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullArgs() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext()); // This line should throw a NullPointerException
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_nonNumberTypes() {
        Expression arg1 = new ConstantExpression("string");
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext()); // This line should throw a ClassCastException
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, null);
        String result = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", result);
    }
}