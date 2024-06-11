package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setNodeSet(null);
        
        Object result = equal.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
        
        String symbol = equal.getSymbol();
        assertEquals("=", symbol);
    }

}