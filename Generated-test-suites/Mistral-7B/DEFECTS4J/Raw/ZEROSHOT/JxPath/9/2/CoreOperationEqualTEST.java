package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.jsr223.SimpleVariable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    private EvalContext context;
    private Expression arg1;
    private Expression arg2;
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        context = Mockito.mock(EvalContext.class);
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_whenBothOperandsAreSame_thenReturnTrue() {
        Object value1 = "test";
        Object value2 = "test";
        Mockito.when(arg1.computeValue(context)).thenReturn(new SimpleVariable("value1", value1));
        Mockito.when(arg2.computeValue(context)).thenReturn(new SimpleVariable("value2", value2));
        Mockito.when(context.getVariableValues()).thenReturn(new Object[0]);

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothOperandsAreDifferent_thenReturnFalse() {
        Object value1 = "test1";
        Object value2 = "test2";
        Mockito.when(arg1.computeValue(context)).thenReturn(new SimpleVariable("value1", value1));
        Mockito.when(arg2.computeValue(context)).thenReturn(new SimpleVariable("value2", value2));
        Mockito.when(context.getVariableValues()).thenReturn(new Object[0]);

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstOperandIsNull_thenReturnFalse() {
        Object value2 = "test";
        Mockito.when(arg1.computeValue(context)).thenReturn(null);
        Mockito.when(arg2.computeValue(context)).thenReturn(new SimpleVariable("value2", value2));
        Mockito.when(context.getVariableValues()).thenReturn(new Object[0]);

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenSecondOperandIsNull_thenReturnFalse() {
        Object value1 = "test";
        Mockito.when(arg1.computeValue(context)).thenReturn(new SimpleVariable("value1", value1));
        Mockito.when(arg2.computeValue(context)).thenReturn(null);
        Mockito.when(context.getVariableValues()).thenReturn(new Object[0]);

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothOperandsAreOfDifferentTypes_thenReturnFalse() {
        Object value1 = 10;
        Object value2 = "test";
        Mockito.when(arg1.computeValue(context)).thenReturn(new SimpleVariable("value1", value1));
        Mockito.when(arg2.computeValue(context)).thenReturn(new SimpleVariable("value2", value2));
        Mockito.when(context.getVariableValues()).thenReturn(new Object[0]);

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_whenCalled_thenReturnEqualSign() {
        String symbol = coreOperationEqual.getSymbol();

        assertEquals("=", symbol);
    }
}