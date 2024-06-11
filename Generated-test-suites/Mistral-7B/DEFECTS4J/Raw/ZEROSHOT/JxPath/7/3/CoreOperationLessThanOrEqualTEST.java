package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        // Test with numeric values
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        double value1 = 5.0;
        double value2 = 10.0;
        when(arg1.computeValue(any(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(any(EvalContext.class))).thenReturn(value2);

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        boolean result = coreOperationLessThanOrEqual.computeValue(mock(EvalContext.class)).booleanValue();
        assertEquals("Expected result to be true", true, result);

        // Test with values where first value is less than second value
        value1 = 3.0;
        value2 = 5.0;
        when(arg1.computeValue(any(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(any(EvalContext.class))).thenReturn(value2);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(mock(EvalContext.class)).booleanValue();
        assertEquals("Expected result to be true", true, result);

        // Test with values where first value is equal to second value
        value1 = 5.0;
        value2 = 5.0;
        when(arg1.computeValue(any(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(any(EvalContext.class))).thenReturn(value2);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(mock(EvalContext.class)).booleanValue();
        assertEquals("Expected result to be true", true, result);

        // Test with values where first value is greater than second value
        value1 = 10.0;
        value2 = 5.0;
        when(arg1.computeValue(any(EvalContext.class))).thenReturn(value1);
        when(arg2.computeValue(any(EvalContext.class))).thenReturn(value2);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(mock(EvalContext.class)).booleanValue();
        assertEquals("Expected result to be false", false, result);
    }
}