package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFalseResult() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(6.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithBooleanArg1() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithBooleanArg2() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg1() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg2() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreNullExpression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithMixedTypes() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleNaN() {
        Expression arg1 = new CoreConstantExpression(Double.NaN);
        Expression arg2 = new CoreConstantExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleNegativeInfinity() {
        Expression arg1 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoublePositiveInfinity() {
        Expression arg1 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleNegativeNumber() {
        Expression arg1 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(new Double(-1.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoublePositiveNumber() {
        Expression arg1 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(new Double(1.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleSmallerThanOther() {
        Expression arg1 = new CoreConstantExpression(new Double(1.0));
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleGreaterThanOther() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(1.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDoubleEqual() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result);
    }
}