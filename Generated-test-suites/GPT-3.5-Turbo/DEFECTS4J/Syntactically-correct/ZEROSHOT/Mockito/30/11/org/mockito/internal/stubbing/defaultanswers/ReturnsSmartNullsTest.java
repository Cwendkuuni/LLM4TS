package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
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

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_forUnstubbedMethod_callToStringMethod_returnSmartNullMessage() throws Throwable {
        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_forUnstubbedMethod_callToStringMethod_returnSmartNullMessage");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[]{}, null);

        Assert.assertEquals("SmartNull returned by unstubbed testAnswer_forUnstubbedMethod_callToStringMethod_returnSmartNullMessage method on mock", result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_forUnstubbedMethod_throwSmartNullPointerException() throws Throwable {
        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_forUnstubbedMethod_throwSmartNullPointerException");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        throwingInterceptor.intercept(null, method, new Object[]{}, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_whenDefaultReturnValueIsNull_returnNull() throws Throwable {
        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        Assert.assertNull(result);
    }

    @Test
    public void testAnswer_whenDefaultReturnValueIsNotNull_returnDefaultReturnValue() throws Throwable {
        ReturnsMoreEmptyValues returnsMoreEmptyValues = Mockito.mock(ReturnsMoreEmptyValues.class);
        Mockito.when(returnsMoreEmptyValues.answer(Mockito.any(InvocationOnMock.class))).thenReturn("Default Return Value");

        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        Assert.assertEquals("Default Return Value", result);
    }

    @Test
    public void testAnswer_whenTypeIsMockable_returnMockObject() throws Throwable {
        ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(classImposterizer.canImposterise(Mockito.any(Class.class))).thenReturn(true);
        Mockito.when(classImposterizer.imposterise(Mockito.any(MethodInterceptor.class), Mockito.any(Class.class))).thenReturn(Mockito.mock(Object.class));

        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_whenTypeIsMockable_returnMockObject");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        Assert.assertNotNull(result);
    }

    @Test
    public void testAnswer_whenTypeIsNotMockable_returnNull() throws Throwable {
        ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(classImposterizer.canImposterise(Mockito.any(Class.class))).thenReturn(false);

        InvocationOnMock invocation = (InvocationOnMock) Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_whenTypeIsNotMockable_returnNull");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        Assert.assertNull(result);
    }

}