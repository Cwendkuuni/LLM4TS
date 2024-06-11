import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = callsRealMethods.answer(invocation);
        
        // Check that the real method is called
        assertNotNull(result);
    }
    
    @Test
    public void testAnswerWithNullInvocation() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Pass null invocation
        Object result = callsRealMethods.answer(null);
        
        // Check that null is returned when null invocation is passed
        assertNull(result);
    }
}