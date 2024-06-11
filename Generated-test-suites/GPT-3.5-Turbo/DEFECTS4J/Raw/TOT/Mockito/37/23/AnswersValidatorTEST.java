import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.*;


import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Invocation invocation;
    private Answer<?> answer;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        invocation = mock(Invocation.class);
        answer = mock(ThrowsException.class);

        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_NonVoidInvocation() {
        // Arrange
        DoesNothing doNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(doNothingAnswer, invocation);

        // Assert
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }


    @Test
    public void testValidate_ReturnValue_VoidInvocation() {
        // Arrange
        Returns returnValueAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(returnValueAnswer, invocation);

        // Assert
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_ReturnsNull_PrimitiveInvocation() {
        // Arrange
        Returns returnValueAnswer = mock(Returns.class);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(returnValueAnswer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        // Act
        answersValidator.validate(returnValueAnswer, invocation);

        // Assert
        verify(reporter).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_NonNull_InvalidReturnTypeInvocation() {
        // Arrange
        Returns returnValueAnswer = mock(Returns.class);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(returnValueAnswer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any(Class.class))).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(returnValueAnswer.printReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        // Act
        answersValidator.validate(returnValueAnswer, invocation);

        // Assert
        verify(reporter).wrongTypeOfReturnValue("String", "int", "methodName");
    }

    @Test
    public void testValidate_Exception_NullThrowable() {
        // Arrange
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(throwsExceptionAnswer, invocation);

        // Assert
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_Runtime_Exception() {
        // Arrange
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable runtimeException = mock(RuntimeException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(runtimeException);

        // Act
        answersValidator.validate(throwsExceptionAnswer, invocation);

        // Assert
        verify(reporter, never()).checkedExceptionInvalid(runtimeException);
    }

    @Test
    public void testValidate_Exception_Error() {
        // Arrange
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable error = mock(Error.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(error);

        // Act
        answersValidator.validate(throwsExceptionAnswer, invocation);

        // Assert
        verify(reporter, never()).checkedExceptionInvalid(error);
    }

    @Test
    public void testValidate_Exception_Checked_Exception() {
        // Arrange
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable checkedException = mock(Exception.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(checkedException);
        when(invocation.isValidException(checkedException)).thenReturn(false);

        // Act
        answersValidator.validate(throwsExceptionAnswer, invocation);

        // Assert
        verify(reporter).checkedExceptionInvalid(checkedException);
    }
}