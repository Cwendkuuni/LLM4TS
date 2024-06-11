import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Create a mock object for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create an instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Set up the mock object to return a specific value
        when(invocation.callRealMethod()).thenReturn("mocked answer");
        
        // Call the answer method and assert the result
        Object result = callsRealMethods.answer(invocation);
        assertEquals("mocked answer", result);
    }
}