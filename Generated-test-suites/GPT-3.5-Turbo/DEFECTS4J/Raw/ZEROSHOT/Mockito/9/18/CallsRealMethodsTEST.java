import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
    }

}