package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;
    
    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValueWithGreaterValue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 5);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueWithEqualValue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueWithLesserValue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArguments() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", null);
        context.setVariable("arg2", null);
        coreOperationGreaterThanOrEqual.computeValue(context);
    }
    
    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArguments() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", "5");
        context.setVariable("arg2", 10);
        coreOperationGreaterThanOrEqual.computeValue(context);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);
    }
}