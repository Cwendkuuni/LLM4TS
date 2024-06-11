import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest{

    @Test
    public void testAnswer() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);

        // Act
        Object result = returnsSmartNulls.new ThrowingInterceptor(invocation)
                .intercept(null, null, null, null);

        // Assert
        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testFormatMethodCall() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});

        // Act
        Object result = returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall();

        // Assert
        assertEquals("toString()", result);
    }

    @Test
    public void testAnswerCanImposterise() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        ClassImposterizer mockImposterizer = Mockito.mock(ClassImposterizer.class);
        ClassImposterizer.INSTANCE = mockImposterizer;
        Mockito.when(mockImposterizer.canImposterise(method.getReturnType())).thenReturn(true);

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswerCannotImposterise() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        ClassImposterizer mockImposterizer = Mockito.mock(ClassImposterizer.class);
        ClassImposterizer.INSTANCE = mockImposterizer;
        Mockito.when(mockImposterizer.canImposterise(method.getReturnType())).thenReturn(false);

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswerNotNullReturnValue() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> mockDelegate = Mockito.mock(Answer.class);
        Mockito.when(mockDelegate.answer(invocation)).thenReturn(new Object());
        returnsSmartNulls.delegate = mockDelegate;

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testAnswerNullReturnValue() throws Throwable {
        // Arrange
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> mockDelegate = Mockito.mock(Answer.class);
        Mockito.when(mockDelegate.answer(invocation)).thenReturn(null);
        returnsSmartNulls.delegate = mockDelegate;

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
    }
}