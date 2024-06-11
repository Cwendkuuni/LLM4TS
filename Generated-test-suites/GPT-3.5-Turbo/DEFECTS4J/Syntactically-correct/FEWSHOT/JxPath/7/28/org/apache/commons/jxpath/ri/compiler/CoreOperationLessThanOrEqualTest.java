package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setValue(arg1, 5);
        context.setValue(arg2, 10);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setValue(arg2, 5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setValue(arg1, 10);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        context.setValue(arg1, null);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        context.setValue(arg2, null);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", operation.getSymbol());
    }
}