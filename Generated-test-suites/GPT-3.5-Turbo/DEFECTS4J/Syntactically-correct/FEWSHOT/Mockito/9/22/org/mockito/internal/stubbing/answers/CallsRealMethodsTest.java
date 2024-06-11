
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {
    
    private CallsRealMethods callsRealMethods;
    
    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        // Test case 1: Verify that the invocation calls the real method
        Mockito.when(invocation.callRealMethod()).thenReturn("Return value");
        Object result1 = callsRealMethods.answer(invocation);
        assertEquals("Return value", result1);
        Mockito.verify(invocation).callRealMethod();
        
        // Test case 2: Verify that the invocation throws an exception if the real method throws an exception
        Mockito.when(invocation.callRealMethod()).thenThrow(new Exception("Exception message"));
        try {
            callsRealMethods.answer(invocation);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("Exception message", e.getMessage());
        }
        Mockito.verify(invocation).callRealMethod();
    }
}
