package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsDefaultValue() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")));

        Object defaultValue = new Object();
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(defaultValue, result);

        verify(invocation).getArguments();
        verify(invocation).getMethod();
    }

    @Test
    public void testReturnsSmartNullForToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")));

        String smartNull = returnsSmartNulls.answer(invocation).toString();
        assertEquals("SmartNull returned by unstubbed toString method on mock", smartNull);
    }

    @Test(expected = MockitoException.class)
    public void testThrowsExceptionForToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")));

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
        ClassImposterizer.imposterise(throwingInterceptor, Object.class);

        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        assertNull(returnsSmartNulls.answer(invocation));

        verify(invocation).getArguments();
        verify(invocation).getMethod();
    }

    @Test
    public void testReturnsSmartNullForClassThatCanBeImposterised() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        ClassImposterizer classImposterizer = spy(ClassImposterizer.INSTANCE);
        when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);

        Object smartNull = returnsSmartNulls.answer(invocation);
        assertEquals(classImposterizer.imposterise(new ReturnsSmartNulls.ThrowingInterceptor(invocation), returnType), smartNull);

        verify(invocation).getArguments();
        verify(invocation).getMethod();
        verify(classImposterizer).imposterise(any(MethodInterceptor.class), any(Class.class));
    }

    @Test
    public void testReturnsNullForClassThatCannotBeImposterised() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);

        assertNull(returnsSmartNulls.answer(invocation));

        verify(invocation).getArguments();
        verify(invocation).getMethod();
        verify(ClassImposterizer.INSTANCE,Mockito.never()).imposterise(any(MethodInterceptor.class), any(Class.class));
    }

}