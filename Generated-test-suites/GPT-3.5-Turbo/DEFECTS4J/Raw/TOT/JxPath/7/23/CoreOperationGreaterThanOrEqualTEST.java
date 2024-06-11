import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual greaterThanOrEqual;

    @Test
    public void testComputeValueWithBothArgsGreater() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(5.0);
        when(arg2.computeValue(context)).thenReturn(2.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithBothArgsEqual() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(3.0);
        when(arg2.computeValue(context)).thenReturn(3.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithFirstArgSmaller() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(1.0);
        when(arg2.computeValue(context)).thenReturn(3.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNegativeArgs() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(-2.0);
        when(arg2.computeValue(context)).thenReturn(-5.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNaNArgs() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(Double.NaN);
        when(arg2.computeValue(context)).thenReturn(Double.NaN);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithOneArgNaN() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(Double.NaN);
        when(arg2.computeValue(context)).thenReturn(5.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithOneArgInfinite() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(Double.POSITIVE_INFINITY);
        when(arg2.computeValue(context)).thenReturn(5.0);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithSecondArgInfinite() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(5.0);
        when(arg2.computeValue(context)).thenReturn(Double.NEGATIVE_INFINITY);

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = greaterThanOrEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = greaterThanOrEqual.getSymbol();

        Assert.assertEquals(">=", symbol);
    }

}