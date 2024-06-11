import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithNotNullReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        Object result = answer.answer(invocation);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testAnswerWithMockableReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(ReturnsSmartNulls.class);
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof ReturnsSmartNulls);
    }
    
    @Test
    public void testAnswerWithNonMockableReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        Object result = answer.answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswerWithToStringMethod() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getName()).thenReturn("toString");
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
    
    @Test
    public void testAnswerWithNullReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(null);
        
        Object result = answer.answer(invocation);
        
        assertNull(result);
    }
}