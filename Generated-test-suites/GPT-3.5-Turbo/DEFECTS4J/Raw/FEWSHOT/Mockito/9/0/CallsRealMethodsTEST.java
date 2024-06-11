
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        // Create a mock invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        // Setup stubbed method behavior
        Mockito.when(invocation.callRealMethod()).thenReturn("Mocked returned value");
        
        // Invoke the answer
        Object result = answer.answer(invocation);
        
        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("Mocked returned value", result);
    }
    
    @Test(expected = Throwable.class)
    public void testAnswerThrowsException() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        // Create a mock invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        // Setup stubbed method behavior
        Mockito.when(invocation.callRealMethod()).thenThrow(new Throwable("Mocked exception"));
        
        // Invoke the answer
        answer.answer(invocation);
    }
}
