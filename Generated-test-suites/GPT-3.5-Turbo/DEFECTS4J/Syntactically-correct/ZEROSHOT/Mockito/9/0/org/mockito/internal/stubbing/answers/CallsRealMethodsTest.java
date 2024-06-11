import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer_callsRealMethod() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = answer.answer(invocation);
        
        verify(invocation, times(1)).callRealMethod();
        assertNotNull(result);
    }
}