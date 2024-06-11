package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(); // Create an instance of the EvalContext class
        
        // Create instances of Expression for testing
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        // Create an instance of CoreOperationLessThanOrEqual for testing
        CoreOperationLessThanOrEqual operationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Test case 1 - l <= r (l = 0, r = 0)
        arg1.setValue(0);
        arg2.setValue(0);
        assertEquals(Boolean.TRUE, operationLessThanOrEqual.computeValue(context));
        
        // Test case 2 - l <= r (l = 1, r = 0)
        arg1.setValue(1);
        arg2.setValue(0);
        assertEquals(Boolean.FALSE, operationLessThanOrEqual.computeValue(context));
        
        // Test case 3 - l <= r (l = 0, r = 1)
        arg1.setValue(0);
        arg2.setValue(1);
        assertEquals(Boolean.TRUE, operationLessThanOrEqual.computeValue(context));
        
        // Test case 4 - l <= r (l = 1, r = 1)
        arg1.setValue(1);
        arg2.setValue(1);
        assertEquals(Boolean.TRUE, operationLessThanOrEqual.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        // Create instances of Expression for testing
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        // Create an instance of CoreOperationLessThanOrEqual for testing
        CoreOperationLessThanOrEqual operationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", operationLessThanOrEqual.getSymbol());
    }
}