package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultValueIfMethodIsNotToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Method method = Object.class.getMethod("toString");

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setArguments(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_CallsToStringMethodAndReturnsSmartNullStringIfMethodIsToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);

        String expected = "SmartNull returned by unstubbed toString method on mock";
        String actual = "";

        Method method = String.class.getMethod("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setArguments(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);
        actual = result.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testAnswer_ReturnsNullIfReturnTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);

        Class<?> finalClass = Object.class;
        Method method = Object.class.getMethod("getClass");

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setArguments(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);

        verifyZeroInteractions(mock);
        verify(mock).getClass();
    }

    @Test
    public void testAnswer_ReturnsSmartNullInstanceIfReturnTypeIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);

        Class<?> mockableClass = mock(Object.class).getClass();
        Method method = mockableClass.getMethod("toString");

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setArguments(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);
        String actual = result.toString();

        String expected = "SmartNull returned by unstubbed toString method on mock";
        assertEquals(expected, actual);

        verifyZeroInteractions(mock);
        verify(mock).toString();
    }

    @Test
    public void testAnswer_CallsDelegateIfMethodHasADefaultValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);

        Method method = Object.class.getMethod("hashCode");
        Object defaultValue = new Integer(123);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setArguments(new Object[0]);

        when(mock.hashCode()).thenReturn(defaultValue);

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testAnswer_CallsDelegate_WhenReturnTypeIsNotMockableOrToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);

        Method method = Object.class.getMethod("equals", Object.class);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        Object argument = mock(Object.class);
        invocation.setArguments(new Object[]{argument});

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);

        verify(mock).equals(argument);
    }
}