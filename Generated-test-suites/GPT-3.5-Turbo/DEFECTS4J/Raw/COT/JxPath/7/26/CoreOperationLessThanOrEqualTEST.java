import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_BothArgsAreEqual() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(10);
        when(arg2.computeValue(context)).thenReturn(10);

        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Arg1IsLessThanArg2() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(5);
        when(arg2.computeValue(context)).thenReturn(10);

        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Arg1IsGreaterThanArg2() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(10);
        when(arg2.computeValue(context)).thenReturn(5);

        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
        Assert.assertEquals("<=", operation.getSymbol());
    }
    
    @Test
    public void testComputeValue_EvaluationException() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenThrow(new EvaluationException());
        when(arg2.computeValue(context)).thenReturn(10);

        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_NullArgs() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(null);
        when(arg2.computeValue(context)).thenReturn(10);

        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }
}