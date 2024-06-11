package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreConstant;
import org.apache.commons.jxpath.ri.core.CoreFunctionCall;
import org.apache.commons.jxpath.ri.core.CoreValue;
import org.junit.Test;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)}.
     */
    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreConstant(new CoreValue("test1"));
        Expression arg2 = new CoreConstant(new CoreValue("test2"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqual);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstant(new CoreValue("test1")),
                new CoreConstant(new CoreValue("test2")));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreConstant(new CoreValue(1));
        Expression arg2 = new CoreConstant(new CoreValue(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertFalse((Boolean) coreOperationNotEqual.computeValue(context));

        arg1 = new CoreConstant(new CoreValue("test1"));
        arg2 = new CoreConstant(new CoreValue("test1"));

        assertTrue((Boolean) coreOperationNotEqual.computeValue(context));

        CoreFunctionCall functionCall = new CoreFunctionCall("not", arg1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, functionCall);

        assertTrue((Boolean) coreOperationNotEqual.computeValue(context));

        coreOperationNotEqual = new CoreOperationNotEqual(functionCall, arg1);

        assertFalse((Boolean) coreOperationNotEqual.computeValue(context));
    }
}