package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testNullReturn() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class));
        assertNull(result);
    }

    @Test
    public void testReturnsSmartNullForString() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("SmartNull");
        String result = (String) returnsSmartNulls.answer(createInvocationOnMock("testMethod", "arg1"));
        assertEquals("SmartNull returned by unstubbed testMethod method on mock", result);
    }

    @Test
    public void testReturnsSmartNullForInteger() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(Integer.MIN_VALUE);
        Integer result = (Integer) returnsSmartNulls.answer(createInvocationOnMock("testMethod", "arg1"));
        assertEquals("SmartNull returned by unstubbed testMethod method on mock", result.toString());
    }

    @Test
    public void testReturnsSmartNullForCustomClass() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        CustomClass customClass = mock(CustomClass.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(customClass);
        when(customClass.toString()).thenReturn("SmartNullCustomClass");
        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", "arg1"));
        assertEquals("SmartNullCustomClass", result.toString());
    }

    @Test
    public void testDoesNotReturnSmartNullForPrimitiveTypes() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn((byte) 0);
        Byte result = (Byte) returnsSmartNulls.answer(createInvocationOnMock("testMethod", "arg1"));
        assertNull(result);
    }

    @Test
    public void testDoesNotReturnSmartNullForFinalClasses() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(String.class);
        Class<?> result = (Class<?>) returnsSmartNulls.answer(createInvocationOnMock("testMethod", "arg1"));
        assertNull(result);
    }

    @Test
    public void testThrowsExceptionWhenToStringMethodIsCalled() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        try {
            returnsSmartNulls.answer(createInvocationOnMock("toString", null));
            fail("Expected Reporter.smartNullPointerException to be thrown");
        } catch (Throwable e) {
            // Expected
        }
    }

    private InvocationOnMock createInvocationOnMock(String methodName, Object argument) {
        Method method = getClass().getMethod(methodName, argument.getClass());
        return new InvocationOnMock(method, new Object[]{argument});
    }

    private static class CustomClass {
        @Override
        public String toString() {
            return "SmartNullCustomClass";
        }
    }
}