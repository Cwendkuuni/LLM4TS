import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

    private AnswersValidator answersValidator = new AnswersValidator();
    private Reporter reporter = new Reporter();

    @Test
    public void testValidate_ThrowsException_ReturnsValidated() {
        // Arrange
        Answer<Throwable> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answer).getThrowable();
        verify(invocation, times(1)).isValidException(any());
        verifyNoMoreInteractions(answer, invocation);
    }

    @Test
    public void testValidate_ThrowsException_ThrowsNullThrowableException() {
        // Arrange
        Answer<Throwable> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.getThrowable()).thenReturn(null);

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).cannotStubWithNullThrowable();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testValidate_ThrowsException_ThrowsCheckedExceptionInvalidException() throws NoSuchMethodException {
        // Arrange
        Answer<Throwable> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        Throwable throwable = new Exception();
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        when(invocation.getMethod()).thenReturn(MockitoException.class.getMethod("getMockInfo"));

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).checkedExceptionInvalid(throwable);
            verify(invocation).isValidException(throwable);
            verify(invocation, times(1)).getMethod();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testValidate_Returns_ReturnsValidated() {
        // Arrange
        Answer<Object> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answer).returnsNull();
        verify(answer).getReturnType();
        verify(invocation).isVoid();
        verify(invocation).getMethod();
        verifyNoMoreInteractions(answer, invocation);
    }

    @Test
    public void testValidate_Returns_ThrowsCannotStubVoidMethodWithAReturnValueException() {
        // Arrange
        Answer<Object> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(true);

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).cannotStubVoidMethodWithAReturnValue();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testValidate_Returns_ThrowsWrongTypeOfReturnValueException() throws NoSuchMethodException {
        // Arrange
        Answer<Object> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(MockitoException.class.getMethod("getMockInfo"));
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).wrongTypeOfReturnValue("java.lang.Object", "java.lang.String", "getMockInfo");
            verify(invocation).isVoid();
            verify(invocation).getMethod();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testValidate_Returns_ThrowsWrongTypeOfReturnValueExceptionForNullReturnValue() throws NoSuchMethodException {
        // Arrange
        Answer<Object> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(MockitoException.class.getMethod("getMockInfo"));
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).wrongTypeOfReturnValue("java.lang.Object", "null", "getMockInfo");
            verify(invocation).isVoid();
            verify(invocation).getMethod();
            verify(invocation).returnsPrimitive();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testValidate_DoNothing_ValidatedForVoidInvocation() {
        // Arrange
        Answer<Object> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(invocation).isVoid();
        verifyNoMoreInteractions(answer, invocation);
    }

    @Test
    public void testValidate_DoNothing_ThrowsOnlyVoidMethodsCanBeSetToDoNothingException() throws NoSuchMethodException {
        // Arrange
        Answer<Object> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(MockitoException.class.getMethod("getMockInfo"));

        // Act
        try {
            answersValidator.validate(answer, invocation);
        } catch (MockitoException e) {
            // Assert
            verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
            verify(invocation).isVoid();
            verify(invocation).getMethod();
            verifyNoMoreInteractions(answer, invocation);
            return;
        }

        // Assert
        fail("Expected MockitoException to be thrown");
    }
}