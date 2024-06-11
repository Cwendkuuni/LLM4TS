import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Test typical use case
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.callRealMethod()).thenReturn("Mockito");
        Object result1 = callsRealMethods.answer(invocation1);
        assertEquals("Mockito", result1);
        
        // Test edge case
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.callRealMethod()).thenReturn("JUnit");
        Object result2 = callsRealMethods.answer(invocation2);
        assertEquals("JUnit", result2);
        
        // Test error scenario
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        when(invocation3.callRealMethod()).thenThrow(new IllegalArgumentException("Invalid argument"));
        try {
            callsRealMethods.answer(invocation3);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid argument", e.getMessage());
        }
    }
}