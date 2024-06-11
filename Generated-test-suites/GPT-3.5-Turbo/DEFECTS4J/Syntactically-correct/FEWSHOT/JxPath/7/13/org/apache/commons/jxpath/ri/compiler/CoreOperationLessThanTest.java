package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        
        assertEquals("<", expression.getSymbol());
        assertEquals("<", expression.getSymbol());
    }
}