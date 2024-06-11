package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1
        context.setNodeSet(new NodeSet());
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test case 2
        context.setNodeSet(new NodeSet());
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", expression.getSymbol());
    }
}