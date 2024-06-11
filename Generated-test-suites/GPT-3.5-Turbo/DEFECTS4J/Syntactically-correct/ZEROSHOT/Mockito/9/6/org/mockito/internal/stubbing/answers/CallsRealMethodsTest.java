import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        callsRealMethods = new CallsRealMethods();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer() throws Throwable {
        Object result = callsRealMethods.answer(invocation);

        assertNotNull(result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerThrowsException() throws Throwable {
        doThrow(new Throwable()).when(invocation).callRealMethod();

        callsRealMethods.answer(invocation);
    }

}