import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;


public class CallsRealMethodsTest {

    @Test
    public void testAnswer() {
        // Create test objects
        
        // Implement test logic
        
        // Assert results
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInvocation() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        callsRealMethods.answer(null);
    }
    
    @Test(expected = Exception.class)
    public void testAnswerWithException() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.callRealMethod()).thenThrow(Exception.class);
        
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        callsRealMethods.answer(invocation);
    }
}