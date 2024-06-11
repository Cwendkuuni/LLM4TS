package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThan() {
        // Test with numeric values
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        double value1 = 5.0;
        double value2 = 10.0;
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(value2);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);

        value1 = 3.5;
        value2 = 4.0;
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(value2);

        result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);

        value1 = 3.5;
        value2 = 3.0;
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(value2);

        result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.TRUE, result);

        // Test with null values
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(null);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(null);

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.UNDEFINED, result);

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(null);

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(null);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(value2);

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(mock(EvalContext.class));
        assertEquals(Boolean.FALSE, result);
    }
}