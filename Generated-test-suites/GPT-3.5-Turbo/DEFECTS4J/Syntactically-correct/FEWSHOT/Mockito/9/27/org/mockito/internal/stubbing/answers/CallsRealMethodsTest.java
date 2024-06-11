package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();

        // Test when invocation callRealMethod() returns an Integer
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.callRealMethod()).thenReturn(10);
        Object result1 = answer.answer(invocation1);
        assertEquals(10, result1);

        // Test when invocation callRealMethod() returns a String
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.callRealMethod()).thenReturn("hello");
        Object result2 = answer.answer(invocation2);
        assertEquals("hello", result2);

        // Test when invocation callRealMethod() throws an exception
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        when(invocation3.callRealMethod()).thenThrow(new NullPointerException());
        try {
            answer.answer(invocation3);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // exception is expected
        }
    }
}