import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerWithValidInvocation() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenCallRealMethod();

        Object result = callsRealMethods.answer(invocation);

        assertNotNull(result);
        // Perform additional assertions on the result if needed
    }

    @Test(expected = Exception.class)
    public void testAnswerWithException() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.callRealMethod()).thenThrow(Exception.class);

        callsRealMethods.answer(invocation);
        // Exception should be thrown, so the execution should not reach this point
        fail("Expected exception to be thrown");
    }

}