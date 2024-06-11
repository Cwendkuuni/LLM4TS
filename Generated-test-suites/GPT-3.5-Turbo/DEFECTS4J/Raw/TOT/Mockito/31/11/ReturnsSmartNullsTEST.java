import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withNonNullValue_returnNonNullValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("Some Value");
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertEquals("Some Value", result);
    }

    @Test
    public void testAnswer_withNullValueAndMockableReturnType_returnSmartNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof SmartNull);
    }

    @Test
    public void testAnswer_withNullValueAndNonMockableReturnType_returnNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_withNullValueAndToStringMethod_throwNullPointerException() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        // Act
        answer.answer(invocation);
        
        // Assert (Exception is expected)
    }
}