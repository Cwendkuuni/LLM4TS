// Test cases for CoreOperationLessThan
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationLessThanTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test when args[0] < args[1]
        Expression arg1 = new Literal("10"); // args[0]
        Expression arg2 = new Literal("20"); // args[1]
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when args[0] >= args[1]
        arg1 = new Literal("20"); // args[0]
        arg2 = new Literal("10"); // args[1]
        operation = new CoreOperationLessThan(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Literal("10"); // args[0]
        Expression arg2 = new Literal("20"); // args[1]
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", operation.getSymbol());
    }
}