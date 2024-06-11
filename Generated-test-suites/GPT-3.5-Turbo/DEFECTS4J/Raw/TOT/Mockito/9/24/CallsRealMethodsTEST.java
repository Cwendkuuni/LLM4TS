import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswer_success() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = callsRealMethods.answer(invocation);
        
        // Assert
        assertNotNull(result);
    }

    @Test(expected = Throwable.class)
    public void testAnswer_throwsThrowable() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        doThrow(new Throwable()).when(invocation).callRealMethod();
        
        // Act
        callsRealMethods.answer(invocation);
        
        // Assert
        // Expects Throwable to be thrown
    }

    @Test
    public void testSerializable() {
        // Assert
        assertTrue(callsRealMethods instanceof java.io.Serializable);
    }
}