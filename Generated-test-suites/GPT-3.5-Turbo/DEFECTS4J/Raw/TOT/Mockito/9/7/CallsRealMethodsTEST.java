import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods_answer() throws Throwable {
        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create an instance of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Verify that the answer method of CallsRealMethods calls the realMethod of InvocationOnMock
        Object result = callsRealMethods.answer(invocation);
        verify(invocation, times(1)).callRealMethod();
        assertNull(result);
    }
}