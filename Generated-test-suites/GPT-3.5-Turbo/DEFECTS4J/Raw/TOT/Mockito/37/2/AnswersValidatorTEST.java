import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answersValidator.setReporter(reporterMock);
    }

    @Test
    public void validate_ThrowsExceptionAnswer_ValidInvocation_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        
        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ThrowsExceptionAnswer_NullThrowable_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(answer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ReturnsAnswer_VoidInvocation_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ReturnsAnswer_NullReturnPrimitiveInvocation_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "testMethod");
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ReturnsAnswer_InvalidReturnTypeInvocation_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("List<String>");
        when(answer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("testMethod");

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock).wrongTypeOfReturnValue("List<String>", "Integer", "testMethod");
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_DoesNothingAnswer_NonVoidInvocation_ReporterCalled() {
        // Arrange
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_DoesNothingAnswer_VoidInvocation_NoReporterCall() {
        // Arrange
        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any());
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(), any(), any());
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

}