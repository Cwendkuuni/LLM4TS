import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() {
        Object realObject = new Object() {
            int methodToCall() {
                return 42;
            }
        };

        CallsRealMethods answer = new CallsRealMethods();
        Object invocationTarget = mock(CallsRealMethods.class);

        when(invocationTarget.answer(any(InvocationOnMock.class))).thenAnswer(answer);

        int result = invocationTarget.methodToCall();

        // Verify that the real method was called and returned the correct value
        assertEquals(result, 42);
        verify(invocationTarget).callRealMethod();
    }

    // Edge case: Testing null InvocationOnMock
    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullInvocationOnMock() {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = null;

        answer.answer(invocation);
    }

    // Edge case: Testing exception handling
    @Test(expected = RuntimeException.class)
    public void testAnswerMethodWithException() {
        CallsRealMethods answer = new CallsRealMethods();
        Object invocationTarget = mock(CallsRealMethods.class);

        when(invocationTarget.answer(any(InvocationOnMock.class))).thenAnswer(answer);

        // Simulate an exception in the real method
        doThrow(new RuntimeException("Test exception")).when(invocationTarget).callRealMethod();

        invocationTarget.methodToCall();
    }

    // Edge case: Testing primitives and arrays
    @Test
    public void testAnswerMethodWithPrimitivesAndArrays() {
        Object realObject = new Object() {
            void methodToCall(int a, byte[] b) {
                System.out.println("Real method called with int: " + a + " and byte array: " + Arrays.toString(b));
            }
        };

        CallsRealMethods answer = new CallsRealMethods();
        Object invocationTarget = mock(CallsRealMethods.class);

        when(invocationTarget.answer(any(InvocationOnMock.class))).thenAnswer(answer);

        int a = 10;
        byte[] b = new byte[] { 1, 2, 3 };

        invocationTarget.methodToCall(a, b);

        // Verify that the real method was called
        verify(invocationTarget).callRealMethod();
    }
}