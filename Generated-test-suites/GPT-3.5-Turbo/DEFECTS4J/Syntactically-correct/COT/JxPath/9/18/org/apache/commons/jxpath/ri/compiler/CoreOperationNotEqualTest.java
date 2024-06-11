import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(operation);
        assertEquals(arg1, operation.args[0]);
        assertEquals(arg2, operation.args[1]);
    }

    @Test
    public void testComputeValue_equalArguments_returnFalse() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        when(operation.equal(context, arg1, arg2)).thenReturn(true);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_notEqualArguments_returnTrue() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        when(operation.equal(context, arg1, arg2)).thenReturn(false);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }

}