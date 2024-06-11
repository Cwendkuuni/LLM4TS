import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }
    
    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = rs.new ThrowingInterceptor(mock(InvocationOnMock.class));
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(throwingInterceptor.intercept(any(), any(), any(), eq(methodProxy))).thenThrow(Exception.class);

        try {
            throwingInterceptor.intercept(null, null, null, methodProxy);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }
}