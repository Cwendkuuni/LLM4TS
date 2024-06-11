package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expressions.Constant;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant(2), new Constant(1));
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant(1), new Constant(1));
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant(null), new Constant(1));
        assertNull(coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Constant(1), new Constant(null));
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant(null), new Constant(1));
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}