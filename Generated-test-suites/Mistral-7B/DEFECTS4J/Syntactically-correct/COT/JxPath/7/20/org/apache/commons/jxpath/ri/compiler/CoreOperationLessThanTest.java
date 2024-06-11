package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.expr.DoubleValue;
import org.junit.Test;

/**
 * Test cases for CoreOperationLessThan class in org.apache.commons.jxpath.ri.compiler package.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanBasicFunctionality() {
        // Given
        Expression arg1 = new Path("$[1.0]");
        Expression arg2 = new Path("$[2.0]");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), null);

        // When
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then
        Assert.assertFalse(result);

        arg1 = new Path("$[1.5]");
        arg2 = new Path("$[1.2]");
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        Assert.assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanNullArgs() {
        // Given
        Expression arg1 = null;
        Expression arg2 = new Path("$[2.0]");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When and Then - should throw an IllegalArgumentException
        coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanArg2Null() {
        // Given
        Expression arg1 = new Path("$[1.0]");
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When and Then - should throw an IllegalArgumentException
        coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), null));
    }

    @Test
    public void testCoreOperationLessThanMixedTypes() {
        // Given
        Expression arg1 = new DoubleValue(1.5);
        Expression arg2 = new Path("$[2.0]");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Double result = (Double) coreOperationLessThan.computeValue(context);

        // Then - should return false, since 1.5 is not less than any double value
        Assert.assertFalse(result.equals(Boolean.FALSE));
    }

    @Test
    public void testCoreOperationLessThanBothNull() {
        // Given
        Expression arg1 = new Path("$[null]");
        Expression arg2 = new Path("$[null]");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then - should return false, since null is not less than null
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanZeroAndNegativeNumber() {
        // Given
        Expression arg1 = new DoubleValue(0.0);
        Expression arg2 = new DoubleValue(-1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanZeroAndPositiveNumber() {
        // Given
        Expression arg1 = new DoubleValue(0.0);
        Expression arg2 = new DoubleValue(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanSameValues() {
        // Given
        Expression arg1 = new DoubleValue(1.0);
        Expression arg2 = new DoubleValue(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanNaN() {
        // Given
        Expression arg1 = new DoubleValue(Double.NaN);
        Expression arg2 = new DoubleValue(Double.NaN);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then - should return undefined result for NaN comparison
        Assert.assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanNegativeNumberAndNaN() {
        // Given
        Expression arg1 = new DoubleValue(-1.0);
        Expression arg2 = new DoubleValue(Double.NaN);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then - should return false for negative number and NaN comparison
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanPositiveNumberAndNaN() {
        // Given
        Expression arg1 = new DoubleValue(1.0);
        Expression arg2 = new DoubleValue(Double.NaN);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);

        // Then - should return true for positive number and NaN comparison
        Assert.assertTrue(result);
    }
}