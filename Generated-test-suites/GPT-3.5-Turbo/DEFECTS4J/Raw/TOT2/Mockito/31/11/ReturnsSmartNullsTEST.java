import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerWithSmartNulls() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(MethodInterceptor.class);

        Object result = smartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof MethodInterceptor);
    }

    @Test
    public void testAnswerWithNullReturn() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(null);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }

    @Test(expected = Exception.class)
    public void testAnswerException() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenThrow(new Exception());

        Object result = smartNulls.answer(invocation);
    }
}