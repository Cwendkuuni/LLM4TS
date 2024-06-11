package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new CoreOperationEqualArg(1);
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
    }

    /**
     * Test computeValue method with equal values.
     */
    @Test
    public void testComputeValue_equal() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationEqualArg(1);
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    /**
     * Test computeValue method with not equal values.
     */
    @Test
    public void testComputeValue_notEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationEqualArg(1);
        Expression arg2 = new CoreOperationEqualArg(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    /**
     * Test computeValue method with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullArguments() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    /**
     * Test computeValue method with null context.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullContext() {
        Expression arg1 = new CoreOperationEqualArg(1);
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(null);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreOperationEqualArg(1);
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    /**
     * Test computeValue method with equal null values.
     */
    @Test
    public void testComputeValue_equalNullValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationEqualArg(null);
        Expression arg2 = new CoreOperationEqualArg(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    /**
     * Test computeValue method with not equal null values.
     */
    @Test
    public void testComputeValue_notEqualNullValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationEqualArg(null);
        Expression arg2 = new CoreOperationEqualArg(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}