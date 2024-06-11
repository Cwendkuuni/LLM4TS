package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("10");
        operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotLessThan() {
        Expression arg1 = new Constant("20");
        Expression arg2 = new Constant("10");
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertEquals("<", symbol);
    }
}