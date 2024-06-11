import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer_withInvocation() throws Throwable {
        // Setup
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Run
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        Object result = callsRealMethods.answer(invocation);

        // Verify
        assertNull(result);
        verify(invocation).callRealMethod();
    }
    
    @Test
    public void testAnswer_withNullInvocation() throws Throwable {
        // Setup
        InvocationOnMock invocation = null;
        
        // Run
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        Object result = callsRealMethods.answer(invocation);

        // Verify
        assertNull(result);
    }
    
    @Test(expected = Throwable.class)
    public void testAnswer_withException() throws Throwable {
        // Setup
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        doThrow(new Throwable()).when(invocation).callRealMethod();
        
        // Run
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        callsRealMethods.answer(invocation);

        // Verify
        verify(invocation).callRealMethod();
    }
}