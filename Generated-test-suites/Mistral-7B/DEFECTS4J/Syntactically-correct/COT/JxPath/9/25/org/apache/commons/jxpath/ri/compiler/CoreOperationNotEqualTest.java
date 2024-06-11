package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Constants;
import org.junit.Test;

/**
 * Test class for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(new Integer(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=" , coreOperationNotEqual.getSymbol());
        assertNotNull(coreOperationNotEqual.getLeftExpression());
        assertNotNull(coreOperationNotEqual.getRightExpression());
    }

    /**
     * Test computeValue method with equal values.
     */
    @Test
    public void testComputeValueWithEqualValues() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(new Integer(1));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
    }

    /**
     * Test computeValue method with different values.
     */
    @Test
    public void testComputeValueWithDifferentValues() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(new Integer(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with null left argument.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullLeftArgument() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreConstantExpression(new Integer(1));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    /**
     * Test computeValue method with null right argument.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullRightArgument() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreNullExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    /**
     * Test computeValue method with different types.
     */
    @Test
    public void testComputeValueWithDifferentTypes() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(new String("Hello"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with the same type but different values, long and double.
     */
    @Test
    public void testComputeValueWithSameTypeButDifferentValuesLongAndDouble() {
        Expression arg1 = new CoreConstantExpression(new Long(1));
        Expression arg2 = new CoreConstantExpression(new Double(2.0));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with the same type but different values, double and long.
     */
    @Test
    public void testComputeValueWithSameTypeButDifferentValuesDoubleAndLong() {
        Expression arg1 = new CoreConstantExpression(new Double(1.0));
        Expression arg2 = new CoreConstantExpression(new Long(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with Boolean values.
     */
    @Test
    public void testComputeValueWithBooleanValues() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with Boolean values, true and true.
     */
    @Test
    public void testComputeValueWithBooleanValuesTrueAndTrue() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
    }

    /**
     * Test computeValue method with Boolean values, false and false.
     */
    @Test
    public void testComputeValueWithBooleanValuesFalseAndFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.FALSE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with Boolean values, true and false.
     */
    @Test
    public void testComputeValueWithBooleanValuesTrueAndFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }

    /**
     * Test computeValue method with Boolean values, false and true.
     */
    @Test
    public void testComputeValueWithBooleanValuesFalseAndTrue() {
        Expression arg1 = new CoreConstantExpression(Boolean.FALSE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }
}