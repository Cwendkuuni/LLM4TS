package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = operation.getArgs()[0];
        Expression arg2 = operation.getArgs()[1];
        context.setValue(arg1, 5);
        context.setValue(arg2, 10);

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = operation.getArgs()[0];
        Expression arg2 = operation.getArgs()[1];
        context.setValue(arg1, 10);
        context.setValue(arg2, 5);

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Equal() {
        EvalContext context = new EvalContext();
        Expression arg1 = operation.getArgs()[0];
        Expression arg2 = operation.getArgs()[1];
        context.setValue(arg1, 5);
        context.setValue(arg2, 5);

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertNotNull(symbol);
        assertEquals(">", symbol);
    }
}