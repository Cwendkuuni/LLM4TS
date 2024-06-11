import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Test case for default return value being null
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        assertNull(returnsSmartNulls.answer(invocation1));
        
        // Test case for default return value not being null
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMethod().getReturnType()).thenReturn(String.class);
        assertEquals("", returnsSmartNulls.answer(invocation2));
    }
}