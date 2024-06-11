package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation1 = new CoreOperationNotEqual(arg1, arg2);
        Object result1 = operation1.computeValue(context);
        assertEquals(Boolean.FALSE, result1);
        
        // Test case 2
        arg1 = new Expression();
        arg2 = new Expression();
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(arg1, arg2);
        Object result2 = operation2.computeValue(context);
        assertEquals(Boolean.TRUE, result2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}