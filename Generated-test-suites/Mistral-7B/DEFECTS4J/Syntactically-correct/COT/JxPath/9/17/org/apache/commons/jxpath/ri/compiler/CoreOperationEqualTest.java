package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;

/**
 * Test cases for CoreOperationEqual class.
 */
public class CoreOperationEqualTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("CoreOperationEqual instance was not initialized correctly.", arg1, coreOperationEqual.getArg1());
        assertEquals("CoreOperationEqual instance was not initialized correctly.", arg2, coreOperationEqual.getArg2());
    }

    /**
     * Test computeValue method with two Boolean arguments equal to true.
     */
    @org.junit.Test
    public void testComputeValueTwoBooleanArgsTrue() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue("Test failed: expected true but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with two Boolean arguments equal to false.
     */
    @org.junit.Test
    public void testComputeValueTwoBooleanArgsFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.FALSE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue("Test failed: expected true but got: " + result, result == Boolean.FALSE);
    }

    /**
     * Test computeValue method with two Boolean arguments of different values.
     */
    @org.junit.Test
    public void testComputeValueTwoBooleanArgsDifferentValues() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertFalse("Test failed: expected false but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with two Number arguments equal to each other.
     */
    @org.junit.Test
    public void testComputeValueTwoNumberArgsEqual() {
        Expression arg1 = new CoreConstantExpression(new Double(1.0));
        Expression arg2 = new CoreConstantExpression(new Double(1.0));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue("Test failed: expected true but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with two Number arguments of different values.
     */
    @org.junit.Test
    public void testComputeValueTwoNumberArgsDifferent() {
        Expression arg1 = new CoreConstantExpression(new Double(1.0));
        Expression arg2 = new CoreConstantExpression(new Double(2.0));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertFalse("Test failed: expected false but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with two String arguments equal to each other.
     */
    @org.junit.Test
    public void testComputeValueTwoStringArgsEqual() {
        Expression arg1 = new CoreConstantExpression("test");
        Expression arg2 = new CoreConstantExpression("test");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue("Test failed: expected true but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with two String arguments of different values.
     */
    @org.junit.Test
    public void testComputeValueTwoStringArgsDifferent() {
        Expression arg1 = new CoreConstantExpression("test");
        Expression arg2 = new CoreConstantExpression("other");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertFalse("Test failed: expected false but got: " + result, result == Boolean.TRUE);
    }

    /**
     * Test computeValue method with null argument.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArgument() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    /**
     * Test getSymbol method.
     */
    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("Test failed: expected '=' but got: " + symbol, symbol, "=");
    }
}