import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Invocation invocation;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        invocation = mock(Invocation.class);
    }

    @Test
    public void validate_ShouldCallValidateDoNothing_WhenAnswerIsInstanceOfDoesNothing() {
        // Arrange
        Answer<?> answer = mock(DoesNothing.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator).validateDoNothing((DoesNothing) answer, invocation);
    }

    @Test
    public void validate_DoNothingAnswer_ShouldCallReporterOnlyVoidMethodsCanBeSetToDoNothing_WhenInvocationIsNotVoid() {
        // Arrange
        DoesNothing answer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validateDoNothing(answer, invocation);

        // Assert
        verify(answer).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ShouldCallValidateReturnValue_WhenAnswerIsInstanceOfReturns() {
        // Arrange
        Answer<?> answer = mock(Returns.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator).validateReturnValue((Returns) answer, invocation);
    }

    @Test
    public void validate_ReturnsAnswer_ShouldCallReporterCannotStubVoidMethodWithAReturnValue_WhenInvocationIsVoid() {
        // Arrange
        Returns answer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(answer).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void validate_ReturnsAnswer_ShouldCallReporterWrongTypeOfReturnValue_WhenAnswerReturnsNullAndInvocationReturnsPrimitiveType() {
        // Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(answer).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void validate_ReturnsAnswer_ShouldCallReporterWrongTypeOfReturnValue_WhenAnswerReturnsNonNullAndInvocationHasInvalidReturnType() {
        // Arrange
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(answer).wrongTypeOfReturnValue("String", "int", "testMethod");
    }

    @Test
    public void validate_ShouldCallValidateException_WhenAnswerIsInstanceOfThrowsException() {
        // Arrange
        Answer<?> answer = mock(ThrowsException.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator).validateException((ThrowsException) answer, invocation);
    }

    @Test
    public void validate_ThrowsExceptionAnswer_ShouldCallReporterCannotStubWithNullThrowable_WhenThrowableIsNull() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(answer).cannotStubWithNullThrowable();
    }

    @Test
    public void validate_ThrowsExceptionAnswer_ShouldNotCallReporterCheckedExceptionInvalid_WhenThrowableIsRuntimeException() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(RuntimeException.class);
        when(answer.getThrowable()).thenReturn(throwable);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verifyZeroInteractions(answer);
    }

    @Test
    public void validate_ThrowsExceptionAnswer_ShouldNotCallReporterCheckedExceptionInvalid_WhenThrowableIsError() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(Error.class);
        when(answer.getThrowable()).thenReturn(throwable);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verifyZeroInteractions(answer);
    }

    @Test
    public void validate_ThrowsExceptionAnswer_ShouldCallReporterCheckedExceptionInvalid_WhenThrowableIsCheckedException() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(Exception.class);
        when(answer.getThrowable()).thenReturn(throwable);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(answer).checkedExceptionInvalid(throwable);
    }
}