import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test basic functionality
        when(invocation.callRealMethod()).thenReturn("result");
        Object result = answer.answer(invocation);
        assertEquals("result", result);
        
        // Test exception handling
        when(invocation.callRealMethod()).thenThrow(new IllegalArgumentException("Test exception"));
        try {
            answer.answer(invocation);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

}