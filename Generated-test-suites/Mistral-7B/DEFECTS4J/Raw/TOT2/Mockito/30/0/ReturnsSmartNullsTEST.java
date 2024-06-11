package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertNull(result);
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Object.class, "test", new Class[0]));
        when(invocationMock.getArguments()).thenReturn(new Object[0]);

        String result = returnsSmartNulls.formatMethodCall();
        String expected = "test()";
        assertEquals(expected, result);
    }

    @Test(expected = org.mockito.exceptions.MismatchedInvocationTypeException.class)
    public void testInterceptorMethodWithMismatchedTypes() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object obj = new Object();
        Method method = new Method(Void.TYPE, "test", new Class[0]);
        Object[] args = new Object[0];
        MethodProxy methodProxy = mock(MethodProxy.class);

        returnsSmartNulls.intercept(obj, method, args, methodProxy);
    }

    @Test
    public void testReporterSmartNullPointerException() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Object.class, "test", new Class[0]));

        try {
            returnsSmartNulls.answer(invocationMock);
            fail("Expected smart null pointer exception");
        } catch (org.mockito.exceptions.SmartNullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testAnswerMethodWithReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Integer.TYPE, "test", new Class[0]));
        when(invocationMock.getArguments()).thenReturn(new Object[0]);

        when(returnsSmartNulls.delegate.answer(invocationMock)).thenReturn(42);

        int result = ((Integer) returnsSmartNulls.answer(invocationMock)).intValue();
        assertEquals(42, result);
    }

    @Test
    public void testAnswerMethodWithMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(String[].class, "test", new Class[0]));
        when(invocationMock.getArguments()).thenReturn(new Object[0]);

        String[] result = (String[]) returnsSmartNulls.answer(invocationMock);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testAnswerMethodWithNonMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Integer.TYPE, "test", new Class[0]));
        when(invocationMock.getArguments()).thenReturn(new Object[0]);

        assertNull(returnsSmartNulls.answer(invocationMock));
    }
}