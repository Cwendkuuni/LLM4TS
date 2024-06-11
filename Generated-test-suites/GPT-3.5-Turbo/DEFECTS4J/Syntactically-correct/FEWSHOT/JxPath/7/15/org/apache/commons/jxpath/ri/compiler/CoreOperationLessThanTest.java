package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;


public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        context = mock(EvalContext.class);

        operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan() {
        when(arg1.computeValue(context)).thenReturn(5);
        when(arg2.computeValue(context)).thenReturn(10);

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        when(arg1.computeValue(context)).thenReturn(10);
        when(arg2.computeValue(context)).thenReturn(5);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_Equal() {
        when(arg1.computeValue(context)).thenReturn(5);
        when(arg2.computeValue(context)).thenReturn(5);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertEquals("<", symbol);
    }
}