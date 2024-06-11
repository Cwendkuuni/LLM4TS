import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_NullReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = answer.answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswer_DefaultReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testAnswer_Imposterise() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testAnswer_Imposterise_NonMockableType() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.TYPE);
        
        Object result = answer.answer(invocation);
        
        assertNull(result);
    }
}