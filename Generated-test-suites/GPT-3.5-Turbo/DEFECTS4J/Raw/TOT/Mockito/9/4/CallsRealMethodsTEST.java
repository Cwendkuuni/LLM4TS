import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        callsRealMethods = new CallsRealMethods();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_validInvocation_invokesRealMethod() throws Throwable {
        // Arrange
        when(invocation.callRealMethod()).thenReturn("Expected Result");

        // Act
        Object result = callsRealMethods.answer(invocation);

        // Assert
        verify(invocation).callRealMethod();
        assertEquals("Expected Result", result);
    }

    @Test
    public void testAnswer_nullInvocation_throwsNullPointerException() throws Throwable {
        // Arrange
        invocation = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            callsRealMethods.answer(invocation);
        });
    }
}