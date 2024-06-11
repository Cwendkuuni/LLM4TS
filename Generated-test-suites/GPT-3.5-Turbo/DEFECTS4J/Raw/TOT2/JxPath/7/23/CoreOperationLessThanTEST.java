package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        assertArrayEquals(new Expression[] {arg1, arg2}, lessThan.args);
    }

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        context.setJXPathContext(new DummyJXPathContext());
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        Object result = lessThan.computeValue(context);
        assertEquals(true, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotLessThan() {
        EvalContext context = new EvalContext();
        context.setJXPathContext(new DummyJXPathContext());
        Expression arg1 = new ConstantExpression(10);
        Expression arg2 = new ConstantExpression(5);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        Object result = lessThan.computeValue(context);
        assertEquals(true, result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new DummyExpression(), new DummyExpression());

        assertEquals("<", lessThan.getSymbol());
    }

    // Edge cases and exception handling scenarios could be added here

    // The complete JUnit test file will continue with additional test cases

}