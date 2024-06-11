package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationEqualTest {
    private CoreOperationEqual operationEqual;

    @Test
    public void testComputeValue() {
        operationEqual = new CoreOperationEqual(arg1, arg2);
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        Object result1 = operationEqual.computeValue(context1);
        assertEquals("Expected result: Boolean.TRUE", Boolean.TRUE, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        Object result2 = operationEqual.computeValue(context2);
        assertEquals("Expected result: Boolean.FALSE", Boolean.FALSE, result2);
        
        // Test case 3
        EvalContext context3 = new EvalContext();
        Object result3 = operationEqual.computeValue(context3);
        assertEquals("Expected result: Boolean.FALSE", Boolean.FALSE, result3);
    }
    
    @Test
    public void testGetSymbol() {
        operationEqual = new CoreOperationEqual(arg1, arg2);
        
        String symbol = operationEqual.getSymbol();
        assertEquals("Expected symbol: =", "=", symbol);
    }
}