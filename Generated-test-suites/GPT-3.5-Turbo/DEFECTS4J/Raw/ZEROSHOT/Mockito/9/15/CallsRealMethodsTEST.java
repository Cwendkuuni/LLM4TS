import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callsRealMethods.answer(invocation);
        // Add assertions here to test the behavior of answer() method
    }

}