import static org.mockito.Mockito.*;

import org.junit.Test;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Create instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        // Create mock InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Stub the callRealMethod method to return a specific value
        when(invocation.callRealMethod()).thenReturn("test");

        // Call the answer method of CallsRealMethods
        Object result = callsRealMethods.answer(invocation);

        // Verify that the expected value is returned
        assertEquals("test", result);
    }
}