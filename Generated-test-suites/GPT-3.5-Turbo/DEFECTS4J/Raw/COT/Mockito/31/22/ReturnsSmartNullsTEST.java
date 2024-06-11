import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;

public class ReturnsSmartNullsTest {

    // Extracted public methods and their signatures:
    // Answer<Object> delegate = new ReturnsMoreEmptyValues();
    // public Object answer(final InvocationOnMock invocation) throws Throwable {...}

    @Test
    public void testAnswer_DelegateNotNull_ReturnsDelegateAnswer() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expected = new Object();
        
        // Act
        when(delegateMock.answer(invocation)).thenReturn(expected);
        Object actual = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testAnswer_DelegateNull_CallsAnswerAndReturnsSmartNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        when(delegateMock.answer(invocation)).thenReturn(null);
        Object actual = returnsSmartNulls.answer(invocation);
        
        // Assert
        assertEquals("SmartNull returned by unstubbed method on mock", actual);
    }

    @Test(expected = NullPointerException.class)
    public void testThrowingInterceptor_InterceptToStringMethod_ReturnsSmartNullMessage() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Location location = mock(Location.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);

        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getLocation()).thenReturn(location);
        when(location.toString()).thenReturn("line 21");

        // Act
        returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(new Object(), Object.class.getMethod("toString"), new Object[]{}, null);

        // Assert - Expect NullPointerException
    }

    @Test(expected = MockitoException.class)
    public void testThowingInterceptor_InterceptNonToStringMethod_ThrowsMockitoException() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Location location = mock(Location.class);

        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        when(invocation.getLocation()).thenReturn(location);
        when(location.toString()).thenReturn("line 34");

        // Act
        returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(new Object(), Object.class.getMethod("hashCode"), new Object[]{}, null);

        // Assert - Expect MockitoException
    }

    @Test
    public void testAnswer_DelegateReturnValueNotNull_ReturnsDelegateReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expected = new Object();

        // Act
        when(delegateMock.answer(invocation)).thenReturn(expected);
        Object actual = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testAnswer_DelegateReturnValueNullAndTypeMockable_ReturnsSmartNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Object.class;

        // Act
        when(delegateMock.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
        Object actual = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals("SmartNull returned by unstubbed method on mock", actual);
    }

    @Test
    public void testAnswer_DelegateReturnValueNullAndTypeNotMockable_ReturnsNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = Object.class;

        // Act
        when(delegateMock.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(false);
        Object actual = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals(null, actual);
    }

    // 
}