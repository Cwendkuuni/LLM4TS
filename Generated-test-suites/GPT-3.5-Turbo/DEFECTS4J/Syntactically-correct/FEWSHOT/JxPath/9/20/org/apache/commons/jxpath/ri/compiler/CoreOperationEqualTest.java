package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_true() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = null;
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_false() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = null;
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}