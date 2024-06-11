package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // test when l >= r
        op.args[0].computeValue(context);
        op.args[1].computeValue(context);
        assertEquals(Boolean.TRUE, op.computeValue(context));

        // test when l < r
        op.args[0].computeValue(context);
        op.args[1].computeValue(context);
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", op.getSymbol());
    }
}