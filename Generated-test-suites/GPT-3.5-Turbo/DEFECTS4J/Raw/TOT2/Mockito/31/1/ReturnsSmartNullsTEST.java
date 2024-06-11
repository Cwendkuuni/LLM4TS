import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getClass().getMethod("someMethod"));
        
        Object result = smartNulls.answer(invocationMock);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswerReturnsSmartNullsThrowsException() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getClass().getMethod("someMethod"));
        
        exception.expect(NullPointerException.class);
        smartNulls.answer(invocationMock);
    }
    
    private void someMethod() {
        // Some implementation for testing
    }

}