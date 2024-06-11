import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_DefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsSmartNulls;
        Object result = answer.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_Imposterise() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof ThrowingInterceptor);
    }
}