package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenAnswer(new CallsRealMethods());

        // TODO: Add assertions for the return values of the real methods

        verify(invocation, times(1)).callRealMethod();
    }
}