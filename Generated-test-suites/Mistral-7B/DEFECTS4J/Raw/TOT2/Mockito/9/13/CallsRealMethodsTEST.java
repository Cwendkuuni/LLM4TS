import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {
    @Test
    public void testAnswerMethodCallsRealMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = callRealMethods.answer(invocation);
        verify(target).callRealMethod();

        assertEquals(result, invocation.getMethod().invoke(target));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnswerMethodWithNullArgument() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = null;

        callRealMethods.answer(invocation);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerMethodWithInvalidInvocation() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(null);

        callRealMethods.answer(invocation);
    }

    @Test(expected = RuntimeException.class)
    public void testAnswerMethodWithThrownException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        doThrow(new RuntimeException("TestException")).when(target).callRealMethod();

        try {
            callRealMethods.answer(invocation);
            fail("Expected exception was not thrown");
        } catch (Throwable e) {
            assertEquals(e.getClass(), RuntimeException.class);
            assertEquals(e.getMessage(), "TestException");
        }
    }
}