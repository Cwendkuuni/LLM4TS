
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        assertEquals(arg1, operation.args[0]);
        assertEquals(arg2, operation.args[1]);
    }

    @Test
    public void testComputeValueWithLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        EvalContext context = mock(EvalContext.class);

        when(operation.args[0].computeValue(context)).thenReturn(1.0);
        when(operation.args[1].computeValue(context)).thenReturn(2.0);

        assertEquals(Boolean.TRUE, operation.computeValue(context));

        verify(operation.args[0], times(1)).computeValue(context);
        verify(operation.args[1], times(1)).computeValue(context);
    }

    @Test
    public void testComputeValueWithNotLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        EvalContext context = mock(EvalContext.class);

        when(operation.args[0].computeValue(context)).thenReturn(2.0);
        when(operation.args[1].computeValue(context)).thenReturn(1.0);

        assertEquals(Boolean.FALSE, operation.computeValue(context));

        verify(operation.args[0], times(1)).computeValue(context);
        verify(operation.args[1], times(1)).computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);

        assertEquals("<", operation.getSymbol());
    }
}
