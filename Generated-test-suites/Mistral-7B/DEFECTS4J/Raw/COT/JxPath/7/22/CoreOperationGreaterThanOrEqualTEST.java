package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual.CoreOperationGreaterThanOrEqualArg;
import org.junit.Test;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenTwoDoubleValuesAreEqual_thenReturnTrue() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(1.5), new ConstantExpression(1.5));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenFirstDoubleValueIsGreaterThanSecond_thenReturnTrue() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(5.0), new ConstantExpression(3.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenFirstDoubleValueIsSmallerThanSecond_thenReturnFalse() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(2.5), new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_whenExpressionTypesAreDifferent_thenThrowException() {
        Expression arg1 = new ConstantExpression("test");
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_whenBothExpressionsAreStrings_thenThrowException() {
        Expression arg1 = new ConstantExpression("test1");
        Expression arg2 = new ConstantExpression("test2");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenFirstExpressionReturnsNaN_thenReturnFalse() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(Double.NaN), new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenSecondExpressionReturnsNaN_thenReturnFalse() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(5.0), new ConstantExpression(Double.NaN));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenFirstExpressionReturnsPositiveInfinite_thenReturnFalse() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenSecondExpressionReturnsPositiveInfinite_thenReturnFalse() {
        CoreOperationGreaterThanOrEqualArg arg1 = new CoreOperationGreaterThanOrEqualArg(new ConstantExpression(5.0), new ConstantExpression(Double.POSITIVE_INFINITY));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1.getExpression1(), arg1.getExpression2());
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}