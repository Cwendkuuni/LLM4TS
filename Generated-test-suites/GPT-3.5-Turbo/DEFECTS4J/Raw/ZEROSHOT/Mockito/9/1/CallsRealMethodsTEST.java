package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Setup
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expectedResult = new Object();
        when(invocation.callRealMethod()).thenReturn(expectedResult);

        // Execute
        Object result = callsRealMethods.answer(invocation);

        // Verify
        assertEquals(expectedResult, result);
    }
}