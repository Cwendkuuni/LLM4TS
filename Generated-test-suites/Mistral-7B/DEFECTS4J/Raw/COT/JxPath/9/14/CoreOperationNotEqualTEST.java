package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for CoreOperationNotEqual.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoreOperationNotEqualTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    @InjectMocks
    private CoreOperationNotEqual coreOperationNotEqual;

    @InjectMocks
    private CoreOperationCompare coreOperationCompare;

    private Expression[] args;

    @Before
    public void setup() {
        args = new Expression[]{arg1, arg2};
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationNotEqual() {
        assertNotNull(coreOperationNotEqual);
    }

    /**
     * Test method computeValue.
     */
    @Test
    public void testComputeValue() {
        when(arg1.computeValue(context)).thenReturn("1");
        when(arg2.computeValue(context)).thenReturn("2");

        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false.", result instanceof Boolean);
        assertFalse((Boolean) result);

        when(arg1.computeValue(context)).thenReturn(new Integer(1));
        when(arg2.computeValue(context)).thenReturn(new Integer(1));

        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false.", result instanceof Boolean);
        assertTrue((Boolean) result);

        when(arg1.computeValue(context)).thenReturn("1");
        when(arg2.computeValue(context)).thenReturn("1");

        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false.", result instanceof Boolean);
        assertFalse((Boolean) result);

        when(arg1.computeValue(context)).thenReturn(new Integer(1));
        when(arg2.computeValue(context)).thenReturn(new Integer(2));

        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false.", result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method getSymbol.
     */
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=".intern(), symbol);
    }

    /**
     * Test method equals with null arg1.
     */
    @Test(expected = NullPointerException.class)
    public void testEqualsNullArg1() {
        coreOperationNotEqual.equals(null);
    }

    /**
     * Test method equals with null arg2.
     */
    @Test(expected = NullPointerException.class)
    public void testEqualsNullArg2() {
        coreOperationNotEqual.equals(coreOperationCompare);
        coreOperationNotEqual.equals(null);
    }

    /**
     * Test method equals with same object.
     */
    @Test
    public void testEqualsSameObject() {
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual2 = new CoreOperationNotEqual(arg1, arg2);

        assertTrue(coreOperationNotEqual1.equals(coreOperationNotEqual2));
        assertTrue(coreOperationNotEqual2.equals(coreOperationNotEqual1));
    }

    /**
     * Test method equals with different object.
     */
    @Test
    public void testEqualsDifferentObject() {
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(arg1, arg2);

        assertFalse(coreOperationNotEqual1.equals(coreOperationCompare1));
        assertFalse(coreOperationCompare1.equals(coreOperationNotEqual1));
    }

    /**
     * Test method hashCode.
     */
    @Test
    public void testHashCode() {
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual2 = new CoreOperationNotEqual(arg1, arg2);

        assertEquals(coreOperationNotEqual1.hashCode(), coreOperationNotEqual2.hashCode());
    }
}