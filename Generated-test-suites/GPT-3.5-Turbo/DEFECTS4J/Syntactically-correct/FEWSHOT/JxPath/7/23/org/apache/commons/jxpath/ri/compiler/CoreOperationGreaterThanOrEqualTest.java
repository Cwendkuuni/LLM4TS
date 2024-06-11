package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 2);

        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        context.setVariable("arg1", 2);
        context.setVariable("arg2", 3);
        
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        assertEquals(">=", expression.getSymbol());
    }
}