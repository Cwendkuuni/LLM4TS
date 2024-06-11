package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(mockInvocation);
        assertNull(result);
    }

    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        Answer<Object> delegate = Mockito.mock(Answer.class);
        returnsSmartNulls.delegate = delegate;

        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        Mockito.when(delegate.answer(mockInvocation)).thenReturn(defaultReturnValue);

        Object result = returnsSmartNulls.answer(mockInvocation);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = Mockito.mock(MethodProxy.class);

        Method method = String.class.getMethod("toString");
        Mockito.when(mockInvocation.getMethod()).thenReturn(method);
        Mockito.when(mockInvocation.getArguments()).thenReturn(new Object[]{});
        Mockito.when(mockMethodProxy.invokeSuper(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Test");

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mockInvocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[]{}, mockMethodProxy);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        Mockito.verifyZeroInteractions(mockMethodProxy);
    }

    @Test
    public void testThrowingInterceptorInterceptWithUnsupportedMethod() throws Throwable {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = Mockito.mock(MethodProxy.class);

        Method method = Object.class.getMethod("equals", Object.class);
        Mockito.when(mockInvocation.getMethod()).thenReturn(method);

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mockInvocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[]{}, mockMethodProxy);

        assertNull(result);
        Mockito.verifyZeroInteractions(mockMethodProxy);
    }

    @Test
    public void testThrowingInterceptorInterceptWithThrowingNullPointerException() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);

        try {
            throwingInterceptor.intercept(null, null, null, null);
        } catch (NullPointerException e) {
            assertEquals("smartNullPointerException", e.getMessage());
        }
    }

    @Test
    public void testThrowingInterceptorFormatMethodCall() throws Throwable {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = String.class.getMethod("length");
        Mockito.when(mockInvocation.getMethod()).thenReturn(method);
        Mockito.when(mockInvocation.getArguments()).thenReturn(new Object[]{});

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mockInvocation);
        String result = throwingInterceptor.formatMethodCall();

        assertEquals("length()", result);
    }
}