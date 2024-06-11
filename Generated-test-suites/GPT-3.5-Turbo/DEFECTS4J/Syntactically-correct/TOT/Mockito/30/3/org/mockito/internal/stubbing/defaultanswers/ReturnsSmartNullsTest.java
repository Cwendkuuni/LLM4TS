import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls;
    
    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }
    
    @Test
    public void testAnswer_WithUnstubbedMethodCall_ReturnsSmartNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("unstubbedMethod");
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.toString().contains("SmartNull returned by unstubbed"));
    }
    
    @Test
    public void testAnswer_WithStubbedMethodCall_ReturnsDefaultReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("stubbedMethod");
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testAnswer_WithNullDefaultReturnValue_ReturnsSmartNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("unstubbedMethod");
        when(returnsSmartNulls.answer(invocation)).thenReturn(null);
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.toString().contains("SmartNull returned by unstubbed"));
    }
    
    @Test
    public void testAnswer_WithFinalReturnType_ReturnsNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("finalMethod");
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswer_WithInvalidMethodCall_ThrowsNullPointerException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("invalidMethod");
        
        // Act
        returnsSmartNulls.answer(invocation);
        
        // Assert (Expected Exception)
    }
}