package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are numbers and the condition is true.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are numbers and the condition is false.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(6.0));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is a number and the second argument is a string.
     */
    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNumberSecondArgumentString() {
        new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new StringExpression("test"));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is a string and the second argument is a number.
     */
    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualFirstArgumentStringSecondArgumentNumber() {
        new CoreOperationGreaterThanOrEqual(new StringExpression("test"), new ConstantExpression(5.0));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are strings and one of them is null.
     */
    @Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNull() {
        new CoreOperationGreaterThanOrEqual(new NullExpression(), new ConstantExpression(5.0));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are strings and neither of them is null.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualBothArgumentsStrings() {
        String arg1 = "5.5";
        String arg2 = "6.0";
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new StringExpression(arg1), new StringExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        arg1 = "6.5";
        arg2 = "6.0";
        operation = new CoreOperationGreaterThanOrEqual(new StringExpression(arg1), new StringExpression(arg2));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are numbers with different data types.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualDifferentDataTypes() {
        double arg1 = 5.0;
        int arg2 = 6;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        arg1 = 6.0;
        operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are Infinity.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualBothArgumentsInfinity() {
        double arg1 = Double.POSITIVE_INFINITY;
        double arg2 = Double.POSITIVE_INFINITY;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is Infinity and the second argument is a number.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentInfinitySecondArgumentNumber() {
        double arg1 = Double.POSITIVE_INFINITY;
        double arg2 = 5.0;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is a number and the second argument is Infinity.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNumberSecondArgumentInfinity() {
        double arg1 = 5.0;
        double arg2 = Double.POSITIVE_INFINITY;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are Negative Infinity.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualBothArgumentsNegativeInfinity() {
        double arg1 = Double.NEGATIVE_INFINITY;
        double arg2 = Double.NEGATIVE_INFINITY;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is Negative Infinity and the second argument is a number.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNegativeInfinitySecondArgumentNumber() {
        double arg1 = Double.NEGATIVE_INFINITY;
        double arg2 = 5.0;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is a number and the second argument is Negative Infinity.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNumberSecondArgumentNegativeInfinity() {
        double arg1 = 5.0;
        double arg2 = Double.NEGATIVE_INFINITY;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when both arguments are NaN.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualBothArgumentsNaN() {
        double arg1 = Double.NaN;
        double arg2 = Double.NaN;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is NaN and the second argument is a number.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNaNSecondArgumentNumber() {
        double arg1 = Double.NaN;
        double arg2 = 5.0;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual constructor, when the first argument is a number and the second argument is NaN.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualFirstArgumentNumberSecondArgumentNaN() {
        double arg1 = 5.0;
        double arg2 = Double.NaN;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(arg1), new ConstantExpression(arg2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }
}