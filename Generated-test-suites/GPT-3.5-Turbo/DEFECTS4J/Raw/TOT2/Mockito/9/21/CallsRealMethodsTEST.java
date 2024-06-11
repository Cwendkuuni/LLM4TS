import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Create a mock invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenReturn("Mockito");
        
        // Test the answer method
        Object result = callsRealMethods.answer(invocation);
        
        // Assert the result
        assertEquals("Mockito", result);
    }
}