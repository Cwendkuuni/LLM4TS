import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_withNonNullReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn("test");

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals("test", result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_withNullReturnValueAndMockableType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(String.class.getDeclaredMethod("toString"));

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_withNullReturnValueAndNonMockableType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(Void.class.getDeclaredMethod("toString"));

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
        verify(delegate).answer(invocation);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_withNullReturnValueAndNonMockableType_shouldThrowNullPointerException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(Integer.class.getDeclaredMethod("toString"));

        // Act
        returnsSmartNulls.answer(invocation);

        // Assert
        // Exception is thrown
    }

}