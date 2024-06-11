package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.FunctionDefinition;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.junit.Test;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualBasic() {
        Expression arg1 = ComplierTest.compile("1");
        Expression arg2 = ComplierTest.compile("2");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertFalse("Result should be false", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationNotEqualTrue() {
        Expression arg1 = ComplierTest.compile("true");
        Expression arg2 = ComplierTest.compile("false");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue("Result should be true", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationNotEqualFalse() {
        Expression arg1 = ComplierTest.compile("false");
        Expression arg2 = ComplierTest.compile("true");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertFalse("Result should be false", result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullArg1() {
        Expression arg1 = null;
        Expression arg2 = ComplierTest.compile("1");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullArg2() {
        Expression arg1 = ComplierTest.compile("1");
        Expression arg2 = null;

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqualFunctionDefinition() {
        FunctionDefinition funcDef = CoreOperationNotEqual.getFunctionDefinition();

        assertNotNull("Function definition should not be null", funcDef);
        assertTrue("Function name should be '!=', is " + funcDef.getName(), funcDef.getName().equals("!"));
    }

    @Test
    public void testCoreOperationNotEqualWithCoreOperationCompare() {
        Expression arg1 = ComplierTest.compile("1");
        Expression arg2 = ComplierTest.compile("1");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(coreOperationCompare, CoreOperationConstants.XPATH_FALSE);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertFalse("Result should be false", result.equals(Boolean.TRUE));

        coreOperationNotEqual = new CoreOperationNotEqual(CoreOperationConstants.XPATH_FALSE, coreOperationCompare);

        context = new EvalContext();
        result = coreOperationNotEqual.computeValue(context);

        assertTrue("Result should be true", result.equals(Boolean.TRUE));
    }

}