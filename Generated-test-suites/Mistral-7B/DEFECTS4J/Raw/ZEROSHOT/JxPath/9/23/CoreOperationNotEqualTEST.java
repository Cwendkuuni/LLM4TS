package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;

import org.junit.Test;

/**
 * Test for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)} .
     */
    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqual);
        assertEquals(CoreOperationNotEqual.class, coreOperationNotEqual.getClass());
        assertEquals(arg1, coreOperationNotEqual.getArg1());
        assertEquals(arg2, coreOperationNotEqual.getArg2());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} .
     */
    @Test
    public void testComputeValue() {
        Object value1 = new Object();
        Object value2 = new Object();
        Object value3 = new Object();
        Object value4 = new Object();
        EvalContext context = mock(EvalContext.class);

        when(context.getValue(args[0])).thenReturn(value1);
        when(context.getValue(args[1])).thenReturn(value2);
        when(context.getValue(args[1])).thenReturn(value3);
        when(context.getValue(args[1])).thenReturn(value4);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(mock(Expression.class), mock(Expression.class));

        assertFalse(coreOperationNotEqual.computeValue(context));
        assertNotEquals(value1, value2);
        assertTrue(coreOperationNotEqual.computeValue(context));
        assertNotEquals(value3, value4);
        assertFalse(coreOperationNotEqual.computeValue(context));
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} .
     */
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(mock(Expression.class), mock(Expression.class));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual()} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualEmptyConstructor() {
        new CoreOperationNotEqual();
    }

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression)} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualOneArgumentConstructor() {
        new CoreOperationNotEqual(mock(Expression.class));
    }
}