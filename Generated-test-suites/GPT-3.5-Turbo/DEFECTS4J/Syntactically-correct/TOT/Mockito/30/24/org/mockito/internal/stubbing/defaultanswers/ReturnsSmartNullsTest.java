import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_WithNonNullDefaultReturnValue_ShouldReturnDefaultReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        Mockito.when(delegate.answer(Mockito.eq(invocation))).thenReturn(defaultReturnValue);

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_WithNullDefaultReturnValueAndMockableReturnType_ShouldReturnSmartNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(delegate.answer(Mockito.eq(invocation))).thenReturn(null);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ReturnsSmartNulls.ThrowingInterceptor);
    }

    @Test
    public void testAnswer_WithNullDefaultReturnValueAndNonMockableReturnType_ShouldReturnNull() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(delegate.answer(Mockito.eq(invocation))).thenReturn(null);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertNull(result);
    }
}