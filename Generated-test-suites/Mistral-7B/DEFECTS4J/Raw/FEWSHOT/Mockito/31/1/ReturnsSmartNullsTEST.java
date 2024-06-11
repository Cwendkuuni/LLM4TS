package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultValue() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        ObjectMock mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {});

        Object defaultReturnValue = returnsSmartNulls.answer(invocation);
        assertSame(defaultReturnValue, "SmartNull returned by unstubbed toString() method on mock");
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenReturnTypeIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("toString")) {});
        when(mock.toString()).thenReturn("Mock String");

        Object result = returnsSmartNulls.answer(invocation);
        assertSame(result, "Mock String");
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenReturnTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(Long.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(Long.class.getDeclaredMethod("value")) {});

        assertNull(returnsSmartNulls.answer(invocation));

        // Verify that smart null pointer exception was thrown
        verify(invocation.getMock(), Mockito.atLeastOnce()).smartNullPointerException(Mockito.any());
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenInvokedTwice() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        ObjectMock mock = mock(Integer.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(Integer.class.getDeclaredMethod("intValue")) {});

        Object result1 = returnsSmartNulls.answer(invocation);
        assertNull(result1);

        Object result2 = returnsSmartNulls.answer(invocation);
        assertSame(result2, result1);

        verify(invocation.getMock(), Mockito.atLeastOnce()).smartNullPointerException(Mockito.any());
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenMethodReturnsToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {});

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenMethodReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {
            @Override
            public Object invoke(Object obj, Object[] args) {
                return null;
            }
        });

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testThrowingInterceptor_ToStringMethodIsCalled() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("toString")) {});

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
        Object result = ClassImposterizer.INSTANCE.invokeMethod(throwingInterceptor, "toString");

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testThrowingInterceptor_NonToStringMethodIsCalled() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ObjectMock mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("length")) {});

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
        try {
            Object result = ClassImposterizer.INSTANCE.invokeMethod(throwingInterceptor, "toString");
            fail("Expected smart null pointer exception");
        } catch (Throwable e) {
            // Expected
        }
    }
}