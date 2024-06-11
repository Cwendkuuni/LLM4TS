
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.*;
import org.junit.Test;

public class CallsRealMethodsTest {
    
    @Test
    public void testAnswer() {
        CallsRealMethods answer = new CallsRealMethods();
        
        // Test case 1: Invocation with return type String
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.callRealMethod()).thenReturn("test");
        String result1 = answer.answer(invocation1);
        assertEquals("test", result1);
        
        // Test case 2: Invocation with return type int
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.callRealMethod()).thenReturn(5);
        int result2 = answer.answer(invocation2);
        assertEquals(5, result2);
        
        // Test case 3: Invocation with return type Object
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        when(invocation3.callRealMethod()).thenReturn(new Object());
        Object result3 = answer.answer(invocation3);
        assertNotNull(result3);
    }
}
