import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.eval.*;
import org.apache.commons.jxpath.ri.*;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(operation);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        when(arg1.computeValue(context)).thenReturn(10);
        when(arg2.computeValue(context)).thenReturn(20);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArg1() {
        Expression arg2 = mock(Expression.class);
        new CoreOperationLessThan(null, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArg2() {
        Expression arg1 = mock(Expression.class);
        new CoreOperationLessThan(arg1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        operation.computeValue(null);
    }

    // Additional test cases for other identified edge cases and exceptions

}