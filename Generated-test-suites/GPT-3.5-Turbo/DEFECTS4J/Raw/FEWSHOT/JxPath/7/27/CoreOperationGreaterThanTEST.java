
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1
        context.setDoubleValue(10.0);
        context.setDoubleValue(5.0);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test case 2
        context.setDoubleValue(5.0);
        context.setDoubleValue(10.0);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test case 3
        context.setDoubleValue(10.0);
        context.setDoubleValue(10.0);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", operation.getSymbol());
    }
    
}    
