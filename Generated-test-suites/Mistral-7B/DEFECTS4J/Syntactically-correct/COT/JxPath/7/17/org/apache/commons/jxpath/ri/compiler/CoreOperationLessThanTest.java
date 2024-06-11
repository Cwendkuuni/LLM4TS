package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationLessThan
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreNumbersAndFirstArgIsLessThanSecond_thenReturnTrue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(2.5),
                new CoreNumberExpression(3.5));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreNumbersAndFirstArgIsGreaterThanOrEqualSecond_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(3.5),
                new CoreNumberExpression(2.5));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreNumbersAndBothAreEqual_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(2.5),
                new CoreNumberExpression(2.5));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationLessThan_whenFirstArgIsNotANumber_thenThrowIllegalStateException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreStringExpression("2.5"),
                new CoreNumberExpression(3.5));
        EvalContext context = new EvalContext();
        coreOperationLessThan.computeValue(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationLessThan_whenSecondArgIsNotANumber_thenThrowIllegalStateException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(2.5),
                new CoreStringExpression("3.5"));
        EvalContext context = new EvalContext();
        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThan_whenFirstArgIsNaN_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(Double.NaN),
                new CoreNumberExpression(3.5));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenSecondArgIsNaN_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(2.5),
                new CoreNumberExpression(Double.NaN));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreBooleansAndFirstArgIsFalse_thenReturnTrue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreBooleanExpression(Boolean.FALSE),
                new CoreBooleanExpression(Boolean.TRUE));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreBooleansAndBothAreTrue_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreBooleanExpression(Boolean.TRUE),
                new CoreBooleanExpression(Boolean.TRUE));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThan_whenBothArgsAreBooleansAndFirstArgIsEqualToSecond_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreBooleanExpression(Boolean.TRUE),
                new CoreBooleanExpression(Boolean.TRUE));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationLessThan_whenBothArgsAreBooleansButOneIsNotABoolean_thenThrowIllegalStateException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreStringExpression("true"),
                new CoreBooleanExpression(Boolean.TRUE));
        EvalContext context = new EvalContext();
        coreOperationLessThan.computeValue(context);
    }
}