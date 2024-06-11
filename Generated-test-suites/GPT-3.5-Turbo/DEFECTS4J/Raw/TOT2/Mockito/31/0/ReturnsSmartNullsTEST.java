import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Typical use case
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);

        // Edge case: Default return value is not null
        when(delegate.answer(invocation)).thenReturn("Mockito");
        result = returnsSmartNulls.answer(invocation);
        assertEquals("Mockito", result);

        // Edge case: Return type not mockable
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        result = returnsSmartNulls.answer(invocation);
        assertNull(result);

        // Error scenario: Exception thrown by interceptor
        when(invocation.getMethod()).thenThrow(new RuntimeException());
        try {
            result = returnsSmartNulls.answer(invocation);
            fail("Expected exception");
        } catch (Throwable e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}