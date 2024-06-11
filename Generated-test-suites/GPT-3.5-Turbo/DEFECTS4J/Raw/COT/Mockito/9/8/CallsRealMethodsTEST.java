
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        // Verify that the callRealMethod() was called
        verify(invocation).callRealMethod();
        // Check that the returned object is the same as the result of callRealMethod()
        assertEquals(result, invocation.callRealMethod());
    }
}
