
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;
    private Answer<?> answerMock;
    private Invocation invocationMock;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        answerMock = mock(Answer.class);
        invocationMock = mock(Invocation.class);
        answersValidator.setReporter(reporterMock);
    }

    @Test
    public void validate_DoNothing_Answer_VoidInvocation_VoidMethod_ShouldNotReportError() {
        when(invocationMock.isVoid()).thenReturn(true);
        when(answerMock instanceof DoesNothing).thenReturn(true);

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_DoNothing_Answer_NonVoidInvocation_ShouldReportError() {
        when(invocationMock.isVoid()).thenReturn(false);
        when(answerMock instanceof DoesNothing).thenReturn(true);

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ReturnValue_Answer_VoidInvocation_ShouldReportError() {
        when(invocationMock.isVoid()).thenReturn(true);
        when(answerMock instanceof Returns).thenReturn(true);

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void validate_ReturnValue_Answer_NullReturnForPrimitiveInvocation_ShouldReportError() {
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(true);
        when(answerMock instanceof Returns).thenReturn(true);
        when(((Returns) answerMock).returnsNull()).thenReturn(true);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        when(invocationMock.getMethodName()).thenReturn("testMethod");

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).wrongTypeOfReturnValue("int", "null", "testMethod");
    }

    @Test
    public void validate_ReturnValue_Answer_InvalidReturnTypeInvocation_ShouldReportError() {
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(invocationMock.isValidReturnType(any(Class.class))).thenReturn(false);
        when(answerMock instanceof Returns).thenReturn(true);
        when(((Returns) answerMock).returnsNull()).thenReturn(false);
        when(invocationMock.printMethodReturnType()).thenReturn("void");
        when(((Returns) answerMock).printReturnType()).thenReturn("String");
        when(invocationMock.getMethodName()).thenReturn("testMethod");

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).wrongTypeOfReturnValue("void", "String", "testMethod");
    }

    @Test
    public void validate_Exception_Answer_NullThrowable_ShouldReportError() {
        when(answerMock instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answerMock).getThrowable()).thenReturn(null);

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).cannotStubWithNullThrowable();
    }

    @Test
    public void validate_Exception_Answer_RuntimeExceptionThrowable_ShouldNotReportError() {
        when(answerMock instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answerMock).getThrowable()).thenReturn(new RuntimeException());

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_Exception_Answer_ErrorThrowable_ShouldNotReportError() {
        when(answerMock instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answerMock).getThrowable()).thenReturn(new Error());

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_Exception_Answer_CheckedThrowable_ShouldReportError() {
        when(answerMock instanceof ThrowsException).thenReturn(true);
        when(((ThrowsException) answerMock).getThrowable()).thenReturn(new Exception());

        answersValidator.validate(answerMock, invocationMock);

        verify(reporterMock).checkedExceptionInvalid(any(Throwable.class));
    }
}
