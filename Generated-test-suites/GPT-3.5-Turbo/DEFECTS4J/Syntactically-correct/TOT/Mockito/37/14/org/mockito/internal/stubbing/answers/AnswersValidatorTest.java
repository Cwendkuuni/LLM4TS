import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.DoesNothing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;
    private Answer<?> answerMock;
    private Invocation invocationMock;

    @Before
    public void setup() {
        reporterMock = mock(Reporter.class);
        answersValidator = new AnswersValidator();
        answersValidator.reporter = reporterMock;
        answerMock = mock(Answer.class);
        invocationMock = mock(Invocation.class);
    }

    @Test
    public void testValidate_DoNothing_WithVoidInvocation_NoError() {
        // Arrange
        DoesNothing answer = new DoesNothing();
        when(invocationMock.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocationMock);

        // Assert
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void testValidate_DoNothing_WithNonVoidInvocation_ReportError() {
        // Arrange
        DoesNothing answer = new DoesNothing();
        when(invocationMock.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(answer, invocationMock);

        // Assert
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_Returns_WithVoidInvocation_ReportError() {
        // Arrange
        Returns answer = new Returns();
        when(invocationMock.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocationMock);

        // Assert
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_Returns_WithNullAnswerAndPrimitiveInvocation_ReportError() {
        // Arrange
        Returns answer = new Returns();
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(true);
        when(answerMock.returnsNull()).thenReturn(true);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        when(invocationMock.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void testValidate_Returns_WithNonNullAnswerAndInvalidReturnTypeInvocation_ReportError() {
        // Arrange
        Returns answer = new Returns();
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(answerMock.returnsNull()).thenReturn(false);
        when(answerMock.getReturnType()).thenReturn(String.class);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        when(invocationMock.getMethodName()).thenReturn("testMethod");
        when(invocationMock.isValidReturnType(String.class)).thenReturn(false);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "String", "testMethod");
    }

    @Test
    public void testValidate_Returns_WithNonNullAnswerAndValidReturnTypeInvocation_NoError() {
        // Arrange
        Returns answer = new Returns();
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(answerMock.returnsNull()).thenReturn(false);
        when(answerMock.getReturnType()).thenReturn(String.class);
        when(invocationMock.printMethodReturnType()).thenReturn("String");
        when(invocationMock.getMethodName()).thenReturn("testMethod");
        when(invocationMock.isValidReturnType(String.class)).thenReturn(true);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void testValidate_ThrowsException_WithNullThrowable_ReportError() {
        // Arrange
        ThrowsException answer = new ThrowsException();
        when(answerMock.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verify(reporterMock).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_ThrowsException_WithRuntimeException_ReportNoError() {
        // Arrange
        ThrowsException answer = new ThrowsException();
        Throwable throwableMock = mock(RuntimeException.class);
        when(answerMock.getThrowable()).thenReturn(throwableMock);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void testValidate_ThrowsException_WithError_ReportNoError() {
        // Arrange
        ThrowsException answer = new ThrowsException();
        Throwable throwableMock = mock(Error.class);
        when(answerMock.getThrowable()).thenReturn(throwableMock);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void testValidate_ThrowsException_WithCheckedExceptionInvalid_ReportError() {
        // Arrange
        ThrowsException answer = new ThrowsException();
        Throwable throwableMock = mock(Exception.class);
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        when(invocationMock.isValidException(throwableMock)).thenReturn(false);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verify(reporterMock).checkedExceptionInvalid(throwableMock);
    }

    @Test
    public void testValidate_ThrowsException_WithCheckedExceptionValid_NoError() {
        // Arrange
        ThrowsException answer = new ThrowsException();
        Throwable throwableMock = mock(Exception.class);
        when(answerMock.getThrowable()).thenReturn(throwableMock);
        when(invocationMock.isValidException(throwableMock)).thenReturn(true);

        // Act
        answersValidator.validate(answerMock, invocationMock);

        // Assert
        verifyZeroInteractions(reporterMock);
    }
}