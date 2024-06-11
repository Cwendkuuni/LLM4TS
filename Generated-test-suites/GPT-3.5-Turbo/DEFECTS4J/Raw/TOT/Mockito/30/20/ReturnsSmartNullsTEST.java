import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    // Test case for answer method when delegate answer is not null
    @Test
    public void testAnswer_whenDelegateNotNull_returnsDelegateAnswer() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expectedResult = new Object();
        when(delegate.answer(invocation)).thenReturn(expectedResult);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertEquals(expectedResult, result);
        verify(delegate).answer(invocation);
    }

    // Test case for answer method when delegate answer is null and type is mockable
    @Test
    public void testAnswer_whenDelegateNullAndTypeMockable_returnsSmartNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = String.class;
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(type);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        verify(delegate).answer(invocation);
        verify(invocation).getMethod();
    }

    // Test case for answer method when delegate answer is null and type is not mockable
    @Test
    public void testAnswer_whenDelegateNullAndTypeNotMockable_returnsNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> type = FinalClass.class;
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(type);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertNull(result);
        verify(delegate).answer(invocation);
        verify(invocation).getMethod();
    }

    // Test case for ThrowingInterceptor when method is toString
    @Test
    public void testThrowingInterceptor_whenMethodIsToString_returnsSmartNullWithMethodCall() throws Throwable {
        // Arrange
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Method method = Object.class.getMethod("toString");

        // Act
        Object result = interceptor.intercept(null, method, null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(result.toString().contains("SmartNull returned by unstubbed toString method on mock"));
    }

    // Test case for ThrowingInterceptor when method is not toString
    @Test(expected = NullPointerException.class)
    public void testThrowingInterceptor_whenMethodIsNotToString_throwsNullPointerException() throws Throwable {
        // Arrange
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Method method = String.class.getMethod("toUpperCase");

        // Act
        interceptor.intercept(null, method, null, null);
    }
}

class FinalClass {
    // Final class for testing purpose
}