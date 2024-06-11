package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.TestUtil;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    @SuppressWarnings("unchecked")
    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestUtil.setUp();
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreOperationCompare(new ConstantExpression(1), new ConstantExpression(1)),
                new CoreOperationCompare(new ConstantExpression(2), new ConstantExpression(1)));
        assertEquals("!=" , coreOperationNotEqual.getSymbol());
    }

    /**
     * Test computeValue method with basic input.
     */
    @org.junit.Test
    public void testComputeValueBasicInput() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with equal inputs.
     */
    @org.junit.Test
    public void testComputeValueEqualInputs() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with same instance.
     */
    @org.junit.Test
    public void testComputeValueSameInstance() {
        Expression arg1 = new ConstantExpression(new Object());
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with null inputs.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testComputeValueNullInputs() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    /**
     * Test computeValue method with different types of inputs.
     */
    @org.junit.Test
    public void testComputeValueDifferentTypesInputs() {
        Expression arg1 = new ConstantExpression("test");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with NaN inputs.
     */
    @org.junit.Test
    public void testComputeValueNaNInputs() {
        Expression arg1 = new ConstantExpression(Double.NaN);
        Expression arg2 = new ConstantExpression(Double.NaN);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with boolean inputs.
     */
    @org.junit.Test
    public void testComputeValueBooleanInputs() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with different boolean values.
     */
    @org.junit.Test
    public void testComputeValueBooleanValues() {
        Expression arg1 = new ConstantExpression(Boolean.FALSE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with null output.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testComputeValueNullOutput() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    /**
     * Test computeValue method with empty arrays.
     */
    @org.junit.Test
    public void testComputeValueEmptyArrays() {
        Expression arg1 = new ArrayExpression(new Object[0].getClass(), new Object[0]);
        Expression arg2 = new ArrayExpression(new Object[0].getClass(), new Object[0]);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with arrays of different sizes.
     */
    @org.junit.Test
    public void testComputeValueArraysDifferentSizes() {
        Expression arg1 = new ArrayExpression(Integer.TYPE, new Object[] { 1, 2, 3 });
        Expression arg2 = new ArrayExpression(Integer.TYPE, new Object[] { 1, 2 });
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with arrays containing same elements.
     */
    @org.junit.Test
    public void testComputeValueArraysSameElements() {
        Expression arg1 = new ArrayExpression(Integer.TYPE, new Object[] { 1, 2, 3 });
        Expression arg2 = new ArrayExpression(Integer.TYPE, new Object[] { 2, 3, 1 });
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with arrays containing different elements.
     */
    @org.junit.Test
    public void testComputeValueArraysDifferentElements() {
        Expression arg1 = new ArrayExpression(Integer.TYPE, new Object[] { 1, 2, 3 });
        Expression arg2 = new ArrayExpression(Integer.TYPE, new Object[] { 4, 5, 6 });
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with arrays of different types.
     */
    @org.junit.Test
    public void testComputeValueArraysDifferentTypes() {
        Expression arg1 = new ArrayExpression(String.class, new Object[] { "test1", "test2" });
        Expression arg2 = new ArrayExpression(Integer.TYPE, new Object[] { 1, 2, 3 });
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }
}