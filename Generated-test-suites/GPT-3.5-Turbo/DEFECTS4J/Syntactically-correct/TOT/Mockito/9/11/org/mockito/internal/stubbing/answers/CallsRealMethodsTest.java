import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Test the answer method for typical use case
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        assertEquals(invocation.callRealMethod(), result);

        // Test the answer method for edge case (null invocation)
        InvocationOnMock nullInvocation = null;
        Object nullResult = callsRealMethods.answer(nullInvocation);
        assertEquals(null, nullResult);
    }
}