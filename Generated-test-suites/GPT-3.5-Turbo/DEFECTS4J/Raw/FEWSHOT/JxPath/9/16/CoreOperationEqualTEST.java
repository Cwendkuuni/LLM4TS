package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual coreOperationEqual;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    
    @Before
    public void setUp() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context = Mockito.mock(EvalContext.class);
    }
    
    @Test
    public void testComputeValue_returnsTrueWhenEqual() {
        Mockito.when(coreOperationEqual.equal(context, arg1, arg2)).thenReturn(true);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }
    
    @Test
    public void testComputeValue_returnsFalseWhenNotEqual() {
        Mockito.when(coreOperationEqual.equal(context, arg1, arg2)).thenReturn(false);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }
    
    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}