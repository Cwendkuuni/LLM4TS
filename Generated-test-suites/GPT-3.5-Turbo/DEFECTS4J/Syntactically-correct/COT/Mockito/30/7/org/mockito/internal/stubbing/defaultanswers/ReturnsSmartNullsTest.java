import static org.mockito.Mockito.*;
import org.junit.*;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls smartNulls;

    @Before
    public void setUp() {
        smartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_nonNull() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(new Object());
        
        Object result = smartNulls.answer(invocation);
        
        assertNotNull(result);
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(delegate, times(1)).answer(invocation);
    }

    @Test
    public void testAnswer_nullMockable() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = smartNulls.answer(invocation);
        
        assertNotNull(result);
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(delegate, times(1)).answer(invocation);
    }

    @Test
    public void testAnswer_nullNotMockable() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = smartNulls.answer(invocation);
        
        assertNull(result);
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(delegate, times(1)).answer(invocation);
    }

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        Object result = smartNulls.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result.toString().contains("SmartNull returned by unstubbed testMethod() method on mock"));
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(delegate, times(1)).answer(invocation);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_smartNullPointerException() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
        
        smartNulls.answer(invocation);
    }
}



// JUnit 4 test cases goes here

