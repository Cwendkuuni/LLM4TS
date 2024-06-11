package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        // Test 1: Return value if invocation is a real method call
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        Object returnValue1 = answer.answer(invocation1);
        assertNull(returnValue1); // Since InvocationOnMock is a mock object, callsRealMethod() should return null
        
        // Test 2: Return value if invocation is not a real method call
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMethod()).thenReturn(null);
        Object returnValue2 = answer.answer(invocation2);
        assertNull(returnValue2); // Since the method is null, callsRealMethod() should return null
    }
}