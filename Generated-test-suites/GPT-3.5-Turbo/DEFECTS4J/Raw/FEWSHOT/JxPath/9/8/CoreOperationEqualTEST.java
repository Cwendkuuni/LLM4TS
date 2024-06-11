package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        String symbol = operationEqual.getSymbol();
        assertEquals(expectedSymbol, symbol);
    }
}