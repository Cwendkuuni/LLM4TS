import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        CallsRealMethods answer = new CallsRealMethods();

        when(invocation.callRealMethod()).thenReturn("Hello World");

        Object result = answer.answer(invocation);

        verify(invocation).callRealMethod();
        assertEquals("Hello World", result);
    }

}