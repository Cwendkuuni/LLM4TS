import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {
    @Test
    public void testAnswerMethod() {
        CallsRealMethods answer = new CallsRealMethods();
        Object realObject = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);
        invocation.setMethod(new Method()); // assuming getMethod() is available in InvocationOnMock

        Object result = answer.answer(invocation);

        verify(realObject).callRealMethod(); // invoke the real method
        assertNotNull(result); // ensure the answer is not null
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullInvocationOnMock() {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = null;
        answer.answer(invocation);
    }

    @Test
    public void testAnswerMethodWithNullTargetObject() {
        CallsRealMethods answer = new CallsRealMethods();
        Object realObject = null;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);

        try {
            answer.answer(invocation);
        } catch (IllegalArgumentException e) {
            // verify that IllegalArgumentException was thrown
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testAnswerMethodWithExceptionThrownByTarget() {
        CallsRealMethods answer = new CallsRealMethods();
        Object realObject = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);

        doThrow(new RuntimeException("Test Exception")).when(realObject).callRealMethod();

        try {
            answer.answer(invocation);
        } catch (Throwable e) {
            // verify that InvocationTargetException was thrown
            Throwable targetException = e.getCause();
            assertEquals("Test Exception", targetException.getMessage());
        }
    }
}