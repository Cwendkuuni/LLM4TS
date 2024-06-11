package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setContextNode("node");
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        
        String symbol = expression.getSymbol();
        assertEquals("<", symbol);
    }
}