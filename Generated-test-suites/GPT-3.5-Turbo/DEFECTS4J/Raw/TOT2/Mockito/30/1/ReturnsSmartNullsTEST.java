import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerWithReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Answer<Object> delegate = invocation -> "mockedReturnValue";

        // Set a mock delegate that returns a value
        returnsSmartNulls.delegate = delegate;

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("mockedReturnValue", result);
    }

    @Test
    public void testAnswerWithFinalReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Set final return type
        invocation.getMethod().setReturnType(String.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerWithToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Set method to toString
        invocation.getMethod().setName("toString");

        Object result = returnsSmartNulls.answer(invocation);

        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
}