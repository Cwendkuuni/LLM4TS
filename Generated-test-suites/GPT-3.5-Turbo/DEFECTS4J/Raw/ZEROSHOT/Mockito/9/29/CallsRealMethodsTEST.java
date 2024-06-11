import org.junit.Test;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Create a mock object
        CallsRealMethods mock = mock(CallsRealMethods.class, new CallsRealMethods());

        // Stub a method
        when(mock.answer(any(InvocationOnMock.class))).thenCallRealMethod();

        // Verify the method is called and returns the expected result
        Object result = mock.answer(null);
        assert(result != null);
    }
}