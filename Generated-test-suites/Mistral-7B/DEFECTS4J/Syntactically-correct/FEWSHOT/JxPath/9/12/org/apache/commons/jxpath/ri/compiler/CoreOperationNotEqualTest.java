package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.CoreFunctions;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstant(Boolean.TRUE), new CoreConstant(Boolean.FALSE));

        assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstant(Boolean.FALSE), new CoreConstant(Boolean.TRUE));

        assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstant(new Integer(1)), new CoreConstant(new Integer(1)));

        assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstant(new Integer(1)), new CoreConstant(new Integer(2)));

        assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new CoreConstant(new Integer(1)), new CoreConstant(new Integer(1)));

        Expression expected = new CoreFunctionCall(CoreFunctions.NOT, coreOperationCompare);

        CoreOperationNotEqual coreOperationNotEqualExpected = new CoreOperationNotEqual(coreOperationCompare, new CoreConstant(CoreFunctions.FUNCTION));

        assertEquals(expected.getClass(), coreOperationNotEqualExpected.getClass());
    }
}