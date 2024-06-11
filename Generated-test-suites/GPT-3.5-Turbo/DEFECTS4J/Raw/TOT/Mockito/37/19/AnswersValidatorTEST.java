
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Answer<?> answer;
    private Invocation invocation;
    private Reporter reporter;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
        reporter = mock(Reporter.class);
        answersValidator.setReporter(reporter);
    }

    @Test
    public void validate_shouldInvokeValidateExceptionWhenAnswerIsThrowsException() {
        // Arrange
        answer = mock(ThrowsException.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateException((ThrowsException) answer, invocation);
    }

    @Test
    public void validate_shouldInvokeValidateReturnValueWhenAnswerIsReturns() {
        // Arrange
        answer = mock(Returns.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateReturnValue((Returns) answer, invocation);
    }

    @Test
    public void validate_shouldInvokeValidateDoNothingWhenAnswerIsDoesNothing() {
        // Arrange
        answer = mock(DoesNothing.class);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateDoNothing((DoesNothing) answer, invocation);
    }

    @Test
    public void validateDoNothing_shouldThrowExceptionWhenInvocationIsNotVoid() {
        // Arrange
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validateDoNothing(any(DoesNothing.class), invocation);

        // Assert
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validateDoNothing_shouldNotThrowExceptionWhenInvocationIsVoid() {
        // Arrange
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validateDoNothing(any(DoesNothing.class), invocation);

        // Assert
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validateReturnValue_shouldThrowExceptionWhenInvocationIsVoid() {
        // Arrange
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validateReturnValue(any(Returns.class), invocation);

        // Assert
        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void validateReturnValue_shouldThrowExceptionWhenAnswerReturnsNullAndInvocationReturnsPrimitive() {
        // Arrange
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, times(1)).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }

    @Test
    public void validateReturnValue_shouldThrowExceptionWhenAnswerReturnsNonNullAndInvaildReturnType() {
        // Arrange
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, times(1)).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void validateReturnValue_shouldNotThrowExceptionWhenAnswerReturnsNonNullAndValidReturnType() {
        // Arrange
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, never()).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void validateException_shouldThrowExceptionWhenThrowableIsNull() {
        // Arrange
        when(answer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validateException(any(ThrowsException.class), invocation);

        // Assert
        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void validateException_shouldNotThrowExceptionWhenThrowableIsRuntimeExceptionOrError() {
        // Arrange
        when(answer.getThrowable()).thenReturn(new RuntimeException());

        // Act
        answersValidator.validateException(any(ThrowsException.class), invocation);

        // Assert
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void validateException_shouldThrowExceptionWhenThrowableIsCheckedExceptionAndInvalid() {
        // Arrange
        when(answer.getThrowable()).thenReturn(new Exception());
        when(invocation.isValidException(any())).thenReturn(false);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporter, times(1)).checkedExceptionInvalid(any());
    }

    @Test
    public void validateException_shouldNotThrowExceptionWhenThrowableIsCheckedExceptionAndValid() {
        // Arrange
        when(answer.getThrowable()).thenReturn(new Exception());
        when(invocation.isValidException(any())).thenReturn(true);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporter, never()).checkedExceptionInvalid(any());
    }
}
