package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", operation.getSymbol());
    }
}