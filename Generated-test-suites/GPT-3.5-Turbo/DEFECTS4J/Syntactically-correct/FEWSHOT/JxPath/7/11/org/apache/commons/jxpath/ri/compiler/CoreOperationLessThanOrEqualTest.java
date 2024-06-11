package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        // Test case 1
        operation.args[0] = "3";
        operation.args[1] = "5";
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
        
        // Test case 2
        operation.args[0] = "5";
        operation.args[1] = "3";
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
        
        // Test case 3
        operation.args[0] = "5";
        operation.args[1] = "5";
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }
}