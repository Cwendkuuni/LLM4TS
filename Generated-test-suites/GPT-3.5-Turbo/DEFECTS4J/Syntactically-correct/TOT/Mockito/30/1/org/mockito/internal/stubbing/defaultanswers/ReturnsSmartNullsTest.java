import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_NullValue() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result = answer.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_MockableType() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TestClass.class);
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }

    @Test
    public void testAnswer_ObjectToString() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[] {});
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test
    public void testAnswer_NullPointerException() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(invocation.getMethod().getName()).thenReturn("hashCode");
        when(invocation.getArguments()).thenReturn(new Object[] {});
        Object result = null;
        try {
            result = answer.answer(invocation);
        } catch (Exception ignored) {}
        assertNull(result);
    }

    class TestClass {
    }
}