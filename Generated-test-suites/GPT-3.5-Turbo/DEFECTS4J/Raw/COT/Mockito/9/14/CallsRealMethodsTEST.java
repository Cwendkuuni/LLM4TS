import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test basic functionality
        when(invocation.callRealMethod()).thenReturn("Test");
        assertEquals("Test", callsRealMethods.answer(invocation));
        
        // Test edge case: null invocation
        invocation = null;
        assertNull(callsRealMethods.answer(invocation));
        
        // Test exception handling: throwing exception
        when(invocation.callRealMethod()).thenThrow(new RuntimeException("Exception"));
        try {
            callsRealMethods.answer(invocation);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Exception", e.getMessage());
        }
    }
}