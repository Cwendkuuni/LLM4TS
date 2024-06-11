
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan lessThan;
    
    @Before
    public void setup() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan() {
        EvalContext context = new EvalContext();
        double arg1 = 5.0;
        double arg2 = 10.0;
        boolean expected = true;
        
        Object result = lessThan.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertEquals(expected, (boolean) result);
    }
    
    @Test
    public void testComputeValue_NotLessThan() {
        EvalContext context = new EvalContext();
        double arg1 = 10.0;
        double arg2 = 5.0;
        boolean expected = false;
        
        Object result = lessThan.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertEquals(expected, (boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        String expected = "<";
        
        String result = lessThan.getSymbol();
        
        assertEquals(expected, result);
    }
}
