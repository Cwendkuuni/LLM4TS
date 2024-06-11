//Test cases for the CoreOperationLessThan class
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test when arg1 is less than arg2
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(3), new Constant(5));
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        // Test when arg1 is greater than arg2
        lessThan = new CoreOperationLessThan(new Constant(7), new Constant(2));
        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        
        // Test when arg1 equals arg2
        lessThan = new CoreOperationLessThan(new Constant(10), new Constant(10));
        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(null, null);
        assertEquals("<", lessThan.getSymbol());
    }
}