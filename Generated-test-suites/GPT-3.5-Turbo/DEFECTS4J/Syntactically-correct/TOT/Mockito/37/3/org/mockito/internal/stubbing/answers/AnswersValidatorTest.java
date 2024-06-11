
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidate_DoNothingInvokedOnVoidMethod_ShouldThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        DoesNothing mockAnswer = mock(DoesNothing.class);
        Invocation mockInvocation = mock(Invocation.class);
        when(mockInvocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValueInvokedOnVoidMethod_ShouldThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        Returns mockAnswer = mock(Returns.class);
        Invocation mockInvocation = mock(Invocation.class);
        when(mockInvocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValueInvokedWithNullOnPrimitiveMethod_ShouldThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        Returns mockAnswer = mock(Returns.class);
        Invocation mockInvocation = mock(Invocation.class);
        when(mockInvocation.isVoid()).thenReturn(false);
        when(mockInvocation.returnsPrimitive()).thenReturn(true);
        when(mockAnswer.returnsNull()).thenReturn(true);
        when(mockInvocation.printMethodReturnType()).thenReturn("int");
        when(mockInvocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, times(1)).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void testValidate_ReturnValueInvokedWithInvalidReturnType_ShouldThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        Returns mockAnswer = mock(Returns.class);
        Invocation mockInvocation = mock(Invocation.class);
        when(mockInvocation.isVoid()).thenReturn(false);
        when(mockInvocation.returnsPrimitive()).thenReturn(false);
        when(mockAnswer.returnsNull()).thenReturn(false);
        when(mockAnswer.getReturnType()).thenReturn("String");
        when(mockInvocation.printMethodReturnType()).thenReturn("int");
        when(mockInvocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, times(1)).wrongTypeOfReturnValue("int", "String", "testMethod");
    }

    @Test
    public void testValidate_ThrowsExceptionInvokedWithNullThrowable_ShouldThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        ThrowsException mockAnswer = mock(ThrowsException.class);
        Invocation mockInvocation = mock(Invocation.class);
        when(mockAnswer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_ThrowsExceptionInvokedWithCheckedException_ShouldNotThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        ThrowsException mockAnswer = mock(ThrowsException.class);
        Invocation mockInvocation = mock(Invocation.class);
        Throwable mockThrowable = mock(Throwable.class);
        when(mockThrowable instanceof RuntimeException).thenReturn(false);
        when(mockThrowable instanceof Error).thenReturn(false);
        when(mockAnswer.getThrowable()).thenReturn(mockThrowable);
        when(mockInvocation.isValidException(mockThrowable)).thenReturn(true);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_ThrowsExceptionInvokedWithUncheckedException_ShouldNotThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        ThrowsException mockAnswer = mock(ThrowsException.class);
        Invocation mockInvocation = mock(Invocation.class);
        RuntimeException mockThrowable = mock(RuntimeException.class);
        when(mockAnswer.getThrowable()).thenReturn(mockThrowable);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void testValidate_ThrowsExceptionInvokedWithError_ShouldNotThrowException() {
        // Arrange
        Reporter mockReporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(mockReporter);
        ThrowsException mockAnswer = mock(ThrowsException.class);
        Invocation mockInvocation = mock(Invocation.class);
        Error mockThrowable = mock(Error.class);
        when(mockAnswer.getThrowable()).thenReturn(mockThrowable);

        // Act
        answersValidator.validate(mockAnswer, mockInvocation);

        // Assert
        verify(mockReporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }
}
