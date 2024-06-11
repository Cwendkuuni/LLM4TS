package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual lessThanOrEqual;

    @Before
    public void setUp() {
        lessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        EvalContext context = new EvalContext();
        Object result = lessThanOrEqual.computeValue(context);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = lessThanOrEqual.getSymbol();
        assertNotNull(symbol);
        assertEquals("<=", symbol);
    }
}