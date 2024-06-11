package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualConstructor() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant(1));
        assertSame(CoreOperationEqual.class, operation.getClass());
    }

    @Test
    public void testCoreOperationEqualComputeValueTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant(1));
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqualComputeValueFalse() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant(2));
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArguments() {
        new CoreOperationEqual(null, null);
    }

    @Test
    public void testCoreOperationEqualGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant(1));
        assertEquals("=", operation.getSymbol());
    }

    @Test
    public void testCoreOperationEqualNullContext() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant(1));
        assertNull(operation.computeValue(null));
    }

    @Test
    public void testCoreOperationEqualNullArg1() {
        CoreOperationEqual operation = new CoreOperationEqual(null, new Constant(1));
        assertNull(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualNullArg2() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), null);
        assertNull(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualDifferentTypes() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(1), new Constant("1"));
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}