import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up the mock to return null as the default return value
        when(invocation.getMethod().getReturnType()).thenReturn(null);
        
        // Test that the answer method returns null
        assertNull(returnsSmartNulls.answer(invocation));
        
        // Set up the mock to return a mockable type as the default return value
        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        
        // Test that the answer method returns a mocked object
        assertNotNull(returnsSmartNulls.answer(invocation));
    }
}