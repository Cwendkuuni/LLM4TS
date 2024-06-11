import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockitoAnnotations;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expectedResult = new Object();

        when(invocation.callRealMethod()).thenReturn(expectedResult);

        Object result = callsRealMethods.answer(invocation);

        assertEquals(expectedResult, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerThrowsThrowable() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        doThrow(new Throwable()).when(invocation).callRealMethod();

        callsRealMethods.answer(invocation);
    }
}