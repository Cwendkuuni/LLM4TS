import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporterMock;
    private Answer<Object> throwsExceptionMock;
    private Answer<Object> returnsMock;
    private Answer<Object> doesNothingMock;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporterMock = mock(Reporter.class);
        throwsExceptionMock = mock(ThrowsException.class);
        returnsMock = mock(Returns.class);
        doesNothingMock = mock(DoesNothing.class);
        answersValidator.reporter = reporterMock;
    }

    @Test
    public void validate_ThrowsException_ValidInvocation_ShouldNotReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isValidException(any(Throwable.class))).thenReturn(true);

        // Act
        answersValidator.validate(throwsExceptionMock, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_ThrowsException_NullThrowable_ShouldReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isValidException(any(Throwable.class))).thenReturn(true);
        when(throwsExceptionMock.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(throwsExceptionMock, invocation);

        // Assert
        verify(reporterMock).cannotStubWithNullThrowable();
        verify(reporterMock, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_Returns_ValidInvocation_ShouldNotReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(true);

        // Act
        answersValidator.validate(returnsMock, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void validate_Returns_VoidInvocation_ShouldReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(returnsMock, invocation);

        // Assert
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock, never()).wrongTypeOfReturnValue(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void validate_Returns_NullReturnValueAndPrimitiveInvocation_ShouldReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(returnsMock.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");

        // Act
        answersValidator.validate(returnsMock, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", any(String.class));
    }

    @Test
    public void validate_Returns_NonNullReturnValueAndInvalidReturnTypeInvocation_ShouldReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(returnsMock.returnsNull()).thenReturn(false);
        when(returnsMock.getReturnType()).thenReturn(Object.class);
        when(invocation.isValidReturnType(Object.class)).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");

        // Act
        answersValidator.validate(returnsMock, invocation);

        // Assert
        verify(reporterMock, never()).cannotStubVoidMethodWithAReturnValue();
        verify(reporterMock).wrongTypeOfReturnValue("String", "java.lang.Object", any(String.class));
    }

    @Test
    public void validate_DoNothing_InvalidInvocation_ShouldReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(doesNothingMock, invocation);

        // Assert
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_DoNothing_ValidInvocation_ShouldNotReportError() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(doesNothingMock, invocation);

        // Assert
        verify(reporterMock, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }
}