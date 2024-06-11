package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        arg1 = new Constant(3);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        arg1 = new Constant(5);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
    }
}