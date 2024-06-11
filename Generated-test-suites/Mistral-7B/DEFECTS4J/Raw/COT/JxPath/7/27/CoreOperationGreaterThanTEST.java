package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CoreOperationGreaterThan.class, InfoSetUtil.class })
public class CoreOperationGreaterThanTest {

    // Test methods go here

    @Test
    public void testCoreOperationGreaterThan_twoPositiveNumbers() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression(1.0), createExpression(0.5));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        Boolean result = (Boolean) operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_twoNegativeNumbers() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression(-1.0), createExpression(-2.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        Boolean result = (Boolean) operation.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_positiveAndNegativeNumber() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression(1.0), createExpression(-0.5));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        Boolean result = (Boolean) operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_invalidArgumentType() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression("abc"), createExpression(1.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_nullArguments() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, createExpression(1.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_nullRightArgument() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression(1.0), null);
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_argumentsOfDifferentTypes() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(createExpression(1.0), createExpression("abc"));
        InfoSet infoSet = createInfoSet();
        EvalContext context = createEvalContext(infoSet);

        operation.computeValue(context);
    }

    // Helper methods go here

    private Expression createExpression(double value) {
        return PowerMockito.mock(Expression.class);
    InfoSetUtil.when(InfoSetUtil.class).doubleValue(Mockito.any(Expression.class))
            .then((invocation) -> value);

    return (Expression) invocation.getArguments()[0];
    }

    private Expression createExpression(InfoSet infoSet, int index) {
        return PowerMockito.mock(Expression.class);
    InfoSetUtil.when(InfoSetUtil.class).doubleValue(Mockito.any(Expression.class))
            .thenReturn(InfoSetUtil.doubleValue(infoSet, index));

    return (Expression) invocation.getArguments()[0];
    }

    private Expression createExpression(String value) {
        return PowerMockito.mock(Expression.class);
    InfoSetUtil.when(InfoSetUtil.class).doubleValue(Mockito.any(Expression.class))
            .thenThrow(new IllegalArgumentException());

    NumInfo numInfo = PowerMockito.mock(NumInfo.class);
    InfoSetUtil.when(InfoSetUtil.class).createNumberInfo(Mockito.any(EvalContext.class), Mockito.anyInt(), Mockito.anyInt())
            .thenReturn(numInfo);

    PowerMockito.when(numInfo.getValue(), Mockito.anyLong()).thenReturn(Double.parseDouble(value));

    return (Expression) invocation.getArguments()[0];
    }

    private Expression[] createArguments(double... values) {
        Expression[] result = new Expression[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = createExpression(values[i]);
        }

        return result;
    }

    private InfoSet createInfoSet() {
        InfoSet result = PowerMockito.mock(InfoSet.class);
        InfoSetUtil.when(InfoSetUtil.class).createInfoSet()
                .thenReturn(result);
        return result;
    }

    private EvalContext createEvalContext(InfoSet infoSet) {
        EvalContext result = PowerMockito.mock(EvalContext.class);
        PowerMockito.when(EvalContext.class.newInstance(infoSet))
                .thenReturn(result);
        return result;
    }
}