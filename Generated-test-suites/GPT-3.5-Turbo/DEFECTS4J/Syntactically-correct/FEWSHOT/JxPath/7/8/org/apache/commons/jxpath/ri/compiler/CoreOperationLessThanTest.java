package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Test case 1
        context.getContextNodeList().add("3");
        Object result = lessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test case 2
        context.getContextNodeList().add("5");
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test case 3
        context.getContextNodeList().add("-3");
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        String symbol = lessThan.getSymbol();
        assertEquals("<", symbol);
    }
}