package org.mockito.internal.stubbing.answers;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Given
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // When
        Object result = callsRealMethods.answer(invocation);

        // Then
        // TODO: Add assertions here
        
    }
}