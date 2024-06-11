package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        context.addVariable("a", 5);
        context.addVariable("b", 10);
        
        arg1.setContext(context);
        arg2.setContext(context);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        context.addVariable("a", 15);
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        assertEquals(">", operation.getSymbol());
    }
}