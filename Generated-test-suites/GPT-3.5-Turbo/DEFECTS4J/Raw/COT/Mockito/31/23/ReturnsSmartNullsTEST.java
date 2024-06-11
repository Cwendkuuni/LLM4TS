import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    @Test
    public void testAnswerDefaultReturnValueIsNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerTypeNotMockable() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerMethodIsToString() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
}