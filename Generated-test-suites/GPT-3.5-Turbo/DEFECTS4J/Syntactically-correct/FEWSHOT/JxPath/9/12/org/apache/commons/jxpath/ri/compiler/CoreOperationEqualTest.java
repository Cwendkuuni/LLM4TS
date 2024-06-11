package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(argument1, argument2);
        EvalContext evalContext = new EvalContext();
        
        // Test case for equal values
        evalContext.setReturnType(Boolean.TRUE);
        assertTrue(coreOperationEqual.computeValue(evalContext));
        
        // Test case for not equal values
        evalContext.setReturnType(Boolean.FALSE);
        assertFalse(coreOperationEqual.computeValue(evalContext));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(argument1, argument2);
        
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}