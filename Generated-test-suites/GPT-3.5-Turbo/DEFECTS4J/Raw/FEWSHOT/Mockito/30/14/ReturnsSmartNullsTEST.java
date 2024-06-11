package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        // Test case for default return value not null
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn("mockValue");
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertEquals("mockValue", result);

        // Test case for default return value null and mockable type
        delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(method.getReturnType()).thenReturn(String.class);
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertEquals(Mockito.CGLIB_PROXY_KEY, result.getClass().getName());

        // Test case for default return value null and non-mockable type
        delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(method.getReturnType()).thenReturn(Integer.class);
        result = answer.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        ObjectMethodsGuru guru = Mockito.mock(ObjectMethodsGuru.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        new Reporter().smartNullPointerException(Mockito.any(Location.class));
        
        // Test case for toString() method
        Mockito.when(guru.isToString(method)).thenReturn(true);
        Mockito.when(location.toString()).thenReturn("testLocation");
        Object result = answer.new ThrowingInterceptor(invocation).intercept(null, method, new Object[]{}, null);
        assertEquals("SmartNull returned by unstubbed method on mock", result);

        // Test case for non-toString() method
        Mockito.when(guru.isToString(method)).thenReturn(false);
        result = answer.new ThrowingInterceptor(invocation).intercept(null, method, new Object[]{}, null);
        assertNull(result);
    }
    
    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        Mockito.when(method.getName()).thenReturn("methodName");
        
        String result = answer.new ThrowingInterceptor(invocation).formatMethodCall();
        assertEquals("methodName(1, 2, 3)", result);
    }

}