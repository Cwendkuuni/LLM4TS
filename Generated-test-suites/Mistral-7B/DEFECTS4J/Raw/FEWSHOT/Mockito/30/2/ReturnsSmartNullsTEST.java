package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Object result = returnsSmartNulls.answer(mock(Object.class));
        assertNull(result);

        Object result2 = returnsSmartNulls.answer(mock(String.class));
        assertNull(result2);

        Object result3 = returnsSmartNulls.answer(mock(Number.class));
        assertNull(result3);

        Object result4 = returnsSmartNulls.answer(mock(List.class));
        assertNull(result4);

        Object result5 = returnsSmartNulls.answer(mock(Map.class));
        assertNull(result5);

        Object result6 = returnsSmartNulls.answer(mock(Class.class));
        assertNull(result6);

        Object result7 = returnsSmartNulls.answer(mock(Object[].class));
        assertNull(result7);

        Object result8 = returnsSmartNulls.answer(mock(Answer.class));
        assertNull(result8);

        Object result9 = returnsSmartNulls.answer(mock(Serializable.class));
        assertNull(result9);
    }

    @Test
    public void testToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn(null);

        String result = (String) returnsSmartNulls.answer(new InvocationOnMock(new Method(String.class.getDeclaredMethod("toString")), mock, new Object[0]));
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test
    public void testThrowingInterceptor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);

        try {
            returnsSmartNulls.answer(new InvocationOnMock(new Method(String.class.getDeclaredMethod("length")), mock, new Object[0]));
            fail();
        } catch (Throwable e) {
            // expected
        }

        verifyZeroInteractions(mock);
    }

    @Test
    public void testThrowingInterceptorToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);

        String result = (String) returnsSmartNulls.answer(new InvocationOnMock(new Method(String.class.getDeclaredMethod("toString")), mock, new Object[0]));
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);

        verifyZeroInteractions(mock);
    }

    @Test
    public void testDelegate() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("Delegate Return");
        returnsSmartNulls.delegate = delegate;

        Object result = returnsSmartNulls.answer(mock);
        assertEquals("Delegate Return", result);
    }
}