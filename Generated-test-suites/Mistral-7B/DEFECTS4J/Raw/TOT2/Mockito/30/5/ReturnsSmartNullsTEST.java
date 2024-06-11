import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.invocation.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Object mock;

    @BeforeAll
    public static void setupClass() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswerMethod() {
        mock = mock(String.class);
        when(mock.toString()).thenReturn("Test String");

        InvocationOnMock invocation = new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{mock});
        Object result = returnsSmartNulls.answer(invocation);

        verify(mock).toString();
        verifyZeroInteractions(returnsSmartNulls);
        assertNotNull(result);
        assertEquals("Test String", result);
    }

    @Test
    public void testToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        String result = returnsSmartNulls.toString();
        assertEquals("ReturnsSmartNulls@" + hashCode() + ", delegate=ReturnsMoreEmptyValues@" + hashCode(), result);
    }

    @Test
    public void testReturnsNullWhenReturnTypeIsNotMockable() {
        InvocationOnMock invocation = new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[0]);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testThrowsExceptionFromUnstubbedMethod() {
        InvocationOnMock invocation = new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[0], new RuntimeException("Test Exception"));
        assertThrows(RuntimeException.class, () -> returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testClassImposterizerFailure() {
        Class<Integer> integerClass = Integer.class;
        InvocationOnMock invocation = new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{integerClass});
        assertThrows(UnsupportedOperationException.class, () -> returnsSmartNulls.answer(invocation));
    }
}