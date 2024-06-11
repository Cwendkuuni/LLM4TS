package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual coreOperation;

    @Before
    public void setUp() {
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        coreOperation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_equalValues_returnsFalse() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getValue(Mockito.any())).thenReturn("value");

        Object result = coreOperation.computeValue(context);

        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_notEqualValues_returnsTrue() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getValue(Mockito.any())).thenReturn("value1", "value2");

        Object result = coreOperation.computeValue(context);

        assertTrue((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperation.getSymbol();

        assertEquals("!=", symbol);
    }
}