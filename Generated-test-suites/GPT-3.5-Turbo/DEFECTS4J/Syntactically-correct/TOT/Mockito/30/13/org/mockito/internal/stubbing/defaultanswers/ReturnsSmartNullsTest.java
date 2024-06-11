import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void answer_DelegatesTo_ReturnsMoreEmptyValues() throws Throwable {
        // Arrange
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getArguments()).thenReturn(null);
        Object expected = new Object();
        Answer<Object> delegateMock = mock(Answer.class);
        when(delegateMock.answer(invocationMock)).thenReturn(expected);
        returnsSmartNulls.delegate = delegateMock;

        // Act
        Object result = returnsSmartNulls.answer(invocationMock);

        // Assert
        assertEquals(expected, result);
        verify(delegateMock).answer(invocationMock);
    }

    @Test
    public void answer_ReturnsNull_WhenDelegatesTo_ReturnsMoreEmptyValues_ReturnsNull() throws Throwable {
        // Arrange
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getArguments()).thenReturn(null);
        Answer<Object> delegateMock = mock(Answer.class);
        when(delegateMock.answer(invocationMock)).thenReturn(null);
        returnsSmartNulls.delegate = delegateMock;

        // Act
        Object result = returnsSmartNulls.answer(invocationMock);

        // Assert
        assertNull(result);
        verify(delegateMock).answer(invocationMock);
    }

    @Test
    public void answer_ReturnsNull_WhenReturnTypeCannotBeImposterized() throws Throwable {
        // Arrange
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getArguments()).thenReturn(null);
        Answer<Object> delegateMock = mock(Answer.class);
        when(delegateMock.answer(invocationMock)).thenReturn(null);
        returnsSmartNulls.delegate = delegateMock;

        // Act
        Object result = returnsSmartNulls.answer(invocationMock);

        // Assert
        assertNull(result);
        verify(delegateMock).answer(invocationMock);
    }

    @Test
    public void intercept_ReturnsSmartNull_WhenMethodIsToString() throws Throwable {
        // Arrange
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocationMock.getArguments()).thenReturn(null);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = 
                returnsSmartNulls.new ThrowingInterceptor(invocationMock);

        // Act
        Object result = throwingInterceptor.intercept(null, invocationMock.getMethod(), null, null);

        // Assert
        assertTrue(result instanceof String);
        assertTrue(((String) result).startsWith("SmartNull returned by unstubbed toString method on mock"));
    }

    @Test(expected = NullPointerException.class)
    public void intercept_ThrowsNullPointerException_WhenMethodIsNotToString() throws Throwable {
        // Arrange
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getArguments()).thenReturn(null);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = 
                returnsSmartNulls.new ThrowingInterceptor(invocationMock);

        // Act
        throwingInterceptor.intercept(null, null, null, null);
    }
}