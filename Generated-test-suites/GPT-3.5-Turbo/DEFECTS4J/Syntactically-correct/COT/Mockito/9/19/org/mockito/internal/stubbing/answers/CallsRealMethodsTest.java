import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    // Test case for answer() method
    @Test
    public void testAnswerMethod() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Mock the callRealMethod() method to return a specific value
        when(invocation.callRealMethod()).thenReturn("Mocked Value");

        // Call the answer() method
        Object result = callsRealMethods.answer(invocation);

        // Verify that the callRealMethod() method was called
        verify(invocation, times(1)).callRealMethod();

        // Verify the result
        assertEquals("Mocked Value", result);
    }

    // TODO: Add edge case and exception testing
  
}