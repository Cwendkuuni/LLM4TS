package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setContextNode(new Node());
        Object value1 = operationLessThanOrEqual.computeValue(context1);
        assertEquals(Boolean.FALSE, value1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setContextNode(new Node());
        Object value2 = operationLessThanOrEqual.computeValue(context2);
        assertEquals(Boolean.TRUE, value2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        String symbol = operationLessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }
}