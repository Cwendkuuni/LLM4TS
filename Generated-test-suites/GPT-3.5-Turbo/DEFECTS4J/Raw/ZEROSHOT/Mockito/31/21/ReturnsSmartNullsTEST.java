import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_WithDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn("defaultReturnValue");
        Object result = returnsSmartNulls.answer(invocation);
        verify(delegate).answer(invocation);
        assertEquals("defaultReturnValue", result);
    }

    @Test
    public void testAnswer_WithTypeMockable_ReturnsSmartNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Class<?> type = String.class;
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        Object result = returnsSmartNulls.answer(invocation);
        verify(delegate).answer(invocation);
        assertSame(type, result.getClass());
    }

    @Test
    public void testAnswer_WithTypeNotMockable_ReturnsNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Class<?> type = Object.class;
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        Object result = returnsSmartNulls.answer(invocation);
        verify(delegate).answer(invocation);
        assertNull(result);
    }
}