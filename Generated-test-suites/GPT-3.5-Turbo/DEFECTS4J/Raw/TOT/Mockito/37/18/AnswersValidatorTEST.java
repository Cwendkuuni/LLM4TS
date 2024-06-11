import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Answer throwsException;
    private Answer returns;
    private Answer doesNothing;
    private Reporter reporterMock;
    private Invocation invocationMock;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        throwsException = mock(ThrowsException.class);
        returns = mock(Returns.class);
        doesNothing = mock(DoesNothing.class);
        reporterMock = mock(Reporter.class);
        invocationMock = mock(Invocation.class);
    }

    @Test
    public void testValidate_DoNothing_WhenInvocationIsNotVoid_ShouldCallReporterOnlyVoidMethodsCanBeSetToDoNothing() {
        // Arrange
        when(invocationMock.isVoid()).thenReturn(false);
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(doesNothing, invocationMock);

        // Assert
        verify(reporterMock).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_WhenInvocationIsVoid_ShouldCallReporterCannotStubVoidMethodWithAReturnValue() {
        // Arrange
        when(invocationMock.isVoid()).thenReturn(true);
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(returns, invocationMock);

        // Assert
        verify(reporterMock).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_WhenReturnsNullAndInvocationReturnsPrimitive_ShouldCallReporterWrongTypeOfReturnValue() {
        // Arrange
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(true);
        when(invocationMock.printMethodReturnType()).thenReturn("int");
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(returns, invocationMock);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("int", "null", invocationMock.getMethodName());
    }

    @Test
    public void testValidate_ReturnValue_WhenReturnsNotNullAndInvalidReturnType_ShouldCallReporterWrongTypeOfReturnValue() {
        // Arrange
        when(invocationMock.isVoid()).thenReturn(false);
        when(invocationMock.returnsPrimitive()).thenReturn(false);
        when(invocationMock.isValidReturnType(any())).thenReturn(false);
        when(invocationMock.printMethodReturnType()).thenReturn("String");
        when(returns.printReturnType()).thenReturn("int");
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(returns, invocationMock);

        // Assert
        verify(reporterMock).wrongTypeOfReturnValue("String", "int", invocationMock.getMethodName());
    }

    @Test
    public void testValidate_Exception_WhenThrowableIsNull_ShouldCallReporterCannotStubWithNullThrowable() {
        // Arrange
        when(throwsException.getThrowable()).thenReturn(null);
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(throwsException, invocationMock);

        // Assert
        verify(reporterMock).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_WhenThrowableIsCheckedAndInvalid_ShouldCallReporterCheckedExceptionInvalid() {
        // Arrange
        Throwable throwableMock = mock(Throwable.class);
        when(throwsException.getThrowable()).thenReturn(throwableMock);
        when(throwableMock instanceof RuntimeException || throwableMock instanceof Error).thenReturn(false);
        when(invocationMock.isValidException(any())).thenReturn(false);
        answersValidator.reporter = reporterMock;

        // Act
        answersValidator.validate(throwsException, invocationMock);

        // Assert
        verify(reporterMock).checkedExceptionInvalid(throwableMock);
    }
}