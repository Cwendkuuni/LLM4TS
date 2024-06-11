package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object answer = returnsSmartNulls.answer(invocation);
        assertNull(answer);
        
        Object defaultReturnValue = delegate.answer(invocation);
        Mockito.when(defaultReturnValue).thenReturn("foo");
        answer = returnsSmartNulls.answer(invocation);
        assertEquals("foo", answer);
        
        ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE).thenReturn(imposterizer);
        Class<?> type = String.class;
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(type);
        MethodInterceptor interceptor = Mockito.mock(MethodInterceptor.class);
        Mockito.when(imposterizer.canImposterise(type)).thenReturn(true);
        Mockito.when(imposterizer.imposterise(interceptor, type)).thenReturn("bar");
        answer = returnsSmartNulls.answer(invocation);
        assertEquals("bar", answer);
    }
    
    @Test
    public void testThrowingInterceptor() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = 
            returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object obj = new Object();
        Method method = obj.getClass().getMethod("toString");
        Object[] args = new Object[0];
        MethodProxy proxy = Mockito.mock(MethodProxy.class);
        
        assertNull(throwingInterceptor.intercept(obj, method, args, proxy));
        
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(true);
        String expected = "SmartNull returned by unstubbed methodCall() method on mock";
        assertEquals(expected, throwingInterceptor.intercept(obj, method, args, proxy));
        
        new Reporter().smartNullPointerException(throwingInterceptor.location);
        assertNotNull(throwingInterceptor.intercept(obj, method, args, proxy));
    }
}