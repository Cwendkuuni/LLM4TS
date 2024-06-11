import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ThrowsExceptionAnswer delegate = mock(ThrowsExceptionAnswer.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ObjectMethodsGuru.class.getMethod("isToString", Method.class);
        when(method.getName()).thenReturn("isToString");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = smartNulls.answer(invocation);

        assertNull(result);
        verify(delegate).answer(eq(invocation));
    }

    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        Object[] args = new Object[]{};
        Method method = ObjectMethodsGuru.class.getMethod("isToString", Method.class);
        MethodProxy.super.intercept(eq(invocation), eq(args), eq(proxy));

        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);

        Object result = interceptor.intercept(null, method, args, proxy);

        assertEquals("SmartNull returned by unstubbed isToString() method on mock", result);
        verify(invocation).getMethod();
    }

    @Test
    public void testThrowingInterceptorIntercept_NullMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        Object[] args = new Object[]{};

        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);

        Object result = interceptor.intercept(null, null, args, proxy);

        assertNull(result);
        verify(invocation, never()).getMethod();
    }

    @Test
    public void testThrowingInterceptorIntercept_ToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        Object[] args = new Object[]{};
        Method method = Object.class.getMethod("toString");
        MethodProxy.super.intercept(eq(invocation), eq(args), eq(proxy));

        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);

        Object result = interceptor.intercept(null, method, args, proxy);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        verify(invocation).getMethod();
    }

    @Test
    public void testReturnsSmartNulls() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("result");

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ObjectMethodsGuru.class.getMethod("isToString", Method.class);
        when(method.getName()).thenReturn("isToString");
        when(invocation.getMethod()).thenReturn(method);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
        verify(delegate).answer(eq(invocation));
    }

    @Test
    public void testReturnsSmartNulls_NullDefaultReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(null);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
        verify(delegate).answer(eq(invocation));
    }

    @Test
    public void testReturnsSmartNulls_NonMockableReturnType() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegate);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ObjectMethodsGuru.class.getMethod("isToString", Method.class);
        when(method.getReturnType()).thenReturn(String.class);
        when(method.getName()).thenReturn("isToString");
        when(invocation.getMethod()).thenReturn(method);
        when(ClassImposterizer.INSTANCE.canImposterise(eq(String.class))).thenReturn(false);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
        verify(delegate).answer(eq(invocation));
    }
}