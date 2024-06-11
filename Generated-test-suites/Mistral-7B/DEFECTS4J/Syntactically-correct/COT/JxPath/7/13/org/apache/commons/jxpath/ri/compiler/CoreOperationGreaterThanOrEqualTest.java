package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.jxpath.ri.test.TestConstants.CONTEXT;
import static org.apache.commons.jxpath.ri.test.TestConstants.TEST_DOCUMENT;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    // Test method for CoreOperationGreaterThanOrEqual constructor
    @Test
    public void testCoreOperationGreaterThanOrEqualConstructor() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(1.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(2.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    // Test method for computeValue method with positive numbers
    @Test
    public void testComputeValueWithPositiveNumbers() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    // Test method for computeValue method with negative numbers
    @Test
    public void testComputeValueWithNegativeNumbers() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(-3.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(-2.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    // Test method for computeValue method with equal numbers
    @Test
    public void testComputeValueWithEqualNumbers() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    // Test method for computeValue method with one NaN number
    @Test(expected = ArithmeticException.class)
    public void testComputeValueWithNaN() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(Double.NaN, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
    }

    // Test method for computeValue method with two NaN numbers
    @Test(expected = ArithmeticException.class)
    public void testComputeValueWithBothNaN() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(Double.NaN, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(Double.NaN, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
    }

    // Test method for computeValue method with zero and negative number
    @Test
    public void testComputeValueWithZeroAndNegative() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(0.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(-2.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    // Test method for computeValue method with zero and positive number
    @Test
    public void testComputeValueWithZeroAndPositive() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(0.0, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(5.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    // Test method for computeValue method with very large numbers
    @Test
    public void testComputeValueWithVeryLargeNumbers() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(Double.POSITIVE_INFINITY, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(Double.POSITIVE_INFINITY, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    // Test method for computeValue method with very large and small numbers
    @Test
    public void testComputeValueWithVeryLargeAndSmallNumbers() {
        Expression arg1 = TestUtils.createSimpleNumberExpression(Double.POSITIVE_INFINITY, TEST_DOCUMENT);
        Expression arg2 = TestUtils.createSimpleNumberExpression(1.0, TEST_DOCUMENT);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = coreOperationGreaterThanOrEqual.computeValue(CONTEXT);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}