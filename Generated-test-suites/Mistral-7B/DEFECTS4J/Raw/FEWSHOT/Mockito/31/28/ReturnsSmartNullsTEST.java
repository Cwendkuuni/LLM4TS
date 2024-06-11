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
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultAnswerIfMethodIsNotToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("mock");

        String result = (String) returnsSmartNulls.answer(new InvocationOnMock(mock, new Method(String.class.getDeclaredMethod("toString")){}));
        assertEquals("mock", result);
    }

    @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
    public void testAnswer_ThrowsSmartNullPointerExceptionIfMethodIsToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, new Method(String.class.getDeclaredMethod("toString")){});

        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testAnswer_ReturnsNullIfTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);

        when(mock.toString()).thenReturn("mock");

        assertNull(returnsSmartNulls.answer(new InvocationOnMock(mock, new Method(Integer.class.getTypeName().equals("int") ? Integer.class.getDeclaredMethod("valueOf", String.class) : new Method(String.class.getDeclaredMethod("toUpperCase")){}))));
    }

    @Test
    public void testAnswer_ReturnsSmartNullIfTypeIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);

        when(mock.toString()).thenReturn("mock");

        String smartNull = (String) returnsSmartNulls.answer(new InvocationOnMock(mock, new Method(String[].class.getDeclaredMethod("new", int.class, int.class))));
        assertEquals("SmartNull returned by unstubbed new[] String(int, int) method on mock", smartNull);
    }

    @Test
    public void testAnswer_ReturnsDelegateAnswerIfNotNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("mock");
        Answer<Object> delegateAnswer = mock(Answer.class);
        when(delegateAnswer.answer(new InvocationOnMock(mock, new Method(String.class.getDeclaredMethod("toCharArray")){}))).thenReturn("delegateAnswer");
        ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
        spyReturnsSmartNulls.delegate = delegateAnswer;

        String result = (String) spyReturnsSmartNulls.answer(new InvocationOnMock(mock, new Method(String.class.getDeclaredMethod("toCharArray")){}));
        assertEquals("delegateAnswer", result);
    }
}