import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setup() {
        callsRealMethods = Mockito.spy(new CallsRealMethods());
    }

    /* Test answer() method */
    @Test
    public void testAnswer() throws Throwable {

        // Set up mock object
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Set up expected result
        Object expectedResult = new Object();
        Mockito.when(invocation.callRealMethod()).thenReturn(expectedResult);

        // Call the answer method
        Object result = callsRealMethods.answer(invocation);

        // Verify that the real method on invocation is called
        Mockito.verify(invocation).callRealMethod();

        // Verify that the result is the expected result
        assertEquals(expectedResult, result);
    }

    /* Test answer() method with exception */
    @Test
    public void testAnswerWithException() throws Throwable {

        // Set up mock object
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Set up expected exception
        Throwable expectedException = new RuntimeException("Test exception");
        Mockito.when(invocation.callRealMethod()).thenThrow(expectedException);

        // Call the answer method
        try {
            callsRealMethods.answer(invocation);
            fail("Exception not thrown");
        } catch (Throwable throwable) {
            // Verify that the real method on invocation is called
            Mockito.verify(invocation).callRealMethod();
            // Verify that the thrown exception is the expected exception
            assertEquals(expectedException, throwable);
        }
    }

    /* Test answer() method with null invocation */
    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInvocation() throws Throwable {
        callsRealMethods.answer(null);
    }
}