package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);

        String result = (String) returnsSmartNulls.answer(mock, new Method(() -> ""));
        assertNull(result);

        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(mock).toString();
        verify(mock).throwing(exceptionCaptor.capture()).withCallRealMethod();
        Throwable thrown = exceptionCaptor.getValue();
        assertNotNull(thrown.getMessage().contains("SmartNull returned by unstubbed toString() method on mock"));
    }

    @Test
    public void testAnswer_notMockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);

        assertNull(returnsSmartNulls.answer(mock, new Method(() -> "")));
    }

    @Test
    public void testAnswer_returnValueNotNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);

        String returnValue = "Hello World";
        Answer<String> delegate = mock(Answer.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(returnValue);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        smartNulls.delegate = delegate;

        String result = (String) smartNulls.answer(mock, new Method(() -> ""));
        assertEquals(returnValue, result);
    }

    @Test
    public void testAnswer_methodReturnsString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(ClassWithStringMethod.class, returnsSmartNulls);

        String result = (String) returnsSmartNulls.answer(mock, new Method(ClassWithStringMethod.class.getMethod("getStringMethod")));
        assertNull(result);

        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(mock).getStringMethod();
        verify(mock).throwing(exceptionCaptor.capture()).withCallRealMethod();
        Throwable thrown = exceptionCaptor.getValue();
        assertNotNull(thrown.getMessage().contains("SmartNull returned by unstubbed getStringMethod() method on mockClassWithStringMethod"));
    }
}

class ClassWithStringMethod {
    public String getStringMethod() {
        return null;
    }
}