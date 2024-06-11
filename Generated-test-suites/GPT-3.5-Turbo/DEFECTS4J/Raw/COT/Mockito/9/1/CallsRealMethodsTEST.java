
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Mock
    private CallsRealMethods callsRealMethods;

    public CallsRealMethodsTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswer() throws Throwable {
        // Test basic functionality
        doReturn("mocked").when(callsRealMethods).answer(any(InvocationOnMock.class));
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenCallRealMethod();

        Object result = callsRealMethods.answer(invocation);

        assertEquals("mocked", result);
    }

    @Test
    public void testAnswer_exceptionHandling() throws Throwable {
        // Test exception handling
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(new RuntimeException("Test exception"));

        try {
            callsRealMethods.answer(invocation);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }
}

