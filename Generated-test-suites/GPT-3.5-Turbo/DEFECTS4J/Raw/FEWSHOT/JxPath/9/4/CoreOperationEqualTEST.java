package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationEqualTest {
    @Test
    public void testComputeValue() {
        // Create mock objects for testing
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        // Create instance of CoreOperationEqual
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        
        // Test when values are equal
        context.setVariable(arg1.toString(), 5);
        context.setVariable(arg2.toString(), 5);
        assertTrue((Boolean)operation.computeValue(context));
        
        // Test when values are not equal
        context.setVariable(arg2.toString(), 10);
        assertFalse((Boolean)operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", operation.getSymbol());
    }
}