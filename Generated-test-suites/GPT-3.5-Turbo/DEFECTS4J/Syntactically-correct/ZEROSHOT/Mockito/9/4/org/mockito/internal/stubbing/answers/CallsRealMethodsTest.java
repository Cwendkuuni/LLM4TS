package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Test answer method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        
        // Verify that the answer method returns the result of invocation.callRealMethod()
        assertNotNull(result);
        verify(invocation).callRealMethod();
    }
}