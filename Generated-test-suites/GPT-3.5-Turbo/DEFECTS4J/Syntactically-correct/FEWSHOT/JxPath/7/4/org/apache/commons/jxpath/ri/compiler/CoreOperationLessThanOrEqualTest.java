package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_LessThanOrEqual() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);
        when(arg1.computeValue(context)).thenReturn(10);
        when(arg2.computeValue(context)).thenReturn(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        when(arg1.computeValue(context)).thenReturn(5);
        when(arg2.computeValue(context)).thenReturn(5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        when(arg1.computeValue(context)).thenReturn(5);
        when(arg2.computeValue(context)).thenReturn(10);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", operation.getSymbol());
    }
}