import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;

public class CallsRealMethodsTest {

    @Mock
    private InvocationOnMock invocationMock;

    @Test
    public void testAnswerInvocation_shouldCallRealMethod() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        Object result = callsRealMethods.answer(invocationMock);

        // Assert that the real method was called and its result is returned
        assertEquals("Expected result", result);
    }

    @Test
    public void testAnswerInvocation_onMockitoInvocation_shouldCallRealMethod() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        Object result = callsRealMethods.answer(invocationMock);

        // Assert that the real method was called and its result is returned
        assertEquals("Expected result", result);
    }
}