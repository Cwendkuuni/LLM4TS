import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    CallsRealMethods callsRealMethods = new CallsRealMethods();

    @Test
    public void testAnswer_callsRealMethod() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Object result = callsRealMethods.answer(invocation);
        
        assertTrue(result instanceof Object);
        // additional assertions can be added here
    }
}