package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        assertEquals("=", coreOperationEqual.getSymbol());
    }
}