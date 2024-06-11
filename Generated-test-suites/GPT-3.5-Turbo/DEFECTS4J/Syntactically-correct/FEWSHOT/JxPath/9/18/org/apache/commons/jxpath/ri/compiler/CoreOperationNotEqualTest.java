package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1: Arguments are equal
        context.setCurrentNode("foo");
        operation.computeValue(context);
        assertFalse(operation.computeValue(context));
        
        // Test case 2: Arguments are not equal
        context.setCurrentNode("bar");
        operation.computeValue(context);
        assertTrue(operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", operation.getSymbol());
    }
}