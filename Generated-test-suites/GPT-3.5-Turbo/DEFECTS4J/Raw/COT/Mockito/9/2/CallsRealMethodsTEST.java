import static org.mockito.Mockito.*;

import org.junit.Test;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test normal case
        when(invocation.callRealMethod()).thenReturn("result");
        Object result = callsRealMethods.answer(invocation);
        assertEquals("result", result);
        
        // Test exception handling
        when(invocation.callRealMethod()).thenThrow(new Exception("error"));
        try {
            callsRealMethods.answer(invocation);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("error", e.getMessage());
        }
        
        // Test edge cases
        // TODO: Add edge case tests
        
    }
}