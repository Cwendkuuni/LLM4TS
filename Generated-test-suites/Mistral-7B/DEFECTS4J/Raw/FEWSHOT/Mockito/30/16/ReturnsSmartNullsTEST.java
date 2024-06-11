package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultValue() {
        ObjectMock mock = mock(Object.class, new ReturnsSmartNulls());
        Object defaultReturnValue = new Object();

        when(mock.getMethodWithDefaultValueReturn()).thenReturn(defaultReturnValue);

        Object result = mock.getMethodWithDefaultValueReturn();

        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForToStringCall() {
        ObjectMock mock = mock(Object.class, new ReturnsSmartNulls());

        String result = (String) mock.answer(new InvocationOnMock(mock, new MethodTestClass(), "toString", new Object[0]));

        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test
    public void testAnswer_ReturnsNull_ForNonMockableType() {
        ObjectMock mock = mock(Object.class, new ReturnsSmartNulls());

        assertNull(mock.answer(new InvocationOnMock(mock, String.class, "valueOf", new Object[]{"foo"})));
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForNonToStringCall() {
        ObjectMock mock = spy(new Object() {
            public void methodWithNullReturn() {}
        }, new ReturnsSmartNulls());

        doCallRealMethod().when(mock).methodWithNullReturn();

        try {
            mock.methodWithNullReturn();
        } catch (Throwable e) {
            String exceptionMessage = e.getMessage();
            assertEquals("SmartNull returned by unstubbed methodWithNullReturn method on mock", exceptionMessage);
        }
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForMethodWithArguments() {
        ObjectMock mock = mock(Object.class, new ReturnsSmartNulls());

        when(mock.getMethodWithArgumentsReturn()).thenCallRealMethod();

        Object[] arguments = new Object[]{1, 2, 3};
        Object result = mock.getMethodWithArgumentsReturn(arguments);

        assertEquals("SmartNull returned by unstubbed getMethodWithArgumentsReturn method on mock", result.getClass().getName());
        verify(mock).getMethodWithArgumentsReturn(arguments);
    }

    private static class ObjectMock {
        public Object getMethodWithDefaultValueReturn() {}
        public String getMethodWithArgumentsReturn() {
            return null;
        }

        public void methodWithNullReturn() {
            return;
        }
    }

    private static class MethodTestClass {
        public String toString() {
            return null;
        }
    }
}