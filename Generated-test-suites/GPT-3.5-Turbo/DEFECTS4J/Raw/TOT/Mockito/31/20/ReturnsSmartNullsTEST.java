import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void returnsSmartNulls_answer_withOrdinaryReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void returnsSmartNulls_answer_withNullForFinalReturnType() throws Throwable {
        // Arrange
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        when(mockInvocation.getMethod().getReturnType()).thenReturn(MyFinalClass.class);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void returnsSmartNulls_answer_withSmartNullForNonFinalReturnType() throws Throwable {
        // Arrange
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        when(mockInvocation.getMethod().getReturnType()).thenReturn(NonFinalClass.class);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof NonFinalClass);
    }
    
    @Test
    public void returnsSmartNulls_answer_withSmartNullForToStringMethodCall() throws Throwable {
        // Arrange
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        when(mockInvocation.getMethod().getName()).thenReturn("toString");
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void returnsSmartNulls_answer_withNPEForOtherMethodCalls() throws Throwable {
        // Arrange
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        
        // Act
        returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        // NPE is expected to be thrown
    }
    
    private class MyFinalClass {
        // Final class for testing purposes
    }
    
    private class NonFinalClass {
        // Non-final class for testing purposes
    }
}