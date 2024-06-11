package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test case with null invocation
        assertThrows(NullPointerException.class, () -> {
            answer.answer(null);
        });
        
        // Test case with valid invocation
        when(invocation.callRealMethod()).thenReturn("foo");
        assertEquals("foo", answer.answer(invocation));
    }

}