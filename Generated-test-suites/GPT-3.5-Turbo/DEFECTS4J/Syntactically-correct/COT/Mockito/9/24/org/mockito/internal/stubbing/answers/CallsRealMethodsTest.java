import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Test case for answer() method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        
        // Assertion to check if the method is calling the real implementation
        verify(invocation, times(1)).callRealMethod();
    }
}