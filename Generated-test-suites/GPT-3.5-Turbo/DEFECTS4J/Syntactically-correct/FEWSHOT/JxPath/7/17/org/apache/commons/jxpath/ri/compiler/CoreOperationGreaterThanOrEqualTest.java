package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setValues("arg1Value", 5);
        context.setValues("arg2Value", 3);
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setValues("arg1Value", 3);
        context.setValues("arg2Value", 5);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Add more test cases to cover different scenarios
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        assertEquals(">=", operation.getSymbol());
    }
}