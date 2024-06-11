import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withNonMockableReturnType_returnsNull() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertEquals(null, result);
    }
    
    @Test
    public void testAnswer_withMockableReturnType_returnsSmartNull() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(StringBuilder.class.getMethod("length"));
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertEquals("SmartNull returned by unstubbed length method on mock", result);
    }
    
    @Test(expected = RuntimeException.class)
    public void testAnswer_withUnsupportedMethod_throwsException() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("charAt", int.class));
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        // Expecting RuntimeException to be thrown
    }
}