package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest {
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(null, null);
    }

    @Test
    public void testComputeValue_equalArgs_returnTrue() {
        EvalContext context = new EvalContext();
        context.setContextNodeSet(null);
        context.setPosition(1);
        context.setSize(1);

        Object result = coreOperationEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol_returnEqualSymbol() {
        String symbol = coreOperationEqual.getSymbol();

        assertEquals("=", symbol);
    }
}