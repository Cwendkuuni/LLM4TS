import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;

public class CallsRealMethodsTest {
    private CallsRealMethods callRealMethods;
    private InvocationOnMock invocationMock;

    @Before
    public void setup() {
        callRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswerMethod() {
        Object expected = new Object();
        invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getArguments()).thenReturn(new Object[0]);
        Object actual = callRealMethods.answer(invocationMock);
        assertEquals(expected, actual);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithThrowable() {
        Object expected = new Object();
        invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenThrow(new RuntimeException());
        try {
            callRealMethods.answer(invocationMock);
        } catch (Throwable e) {
            fail("Expected RuntimeException but got " + e.getClass().getName());
        }
    }
}