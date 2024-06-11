package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
    }

    @Test
    public void testComputeValuePositive() {
        double l = 5.0;
        double r = 10.0;
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        when(arg1.computeValue(context)).thenReturn(l);
        when(arg2.computeValue(context)).thenReturn(r);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueNegative() {
        double l = 10.0;
        double r = 5.0;
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        when(arg1.computeValue(context)).thenReturn(l);
        when(arg2.computeValue(context)).thenReturn(r);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue() {
        double l = 3.5;
        double r = 5.0;
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        when(arg1.computeValue(context)).thenReturn(l);
        when(arg2.computeValue(context)).thenReturn(r);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Testing with null context and arguments
        EvalContext nullContext = null;
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertThrows(IllegalArgumentException.class, () -> coreOperationLessThan.computeValue(nullContext));

        // Testing with null arguments
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression nullArg1 = null;
        Expression nullArg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(nullArg1, nullArg2);
        assertThrows(IllegalArgumentException.class, () -> coreOperationLessThan.computeValue(context));

        // Testing with non-numeric arguments
        Expression arg1NonNumeric = Mockito.mock(Expression.class);
        Expression arg2Numeric = Mockito.mock(Expression.class);
        when(arg1NonNumeric.computeValue(context)).thenReturn(new String("abc"));
        when(arg2Numeric.computeValue(context)).thenReturn(5.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1NonNumeric, arg2Numeric);
        assertFalse(coreOperationLessThan.computeValue(context).isPresent());

        // Testing with NaN values
        EvalContext contextNaN = Mockito.mock(EvalContext.class);
        Expression arg1NaN = Mockito.mock(Expression.class);
        Expression arg2NaN = Mockito.mock(Expression.class);
        when(arg1NaN.computeValue(context)).thenReturn(Double.NaN);
        when(arg2NaN.computeValue(context)).thenReturn(Double.NaN);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1NaN, arg2NaN);
        assertFalse(coreOperationLessThan.computeValue(context).isPresent());
    }
}