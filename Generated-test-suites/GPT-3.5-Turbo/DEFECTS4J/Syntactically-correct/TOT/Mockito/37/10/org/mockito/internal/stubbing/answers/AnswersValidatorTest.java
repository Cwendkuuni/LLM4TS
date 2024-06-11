import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidate_WithThrowsException_ValidateExceptionIsCalled() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = spy(new AnswersValidator());

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateException(answer, invocation);
    }

    @Test
    public void testValidate_WithReturns_ValidateReturnValueIsCalled() {
        // Arrange
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = spy(new AnswersValidator());

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateReturnValue(answer, invocation);
    }

    @Test
    public void testValidate_WithDoesNothing_ValidateDoNothingIsCalled() {
        // Arrange
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = spy(new AnswersValidator());

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(answersValidator, times(1)).validateDoNothing(answer, invocation);
    }

    @Test
    public void testValidateDoNothing_WithNonVoidInvocation_ReportOnlyVoidMethodsCanBeSetToDoNothing() {
        // Arrange
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateDoNothing(answer, invocation);

        // Assert
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue_WithVoidInvocation_ReportCannotStubVoidMethodWithAReturnValue() {
        // Arrange
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).cannotStubVoidMethodWithAReturnValue();

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidateReturnValue_WithNullReturnValueAndPrimitiveInvocation_ReportWrongTypeOfReturnValue() {
        // Arrange
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).wrongTypeOfReturnValue("int", "null", "methodName");

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, times(1)).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidateReturnValue_WithNonNullReturnValueAndInvalidReturnType_ReportWrongTypeOfReturnValue() {
        // Arrange
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(anyObject())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("methodName");

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).wrongTypeOfReturnValue("String", "Integer", "methodName");

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateReturnValue(answer, invocation);

        // Assert
        verify(reporter, times(1)).wrongTypeOfReturnValue("String", "Integer", "methodName");
    }

    @Test
    public void testValidateException_WithNullThrowable_ReportCannotStubWithNullThrowable() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).cannotStubWithNullThrowable();

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporter, times(1)).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateException_WithRuntimeExceptionOrError_DoesNotReportError() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        when(answer.getThrowable()).thenReturn(new RuntimeException());

        Reporter reporter = mock(Reporter.class);

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporter, times(0)).checkedExceptionInvalid(anyObject());
    }

    @Test
    public void testValidateException_WithCheckedExceptionAndInvalidException_ReportCheckedExceptionInvalid() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);

        Throwable throwable = new Throwable();
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);

        Reporter reporter = spy(new Reporter());
        doNothing().when(reporter).checkedExceptionInvalid(throwable);

        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.setReporter(reporter);

        // Act
        answersValidator.validateException(answer, invocation);

        // Assert
        verify(reporter, times(1)).checkedExceptionInvalid(throwable);
    }
}