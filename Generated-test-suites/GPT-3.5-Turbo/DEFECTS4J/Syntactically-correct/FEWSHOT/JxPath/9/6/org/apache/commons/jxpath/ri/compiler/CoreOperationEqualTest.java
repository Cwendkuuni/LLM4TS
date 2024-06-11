package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        context.set("arg1", "foo");
        context.set("arg2", "foo");
        assertTrue(opEqual.computeValue(context));
        
        // Test case 2
        context.set("arg1", "bar");
        context.set("arg2", "baz");
        assertFalse(opEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        
        String expected = "=";
        assertEquals(expected, opEqual.getSymbol());
    }
}