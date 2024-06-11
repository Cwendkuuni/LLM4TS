
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        // Arrange
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(new Object());

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testAnswer_defaultReturnValueNull() throws Throwable {
        // Arrange
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswer_returnTypeMockable() throws Throwable {
        // Arrange
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(true);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswer_returnTypeNotMockable() throws Throwable {
        // Arrange
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(false);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNull(result);
    }
}