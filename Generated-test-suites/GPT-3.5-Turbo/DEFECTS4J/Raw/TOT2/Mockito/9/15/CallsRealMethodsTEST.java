import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenReturn("Real Method Called");
        
        Object result = callsRealMethods.answer(invocation);
        
        assertEquals("Real Method Called", result);
    }
    
    @Test
    public void testAnswer_NullInvocation() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        Object result = callsRealMethods.answer(null);
        
        assertNull(result);
    }
  
    // Additional test cases for edge cases and error scenarios can be added here
    
}