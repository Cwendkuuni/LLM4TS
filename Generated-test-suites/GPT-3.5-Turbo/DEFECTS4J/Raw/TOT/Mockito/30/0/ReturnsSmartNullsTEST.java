import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls smartNulls;

    @Before
    public void setUp() {
        smartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_WithUnstubbedMethod_ReturnsSmartNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswer_WithStubbedMethod_ReturnsSmartNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswer_WithFinalReturnType_ReturnsNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Double.class);

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswer_WithToStringMethod_ReturnsSmartNull() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(new ObjectMethodsGuru().isToString(invocation.getMethod())).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        // Act
        Object result = smartNulls.answer(invocation);

        // Assert
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
}