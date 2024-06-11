import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object[] args = new Object[0];

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, null, args, null);

        assertEquals("SmartNull returned by unstubbed method() method on mock", result);
    }

    @Test
    public void testThrowingInterceptorInterceptWithToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Object[] args = new Object[0];

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, toStringMethod, args, null);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
}