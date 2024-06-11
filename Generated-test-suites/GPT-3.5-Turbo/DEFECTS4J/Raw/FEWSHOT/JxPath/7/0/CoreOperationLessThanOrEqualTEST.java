package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 5);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", expression.getSymbol());
    }
}