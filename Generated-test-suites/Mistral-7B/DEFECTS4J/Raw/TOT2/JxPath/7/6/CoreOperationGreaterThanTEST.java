package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreNumberExpression(3.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeNumbers() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(-5.0), new CoreNumberExpression(-3.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithInfiniteAndFinite() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(Double.POSITIVE_INFINITY), new CoreNumberExpression(3.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroAndNegativeZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(0.0), new CoreNumberExpression(-0.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test(expected = ClassCastException.class)
    public void testExceptionWhenArgumentsAreNotNumbers() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreStringExpression("5"), new CoreNumberExpression(3.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testExceptionWhenArgumentsAreBothStrings() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreStringExpression("5"), new CoreStringExpression("3"));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testExceptionWhenArgumentsAreOneStringOneNumber() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreStringExpression("5"), new CoreNumberExpression(3.0));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testExceptionWhenFirstArgumentIsStringAndSecondIsString() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreStringExpression("5"), new CoreStringExpression("3"));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenBothArgumentsAreNaN() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(Double.NaN), new CoreNumberExpression(Double.NaN));

        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
        context.setInfoSet(infoSet);

        coreOperationGreaterThan.computeValue(context);
    }
}