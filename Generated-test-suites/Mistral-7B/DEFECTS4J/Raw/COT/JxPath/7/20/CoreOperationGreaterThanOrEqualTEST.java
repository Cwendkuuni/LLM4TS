package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.jxpath.ri.test.TestConstants.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenBothArgumentsAreNumbersAndTheFirstOneIsGreaterOrEqualToTheSecondOne_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5.0), new Literal(5.0));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenBothArgumentsAreNumbersAndTheFirstOneIsLessThanTheSecondOne_thenReturnFalse() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(2.0), new Literal(5.0));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenBothArgumentsAreNumbersAndTheFirstOneIsEqualToTheSecondOne_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5.0), new Literal(5.0));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_whenArgumentsHaveDifferentTypes_thenThrowClassCastException() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5), new Literal(2.5));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenArgumentsAreBothNegativeNumbersAndTheFirstOneIsGreaterOrEqualToTheSecondOne_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(-3.0), new Literal(-2.0));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenArgumentsAreBothNegativeNumbersAndTheFirstOneIsLessThanTheSecondOne_thenReturnFalse() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(-3.0), new Literal(-2.5));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_whenArgumentsAreBothNegativeNumbersAndTheFirstOneIsEqualToTheSecondOne_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(-5.0), new Literal(-5.0));
        EvalContext context = TestUtils.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}