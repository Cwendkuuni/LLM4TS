package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {
    private CoreOperationEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationEqual(null, null);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("=", symbol);
    }

}