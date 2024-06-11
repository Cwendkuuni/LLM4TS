package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();

        // Test case 1
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        Object result1 = answer.answer(invocation1);
        assertNull(result1);

        // Test case 2
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.callRealMethod()).thenReturn("Test Result");
        Object result2 = answer.answer(invocation2);
        assertEquals("Test Result", result2);
    }

}