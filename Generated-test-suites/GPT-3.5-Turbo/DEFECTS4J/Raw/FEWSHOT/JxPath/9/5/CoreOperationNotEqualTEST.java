
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    private JXPathContext context;
    
    @Before
    public void setUp() {
        context = mock(JXPathContext.class);
    }
    
    @Test
    public void testComputeValue_equalArg1AndArg2_returnFalse() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        when(op.equal(context, arg1, arg2)).thenReturn(true);
        Object result = op.computeValue(context);
        assertEquals(false, result);
    }

    @Test
    public void testComputeValue_notEqualArg1AndArg2_returnTrue() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        when(op.equal(context, arg1, arg2)).thenReturn(false);
        Object result = op.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        String symbol = op.getSymbol();
        assertEquals("!=", symbol);
    }
}
