import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationLessThanOrEqualTest {

    private EvalContext context;
    private CoreOperationLessThanOrEqual operation;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setup() {
        context = Mockito.spy(new EvalContext());
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsLessThanSecond_thenReturnTrue() {
        double arg1Value = 5.0;
        double arg2Value = 10.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsGreaterThanSecond_thenReturnFalse() {
        double arg1Value = 10.0;
        double arg2Value = 5.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsEqualToSecond_thenReturnTrue() {
        double arg1Value = 5.0;
        double arg2Value = 5.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsNaN_thenReturnFalse() {
        double arg1Value = Double.NaN;
        double arg2Value = 5.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsNegativeInfinity_thenReturnFalse() {
        double arg1Value = Double.NEGATIVE_INFINITY;
        double arg2Value = 5.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNumbersAndFirstIsPositiveInfinity_thenReturnFalse() {
        double arg1Value = Double.POSITIVE_INFINITY;
        double arg2Value = 5.0;

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2Value);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_whenOneArgIsNotANumber_thenThrowClassCastException() {
        InfoSet arg1InfoSet = Mockito.mock(InfoSet.class);
        Mockito.when(arg1.computeValue(context)).thenReturn(arg1InfoSet);

        Object result = operation.computeValue(context);

        fail("Expected ClassCastException but got: " + result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_whenOneArgIsANumberAndAnotherIsInfoSet_thenThrowClassCastException() {
        double arg1Value = 5.0;
        InfoSet arg2InfoSet = Mockito.mock(InfoSet.class);

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1Value);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2InfoSet);

        Object result = operation.computeValue(context);

        fail("Expected ClassCastException but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenBothArgsAreInfoSets_thenThrowIllegalArgumentException() {
        InfoSet arg1InfoSet = Mockito.mock(InfoSet.class);
        InfoSet arg2InfoSet = Mockito.mock(InfoSet.class);

        Mockito.when(arg1.computeValue(context)).thenReturn(arg1InfoSet);
        Mockito.when(arg2.computeValue(context)).thenReturn(arg2InfoSet);

        Object result = operation.computeValue(context);

        fail("Expected IllegalArgumentException but got: " + result);
    }
}