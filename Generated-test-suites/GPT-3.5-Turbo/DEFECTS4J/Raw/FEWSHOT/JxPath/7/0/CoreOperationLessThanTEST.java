package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationLessThanTest {

    private static CoreOperationLessThan op;

    @BeforeClass
    public static void setUp() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        op = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValueLess() {
        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNotLess() {
        EvalContext context = new EvalContext();
        op.args[0] = new Constant(10);
        Object result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = op.getSymbol();
        assertEquals("<", symbol);
    }
}