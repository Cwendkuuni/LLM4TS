import org.junit.Test;
import org.mockito.Mockito;

public class CallsRealMethodsTest {
    @Test
    public void testCallRealMethod() {
        CallsRealMethods mock = Mockito.mock(CallsRealMethods.class);
        Object result = mock.answer(Mockito.mock(InvocationOnMock.class));
        assertNotNull(result);
    }

    @Test
    public void testCallRealMethodWithNullInvocationOnMock() {
        CallsRealMethods mock = Mockito.mock(CallsRealMethods.class);
        try {
            mock.answer(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCallRealMethodWithExceptions() {
        CallsRealMethods mock = Mockito.mock(CallsRealMethods.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        try {
            doThrow(new RuntimeException("Test Exception")).when(invocation).callRealMethod();
            mock.answer(invocation);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}
//