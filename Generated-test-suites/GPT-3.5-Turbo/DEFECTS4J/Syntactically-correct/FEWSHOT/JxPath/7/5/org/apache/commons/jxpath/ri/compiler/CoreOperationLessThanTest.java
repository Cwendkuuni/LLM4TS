package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        context.addValue("arg1", 5);
        context.addValue("arg2", 10);
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.addValue("arg1", 10);
        context.addValue("arg2", 5);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        context.addValue("arg1", 5);
        context.addValue("arg2", 5);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", operation.getSymbol());
    }
}