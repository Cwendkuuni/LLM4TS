
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {
    
    @Test
    public void testAnswer() throws Throwable{
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = new InvocationOnMock(){};
        
        // Test the return value of the answer method
        assertEquals(invocation.callRealMethod(), callsRealMethods.answer(invocation));
    }
}
