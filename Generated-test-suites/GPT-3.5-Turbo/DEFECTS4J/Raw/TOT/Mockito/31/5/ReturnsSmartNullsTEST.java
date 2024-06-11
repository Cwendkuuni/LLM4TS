import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_withDefaultReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("getString");
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAnswer_withMockedReturnType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Mockito.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Mockito);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswer_withUnsupportedReturnType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        // Expecting NullPointerException
    }
    
    @Test
    public void testAnswer_withToStringMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        
        // Act
        Object result = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

}