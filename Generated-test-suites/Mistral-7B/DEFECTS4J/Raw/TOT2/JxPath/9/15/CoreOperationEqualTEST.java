package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualDifferentDataTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression("one");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualNull() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArg() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    // Add tests for edge cases and exception handling here
}