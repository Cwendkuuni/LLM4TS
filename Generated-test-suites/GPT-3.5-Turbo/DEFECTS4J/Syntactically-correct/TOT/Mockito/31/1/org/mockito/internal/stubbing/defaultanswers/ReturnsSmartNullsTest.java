import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_whenDefaultReturnValueNotNull_shouldReturnDefaultReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn("default");

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertEquals("default", result);
    }

    @Test
    public void testAnswer_whenDefaultReturnValueNullAndReturnTypeMockable_shouldReturnMockedObject() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Class<?> type = Object.class;

        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ThrowingInterceptor);
    }

    @Test
    public void testAnswer_whenDefaultReturnValueNullAndReturnTypeNotMockable_shouldReturnNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Class<?> type = String.class;

        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(false);

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNull(result);
    }
}