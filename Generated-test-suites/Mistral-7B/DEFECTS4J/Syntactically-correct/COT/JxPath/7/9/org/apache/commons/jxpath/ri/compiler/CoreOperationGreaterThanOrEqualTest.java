package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(5.0),
                TestUtils.createDoubleExpression(5.0));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDifferentTypes() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(5.0),
                TestUtils.createStringExpression("5"));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg1() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                null, TestUtils.createDoubleExpression(5.0));
        EvalContext context = TestUtils.createTestContext();
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg2() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(5.0),
                null);
        EvalContext context = TestUtils.createTestContext();
        operation.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(-3.0),
                TestUtils.createDoubleExpression(-2.0));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(0.0),
                TestUtils.createDoubleExpression(5.0));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZeroAndZero() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(0.0),
                TestUtils.createDoubleExpression(0.0));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithMaxDoubleValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(Double.MAX_VALUE),
                TestUtils.createDoubleExpression(Double.MAX_VALUE));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithMinDoubleValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(Double.MIN_VALUE),
                TestUtils.createDoubleExpression(Double.MIN_VALUE));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                TestUtils.createDoubleExpression(Double.NaN),
                TestUtils.createDoubleExpression(Double.NaN));
        EvalContext context = TestUtils.createTestContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.NaN, result);
    }
}