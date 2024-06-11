import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_withMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof ThrowingInterceptor);
    }
    
    @Test
    public void testSmartNullMessage() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
        Object result = interceptor.intercept(null, ReturnsSmartNulls.class.getMethod("toString"), null, null);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = NullPointerException.class)
    public void testSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
        interceptor.intercept(null, ReturnsSmartNulls.class.getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class), null, null);
    }

}