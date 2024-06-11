import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setup() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testAnswerCallsRealMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = callsRealMethods.answer(invocation);

        // Assert
        // The answer should call the real method
        assertEquals("Return value should be the result of the real method",
                invocation.callRealMethod(), result);
    }

    @Test
    public void testAnswerThrowsException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Exception expectedException = new Exception("Test exception");
        invocation.callRealMethod(); // Simulate real method throwing the exception

        // Act
        try {
            callsRealMethods.answer(invocation);
        } catch (Exception e) {
            // Assert
            // The answer should propagate the exception thrown by the real method
            assertEquals("Exception message should match",
                    expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testAnswerWithDifferentInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expectedResult = new Object();

        // Act
        Object result = callsRealMethods.answer(invocation);

        // Assert
        // The answer should return the result of the real method
        assertEquals("Return value should match the expected result",
                expectedResult, result);
    }

}