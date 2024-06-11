package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import org.mockito.invocation.InvocationOnMock;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);
        String result = (String) returnsSmartNulls.answer(mock, new Method(String.class.getMethod("toString")), new Object[0]);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testAnswer_ReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);
        String result = (String) returnsSmartNulls.answer(mock, new Method(String.class.getMethod("valueOf", new Class[0])), new Object[0]);
        assertNull(result);
    }

    @Test
    public void testAnswer_ReturnsSmartNullForMethodWithToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        when(mock.toString()).thenReturn("SmartNull");
        Object result = returnsSmartNulls.answer(mock, new Method(Object.class.getMethod("toString")), new Object[0]);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsSmartNullForUnmockedMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Method method = Object.class.getMethod("hashCode");
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertEquals("SmartNull returned by unstubbed hashCode() method on mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsSmartNullForUnmockedStaticMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Method method = Object.class.getMethod("class", new Class[0]);
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertEquals("SmartNull returned by unstubbed getClass() method on mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsSmartNullForUnmockedMethodCallWithArgument() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Object argument = mock(String.class);
        Method method = Object.class.getMethod("length", new Class[] { String.class });
        Object result = returnsSmartNulls.answer(mock, method, new Object[] { argument });
        assertEquals("SmartNull returned by unstubbed length(java.lang.String) method on mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsSmartNullForUnmockedInterfaceMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(MyInterface.class, returnsSmartNulls);
        Method method = MyInterface.class.getMethod("methodWithReturnType");
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertEquals("SmartNull returned by unstubbed methodWithReturnType() method on MyInterface mock", result.toString());
    }

    @Test
    public void testAnswer_ReturnsNullForUnmockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(FinalClass.class, returnsSmartNulls);
        Method method = FinalClass.class.getMethod("finalMethod");
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertNull(result);
    }

    @Test
    public void testAnswer_ReturnsNullForUnmockableStaticMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Method method = Object.class.getMethod("valueOf", new Class[0]);
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertNull(result);
    }

    @Test
    public void testAnswer_ReturnsNullForUnmockablePrivateMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(MyPrivateClass.class, returnsSmartNulls);
        Method method = MyPrivateClass.class.getDeclaredMethod("privateMethod");
        method.setAccessible(true);
        Object result = returnsSmartNulls.answer(mock, method, new Object[0]);
        assertNull(result);
    }

    @Test
    public void testAnswer_ThrowsExceptionForMethodsWithoutToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Method method = Object.class.getMethod("equals", new Class[] { Object.class });
        try {
            returnsSmartNulls.answer(mock, method, new Object[0]);
            fail("expected Reporter.smartNullPointerException to be thrown");
        } catch (Throwable e) {
            verify(mock).toString();
        }
    }

    @Test
    public void testAnswer_DoesNotCallToStringForMethodsWithToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        when(mock.toString()).thenReturn("MockObject");
        Method method = Object.class.getMethod("equals", new Class[] { Object.class });
        ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
        doAnswer(invocation -> "DummyReturnValue").when(spyReturnsSmartNulls).answer(eq(mock), eq(method), anyArray());
        assertFalse(mock.toString() == returnsSmartNulls.answer(mock, method, new Object[0]));
        verifyZeroInteractions(mock);
    }
}