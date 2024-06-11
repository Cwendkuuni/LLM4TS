import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        // Check that the answer invokes the callRealMethod on the invocation
        assertEquals(invocation.callRealMethod(), result);
    }
}