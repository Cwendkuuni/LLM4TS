import static org.mockito.Mockito.*;
import org.junit.Test;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Initialize
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test case for answer method
        Object result = answer.answer(invocation);
        
        // Verify
        verify(invocation).callRealMethod();
    }

}