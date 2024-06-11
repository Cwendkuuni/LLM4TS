import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.jupiter.api.Assertions.*;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(returnsSmartNulls.answer(invocation)).thenReturn("Test Value");

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("Test Value", result);
    }

    @Test
    public void testAnswerNullDelegate() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(returnsSmartNulls.answer(invocation)).thenReturn(null);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerSmartNull() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(returnsSmartNulls.answer(invocation)).thenThrow(new RuntimeException());

        try {
            returnsSmartNulls.answer(invocation);
            fail("Expected 'SmartNull' exception");
        } catch (SmartNullException e) {
            // Expected exception
        }

        // Test getMessage() method of SmartNullException
        assertEquals("SmartNull returned by unstubbed testMethod() method on mock", e.getMessage());
    }

    @Test
    public void testAnswerToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.doThrow(new RuntimeException())
                .when(returnsSmartNulls)
                .toString();

        try {
            returnsSmartNulls.toString();
            fail("Expected 'SmartNullPointerException' exception");
        } catch (SmartNullPointerException e) {
            // Expected exception
        }

        // Test getMessage() method of SmartNullPointerException
        assertEquals("testToString() method on ReturnsSmartNulls class was called", e.getMessage());
    }

    @Test
    public void testAnswerFinalType() {
        final class FinalClass {}
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        try {
            Mockito.when(returnsSmartNulls.answer(invocation)).thenReturn(new FinalClass());
            returnsSmartNulls.answer(invocation);
            fail("Expected 'NullPointerException' exception");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}