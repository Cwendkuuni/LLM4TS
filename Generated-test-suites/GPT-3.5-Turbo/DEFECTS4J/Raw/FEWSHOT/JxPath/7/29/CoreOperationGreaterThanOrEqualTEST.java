package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        context.setContextNodeValue(5);
        context.setCurrentNodeValue(3);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setContextNodeValue(3);
        context.setCurrentNodeValue(5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setContextNodeValue(5);
        context.setCurrentNodeValue(5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", operation.getSymbol());
    }
}