
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
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
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMethod()).thenReturn(null);
        Object result1 = answer.answer(invocation1);
        Assert.assertNull(result1);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("isToString", Method.class));
        Object result2 = answer.answer(invocation2);
        Assert.assertEquals("SmartNull returned by unstubbed isToString() method on mock", result2);       
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        ThrowingInterceptor interceptor = answer.new ThrowingInterceptor(invocation);
        Object result = interceptor.intercept(null, null, null, null);
        Assert.assertNull(result);

        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("isToString", Method.class));
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.whenNew(Reporter.class).withNoArguments().thenReturn(reporter);
        Object result2 = interceptor.intercept(null, null, null, null);
        Assert.assertNull(result2);
        Mockito.verify(reporter).smartNullPointerException(location);
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        ThrowingInterceptor interceptor = answer.new ThrowingInterceptor(invocation);

        String result = interceptor.formatMethodCall();
        Assert.assertEquals("null()", result);

        Method method = ObjectMethodsGuru.class.getDeclaredMethod("isToString", Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        String result2 = interceptor.formatMethodCall();
        Assert.assertEquals("isToString()", result2);
    }
}
