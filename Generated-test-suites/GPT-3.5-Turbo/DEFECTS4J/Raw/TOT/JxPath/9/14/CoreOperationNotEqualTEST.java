package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: two equal arguments
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Typical use case: two unequal arguments
        arg1 = new Constant(5);
        arg2 = new Constant(10);
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case: argument types are different
        arg1 = new Constant(5);
        arg2 = new Constant("5");
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Error scenario: null argument
        arg1 = new Constant(5);
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}