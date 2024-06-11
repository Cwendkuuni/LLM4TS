import static org.mockito.Mockito.*;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        Object result = instance.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testThrowingInterceptor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        MethodInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        MethodProxy proxy = mock(MethodProxy.class);
        Method method = mock(Method.class);
        Object[] args = new Object[0];

        interceptor.intercept(null, method, args, proxy);

        Assert.assertThatThrows(NullPointerException.class, () -> {});
    }

    @Test
    public void testNonNullReturnValue() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(new Method(() -> "Test"));
        Object result = instance.answer(invocation);

        assertEquals("Test", result);
    }

    @Test
    public void testThrowingException() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenThrow(new RuntimeException("Test Exception"));
        try {
            instance.answer(invocation);
        } catch (Throwable e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testReturningSmartNull() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(String.class);
        when(invocation.getMethod()).thenReturn(new Method(mock(Method.class)) {
            @Override
            public Class<?> getReturnType() {
                return String.class;
            }
        });

        Object result = instance.answer(invocation);

        assertInstanceOf(SmartNull.class, result);
    }

    @Test
    public void testNotReturningSmartNullForFinalOrNonMockableReturnTypes() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Long.class);

        Object result = instance.answer(invocation);

        assertNull(result);
    }
}