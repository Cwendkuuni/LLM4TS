package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNode(arg1);
        context.setContextPosition(1);
        context.setContextSize(5);
        
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
        
        context.setContextNode(arg2);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
        
        arg1 = null;
        arg2 = null;
        context.setContextNode(null);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
    }
}