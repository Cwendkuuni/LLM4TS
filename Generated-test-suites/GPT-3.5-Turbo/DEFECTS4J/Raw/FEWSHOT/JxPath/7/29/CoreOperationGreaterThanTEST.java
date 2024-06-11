package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Test when the left value is greater than the right value
        EvalContext context1 = new EvalContext();
        context1.setValue("arg1", 5);
        context1.setValue("arg2", 3);
        assertEquals(Boolean.TRUE, operation.computeValue(context1));
        
        // Test when the right value is greater than the left value
        EvalContext context2 = new EvalContext();
        context2.setValue("arg1", 3);
        context2.setValue("arg2", 5);
        assertEquals(Boolean.FALSE, operation.computeValue(context2));
        
        // Test when the left value is equal to the right value
        EvalContext context3 = new EvalContext();
        context3.setValue("arg1", 3);
        context3.setValue("arg2", 3);
        assertEquals(Boolean.FALSE, operation.computeValue(context3));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        assertEquals(">", operation.getSymbol());
    }
}