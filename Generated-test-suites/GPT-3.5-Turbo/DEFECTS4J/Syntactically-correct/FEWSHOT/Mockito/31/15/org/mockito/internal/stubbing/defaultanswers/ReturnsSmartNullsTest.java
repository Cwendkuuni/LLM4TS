package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy methodProxy = mock(MethodProxy.class);
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("isToString", MethodProxy.class));
        when(invocation.getMockCreationSettings()).thenReturn(settings);
        when(settings.getTypeToMock()).thenReturn(MethodProxy.class);

        // Test with ordinary return values
        Answer delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn("result");
        answer.delegate = delegate;
        assertEquals("result", answer.answer(invocation));

        // Test with mockable type
        delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        answer.delegate = delegate;
        assertEquals("SmartNull returned by unstubbed isToString() method on mock", answer.answer(invocation));

        // Test with non-mockable type
        delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        answer.delegate = delegate;
        when(invocation.getMockCreationSettings()).thenReturn(null);
        assertNull(answer.answer(invocation));
    }
}