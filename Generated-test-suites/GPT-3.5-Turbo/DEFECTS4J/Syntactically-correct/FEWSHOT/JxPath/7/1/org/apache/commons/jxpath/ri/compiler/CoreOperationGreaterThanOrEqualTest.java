package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        // Test when l is greater than r
        op.args[0] = new Expression(5);
        op.args[1] = new Expression(2);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));
        
        // Test when l is equal to r
        op.args[0] = new Expression(3);
        op.args[1] = new Expression(3);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));
        
        // Test when l is less than r
        op.args[0] = new Expression(7);
        op.args[1] = new Expression(9);
        assertEquals(Boolean.FALSE, op.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        assertEquals(">=", op.getSymbol());
    }
}