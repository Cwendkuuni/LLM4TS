package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(Object.class);
        Method method = Object.class.getMethod("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setTarget(mock);

        when(returnsSmartNulls.answer(invocation)).thenReturn(null);

        assertNull(returnsSmartNulls.answer(invocation));
        verify(mock).toString();
    }

    @Test
    public void testAnswerReturnsInstanceOfMock() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);
        Method method = String.class.getMethod("value");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setTarget(mock);

        when(returnsSmartNulls.answer(invocation)).thenReturn(mock);

        assertEquals(mock, returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswerReturnsInstanceOfImposterisedClass() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);
        ClassImposterizer classImposterizer = spy(ClassImposterizer.class);
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        Method method = String.class.getMethod("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);
        invocation.setTarget(mock);

        when(classImposterizer.canImposterise(String.class)).thenReturn(true);
        when(classImposterizer.imposterise(interceptor, String.class)).thenReturn(mock);
        when(returnsSmartNulls.delegate).thenReturn(new ReturnsMoreEmptyValues());
        when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();

        assertEquals(mock, returnsSmartNulls.answer(invocation));
        verify(classImposterizer).imposterise(interceptor, String.class);
    }

    @Test
    public void testAnswerReturnsSmartNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Method method = Object.class.getMethod("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(method);

        assertEquals("SmartNull returned by unstubbed toString method on mock", returnsSmartNulls.answer(invocation));
    }
}