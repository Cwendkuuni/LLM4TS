package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        arg1.computeValue(context);
        arg2.computeValue(context);
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        assertEquals("<=", operation.getSymbol());
    }
}