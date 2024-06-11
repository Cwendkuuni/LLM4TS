package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        // Create mock objects
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        // Set up mock behavior
        when(arg1.computeValue(context)).thenReturn(1);
        when(arg2.computeValue(context)).thenReturn(2);

        // Create instance of CoreOperationGreaterThan
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Call method under test
        Object result = coreOperationGreaterThan.computeValue(context);

        // Verify result
        Assert.assertEquals(Boolean.FALSE, result);
        
        // Verify interactions
        verify(arg1).computeValue(context);
        verify(arg2).computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        // Create mock objects
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        // Create instance of CoreOperationGreaterThan
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Call method under test
        String result = coreOperationGreaterThan.getSymbol();

        // Verify result
        Assert.assertEquals(">", result);
    }
}