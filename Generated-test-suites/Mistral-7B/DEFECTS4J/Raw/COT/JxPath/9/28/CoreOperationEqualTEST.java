package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_WhenBothArgsAreSameObject_ThenResultShouldBeTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(1));
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_WhenFirstArgIsNullAndSecondArgIsNotNull_ThenResultShouldBeFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(null), new Constant("Test"));
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_WhenFirstArgIsNotNullAndSecondArgIsNull_ThenResultShouldBeFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("Test"), new Constant(null));
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_WhenBothArgsAreDifferentObjects_ThenResultShouldBeFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(1), new Constant(2));
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_WhenOneOrBothArgsAreNotExpressions_ThenExceptionShouldBeThrown() {
        new CoreOperationEqual(new Constant(1), "Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_WhenOneArgIsAnArrayAndAnotherIsANumericValue_ThenExceptionShouldBeThrown() {
        new CoreOperationEqual(new ArrayConstant(new Object[]{"Test"}), new Constant(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_WhenBothArgsAreArraysButDifferentSize_ThenExceptionShouldBeThrown() {
        new CoreOperationEqual(new ArrayConstant(new Object[]{"Test1", "Test2"}), new ArrayConstant(new Object[]{"Test1"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_WhenBothArgsAreArraysButNotSameType_ThenExceptionShouldBeThrown() {
        new CoreOperationEqual(new ArrayConstant(new Object[]{"Test1", "Test2"}), new ArrayConstant(new String[]{"Test1", "Test3"}));
    }

    @Test
    public void testCoreOperationEqual_WhenBothArgsAreArraysAndSameSizeAndAllValuesAreEqual_ThenResultShouldBeTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[]{"Test1", "Test2"}), new ArrayConstant(new Object[]{"Test1", "Test2"}));
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_WhenBothArgsAreArraysAndSomeValuesAreEqualButSomeAreDifferent_ThenResultShouldBeFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ArrayConstant(new Object[]{"Test1", "Test2"}), new ArrayConstant(new Object[]{"Test1", "Test3"}));
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
}