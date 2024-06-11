package org.apache.commons.jxpath.ri.compiler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual greaterThanOrEqual;
    
    @Before
    public void setUp() {
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        greaterThanOrEqual.getArgs()[0] = new Expression();
        greaterThanOrEqual.getArgs()[0].computeValue(context);
        greaterThanOrEqual.getArgs()[1] = new Expression();
        greaterThanOrEqual.getArgs()[1].computeValue(context);
        
        Object value = greaterThanOrEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals(">=", greaterThanOrEqual.getSymbol());
    }
}