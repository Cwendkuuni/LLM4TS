import org.junit.Before;
import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.Reporter;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_ReturnsSmartNulls() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        
        when(delegate.answer(invocation)).thenReturn(null);
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        verify(delegate).answer(invocation);
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void testAnswer_ReturnsDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        
        when(delegate.answer(invocation)).thenReturn("mock");
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("mock", result);
        verify(delegate).answer(invocation);
        verifyNoMoreInteractions(delegate);
    }

    @Test(expected = Reporter.class)
    public void testAnswer_ThrowsSmartNullPointerException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenThrow(new RuntimeException());
        
        returnsSmartNulls.answer(invocation);
    }
}