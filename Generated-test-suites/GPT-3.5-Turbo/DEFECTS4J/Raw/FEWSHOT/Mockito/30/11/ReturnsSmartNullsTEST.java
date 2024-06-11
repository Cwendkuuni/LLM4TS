package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        // Test when defaultReturnValue is not null
        Answer<Object> delegate = new ReturnsMoreEmptyValues();
        Mockito.when(delegate.answer(invocation)).thenReturn("defaultReturnValue");
        assertEquals("defaultReturnValue", returnsSmartNulls.answer(invocation));
        
        // Test when defaultReturnValue is null and ClassImposterizer.canImposterise is true
        Class<?> type1 = String.class;
        ClassImposterizer.INSTANCE = new ClassImposterizer();
        Mockito.when(invocation.getMethod()).thenReturn(type1.getMethod("toString"));
        assertEquals("SmartNull returned by unstubbed toString method on mock", returnsSmartNulls.answer(invocation));
        
        // Test when defaultReturnValue is null and ClassImposterizer.canImposterise is false
        Class<?> type2 = Object.class;
        Mockito.when(invocation.getMethod()).thenReturn(type2.getMethod("toString"));
        assertNull(returnsSmartNulls.answer(invocation));
    }
    
    @Test
    public void testThrowingInterceptor() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        
        // Test when isToString returns true
        MethodProxy methodProxy1 = Mockito.mock(MethodProxy.class);
        Mockito.when(new ObjectMethodsGuru().isToString(methodProxy1)).thenReturn(true);
        assertEquals("SmartNull returned by unstubbed toString method on mock", throwingInterceptor.intercept(null, methodProxy1, null, null));
        
        // Test when isToString returns false
        MethodProxy methodProxy2 = Mockito.mock(MethodProxy.class);
        Mockito.when(new ObjectMethodsGuru().isToString(methodProxy2)).thenReturn(false);
        Mockito.doNothing().when(new Reporter()).smartNullPointerException(new Location());
        assertNull(throwingInterceptor.intercept(null, methodProxy2, null, null));
    }
    
    @Test
    public void testFormatMethodCall() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Object[] args1 = {"arg1"};
        Mockito.when(invocation.getArguments()).thenReturn(args1);
        assertEquals("toString(arg1)", returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall());
        
        Object[] args2 = {"arg1", "arg2"};
        Mockito.when(invocation.getArguments()).thenReturn(args2);
        assertEquals("toString(arg1, arg2)", returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall());
    }
}