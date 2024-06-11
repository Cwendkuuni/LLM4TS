package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = mock(EvalContext.class);
        
        when(operation.equal(context, arg1, arg2)).thenReturn(true);
        assertFalse((boolean)operation.computeValue(context));

        when(operation.equal(context, arg1, arg2)).thenReturn(false);
        assertTrue((boolean)operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}