import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_OrdinaryReturnValue() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn("mock answer");
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("mock answer", result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_NullValueType() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_NonMockableType() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(42);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(42, result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_NullDefaultReturnValue() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getDeclaringClass()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_NullDefaultReturnValue_WithArguments() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getDeclaringClass()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("equals");
        when(invocation.getArguments()).thenReturn(new Object[]{"test"});

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed equals(\"test\") method on mock", result);
        verify(delegate).answer(invocation);
    }

    @Test(expected = Reporter.smartNullPointerException.class)
    public void testAnswer_NullDefaultReturnValue_SmartNullPointerExceptionThrown() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getDeclaringClass()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("hashCode");
        when(invocation.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocation);

        verify(delegate).answer(invocation);
    }
}