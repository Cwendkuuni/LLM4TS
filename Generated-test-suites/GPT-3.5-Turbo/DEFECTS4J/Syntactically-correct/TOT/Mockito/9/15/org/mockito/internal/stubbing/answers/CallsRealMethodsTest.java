import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();

        // Test typical use case
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);
        assertNull(result); // The method should return null for void methods

        // Test edge case
        assertNull(answer.answer(null)); // The method should handle null invocations gracefully

        // Test error scenario
        invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(new RuntimeException());
        try {
            answer.answer(invocation);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}