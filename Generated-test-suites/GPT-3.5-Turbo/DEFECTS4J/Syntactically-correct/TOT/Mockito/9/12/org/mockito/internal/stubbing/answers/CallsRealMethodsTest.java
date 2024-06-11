import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void answer_InvokesRealImplementation_ReturnsRealMethodReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = new Object();
        when(invocation.callRealMethod()).thenReturn(returnValue);

        // Act
        Object actual = callsRealMethods.answer(invocation);

        // Assert
        verify(invocation).callRealMethod();
        assertEquals(returnValue, actual);
    }

    @Test(expected = Throwable.class)
    public void answer_WhenRealMethodThrowsException_ThrowsException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(new Throwable());

        // Act
        callsRealMethods.answer(invocation);

        // Assert
        verify(invocation).callRealMethod();
    }
}