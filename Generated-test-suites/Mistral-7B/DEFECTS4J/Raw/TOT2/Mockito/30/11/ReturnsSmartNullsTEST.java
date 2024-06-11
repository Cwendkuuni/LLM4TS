
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.SmartNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocation);
        verify(mock).answer(invocation); // testing if the answer method was called

        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNonnullValue() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object expectedValue = new Object();
        Mockito.when(invocation.getMethod().getName()).thenReturn("testMethod");
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        Mockito.when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(expectedValue);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertEquals(expectedValue, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerMethodWithException() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(int[].class);

        try {
            returnsSmartNulls.answer(invocation);
        } catch (Throwable e) {
            throw new IllegalStateException("Unexpected exception thrown", e);
        }

        fail("Exception not thrown");
    }

    @Test
    public void testAnswerMethodWithSmartNull() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof org.mockito.internal.util.SmartNull);
    }

    @Test
    public void testFormatMethodCall() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        String methodName = "getSomething";
        Object[] args = new Object[]{1, 2, 3};

        String expected = "getSomething(1, 2, 3)";

        Mockito.when(invocation.getMethod().getName()).thenReturn(methodName);
        Mockito.when(invocation.getArguments()).thenReturn(args);

        String result = returnsSmartNulls.formatMethodCall();

        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowingInterceptor_toStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);

        try {
            String result = throwingInterceptor.intercept(mock, null, null, null);
        } catch (Throwable e) {
            throw new IllegalStateException("Unexpected exception thrown", e);
        }

        fail("Exception not thrown");
    }

    @Test
    public void testThrowingInterceptor_nonToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);

        try {
            throwingInterceptor.intercept(mock, Mockito.mock(Method.class), new Object[]{1}, Mockito.mock(MethodProxy.class));
        } catch (Throwable e) {
            verify(mock, ArgumentMatchers.any()).answer(ArgumentMatchers.any(InvocationOnMock.class));
        }
    }
}