package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        // Test case 1: args[0] == args[1]
        arg1.setValue(5);
        arg2.setValue(5);
        assertFalse((Boolean) operation.computeValue(null));
        
        // Test case 2: args[0] != args[1]
        arg2.setValue(10);
        assertTrue((Boolean) operation.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        String expected = "!=";
        String actual = operation.getSymbol();
        
        assertEquals(expected, actual);
    }
}