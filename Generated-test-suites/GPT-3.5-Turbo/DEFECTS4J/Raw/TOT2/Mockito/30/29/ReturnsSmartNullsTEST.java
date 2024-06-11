import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }

    @Test
    public void testAnswer_ReturnsSmartNulls_NullReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals(null, result);
    }

    @Test
    public void testAnswer_ReturnsSmartNulls_ThrowsNullPointerException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals(null, result);
    }
}