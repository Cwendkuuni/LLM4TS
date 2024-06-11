package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value2");
        
        assertFalse(coreOperationEqual.computeValue(context));
        
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value1");
        
        assertTrue(coreOperationEqual.computeValue(context));
        
        context.setVariable("arg1", "value2");
        context.setVariable("arg2", "value1");
        
        assertFalse(coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        assertEquals("=", coreOperationEqual.getSymbol());
    }

}