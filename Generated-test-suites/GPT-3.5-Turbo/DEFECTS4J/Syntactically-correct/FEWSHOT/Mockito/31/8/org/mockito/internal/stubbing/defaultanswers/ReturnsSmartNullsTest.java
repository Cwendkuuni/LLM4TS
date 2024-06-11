
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("method"));
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testAnswerWithClassImposterizer() throws Throwable {
        ClassImposterizer mockImposterizer = mock(ClassImposterizer.class);
        when(mockImposterizer.canImposterise(TestClass.class)).thenReturn(true);
        ClassImposterizer.INSTANCE = mockImposterizer;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = mock(MethodProxy.class);
        when(mockMethodProxy.invokeSuper(null, null)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("method"));
        when(mockImposterizer.imposterise(Mockito.<MethodInterceptor>anyObject(), Mockito.eq(TestClass.class))).thenReturn(new TestClass());

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }

    @Test
    public void testAnswerWithClassImposterizerNotMockable() throws Throwable {
        ClassImposterizer mockImposterizer = mock(ClassImposterizer.class);
        when(mockImposterizer.canImposterise(TestClass.class)).thenReturn(false);
        ClassImposterizer.INSTANCE = mockImposterizer;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = mock(MethodProxy.class);
        when(mockMethodProxy.invokeSuper(null, null)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("method"));

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testThrowingInterceptorInterceptWithToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = mock(MethodProxy.class);
        when(mockMethodProxy.invokeSuper(null, null)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("toString"));
        ObjectMethodsGuru mockGuru = mock(ObjectMethodsGuru.class);
        when(mockGuru.isToString(TestClass.class.getMethod("toString"))).thenReturn(true);
        when(mockGuru.isToString(TestClass.class.getMethod("method"))).thenReturn(false);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        throwingInterceptor.intercept(null, TestClass.class.getMethod("toString"), null, mockMethodProxy);

        assertNotNull(throwingInterceptor);
        assertNotNull(throwingInterceptor.intercept(null, TestClass.class.getMethod("toString"), null, mockMethodProxy));
    }

    @Test(expected = Reporter.class)
    public void testThrowingInterceptorInterceptWithoutToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy mockMethodProxy = mock(MethodProxy.class);
        when(mockMethodProxy.invokeSuper(null, null)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("method"));
        ObjectMethodsGuru mockGuru = mock(ObjectMethodsGuru.class);
        when(mockGuru.isToString(TestClass.class.getMethod("toString"))).thenReturn(true);
        when(mockGuru.isToString(TestClass.class.getMethod("method"))).thenReturn(false);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        throwingInterceptor.intercept(null, TestClass.class.getMethod("method"), null, mockMethodProxy);

        assertNotNull(throwingInterceptor);
        assertNotNull(throwingInterceptor.intercept(null, TestClass.class.getMethod("method"), null, mockMethodProxy));
    }

    class TestClass {
        public void method() {
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
