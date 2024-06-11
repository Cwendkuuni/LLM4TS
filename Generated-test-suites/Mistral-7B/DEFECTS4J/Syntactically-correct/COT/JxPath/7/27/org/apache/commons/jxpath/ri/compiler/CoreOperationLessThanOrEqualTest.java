package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.TestUtils;
import org.junit.Test;

/**
 * Test cases for the CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_double_double() {
        Expression arg1 = TestUtils.createDoubleExpression(1.5);
        Expression arg2 = TestUtils.createDoubleExpression(2.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = TestUtils.createDoubleExpression(1.5);
        arg2 = TestUtils.createDoubleExpression(1.5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestUtils.createDoubleExpression(2.5);
        arg2 = TestUtils.createDoubleExpression(1.5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestUtils.createDoubleExpression(1.5);
        arg2 = TestUtils.createDoubleExpression(1.5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_illegal_arguments() {
        Expression arg1 = TestUtils.createStringExpression("1");
        Expression arg2 = TestUtils.createDoubleExpression(2.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_null_arguments() {
        Expression arg1 = null;
        Expression arg2 = TestUtils.createDoubleExpression(2.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_double_null() {
        Expression arg1 = TestUtils.createDoubleExpression(1.5);
        Expression arg2 = null;
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.UNKNOWN, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_null_double() {
        Expression arg1 = null;
        Expression arg2 = TestUtils.createDoubleExpression(1.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.UNKNOWN, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_double_NaN() {
        Expression arg1 = TestUtils.createDoubleExpression(1.5);
        Expression arg2 = TestUtils.createDoubleExpression(Double.NaN);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_NaN_double() {
        Expression arg1 = TestUtils.createDoubleExpression(Double.NaN);
        Expression arg2 = TestUtils.createDoubleExpression(1.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_negative_numbers() {
        Expression arg1 = TestUtils.createDoubleExpression(-1.5);
        Expression arg2 = TestUtils.createDoubleExpression(-2.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_zero() {
        Expression arg1 = TestUtils.createDoubleExpression(0.0);
        Expression arg2 = TestUtils.createDoubleExpression(0.0);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_zero_negative() {
        Expression arg1 = TestUtils.createDoubleExpression(0.0);
        Expression arg2 = TestUtils.createDoubleExpression(-1.5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_negative_zero() {
        Expression arg1 = TestUtils.createDoubleExpression(-1.5);
        Expression arg2 = TestUtils.createDoubleExpression(0.0);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createInfoSet();
        EvalContext context = TestUtils.createEvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}