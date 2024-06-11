
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        Object result = callsRealMethods.answer(invocation);
        
        verify(invocation, times(1)).callRealMethod();
    }
}
