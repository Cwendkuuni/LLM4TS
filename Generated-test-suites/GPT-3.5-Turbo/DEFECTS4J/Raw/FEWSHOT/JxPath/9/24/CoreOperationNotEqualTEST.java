package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreVariableReference("arg1");
        Expression arg2 = new CoreVariableReference("arg2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(null);
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value2");
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setVariable("arg2", "value1");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreVariableReference("");
        Expression arg2 = new CoreVariableReference("");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", operation.getSymbol());
    }
}