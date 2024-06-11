package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import org.junit.Assert;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDifferentTypes() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreString("5");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithUnequalValues() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
        Assert.assertFalse(result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithLessThanTwoArguments() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArguments2() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        Expression arg1 = new CoreNumber(Double.NaN);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeAndPositiveValues() {
        Expression arg1 = new CoreNumber(-5.0);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
        Assert.assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZeroAndPositiveValues() {
        Expression arg1 = new CoreNumber(0.0);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
        Assert.assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZeroAndZero() {
        Expression arg1 = new CoreNumber(0.0);
        Expression arg2 = new CoreNumber(0.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithPositiveAndZero() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(0.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithPositiveAndNegativeValues() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(-5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
        Assert.assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeAndNegativeValues() {
        Expression arg1 = new CoreNumber(-5.0);
        Expression arg2 = new CoreNumber(-5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue(result.equals(Boolean.TRUE));
    }
}