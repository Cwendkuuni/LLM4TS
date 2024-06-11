package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri. JaneValue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(1));
        assertSame(coreOperationEqual.getClass(), CoreOperationEqual.class);
    }

    @Test
    public void testComputeValueForBothNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(null), new Constant(null));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueForBothNotNullButDifferentTypes() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant("1"));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForBothNotNullAndEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(1));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForBothNotNullButDifferentValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(2));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(null), new Constant(1));
        assertNull(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueForSecondArgNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(null));
        assertNull(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueForFirstAndSecondArgSameInstance() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(new JaneValue(1)), new Constant(new JaneValue(1)));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgInstanceAndSecondArgConstant() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(new JaneValue(1)), new Constant(1));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgConstantAndSecondArgInstance() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(new JaneValue(1)));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgArrayAndSecondArgArray() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(2) }),
                new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(1) }));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgArrayAndSecondArgSingleElementArray() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(2) }),
                new ArrayConstant(new Object[] { new JaneValue(1) }));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgSingleElementArrayAndSecondArgArray() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1) }),
                new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(1) }));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgSingleElementArrayAndSecondArgSingleElementArray() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1) }),
                new ArrayConstant(new Object[] { new JaneValue(1) }));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgSingleElementArrayAndSecondArgSingleElementDifferentValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1) }),
                new ArrayConstant(new Object[] { new JaneValue(2) }));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgArrayAndSecondArgSingleElementDifferentValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(2) }),
                new ArrayConstant(new Object[] { new JaneValue(2) }));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgArrayAndSecondArgSingleElementSameValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1), new JaneValue(1) }),
                new ArrayConstant(new Object[] { new JaneValue(1) }));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueForFirstArgSingleElementArrayAndSecondArgSingleElementSameValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[] { new JaneValue(1) }),
                new ArrayConstant(new Object[] { new JaneValue(1) }));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()).booleanValue());
    }

}