import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

    @Test
    public void testValidate_ThrowsException_ValidInvocation() {
        // Arrange
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = new AnswersValidator();

        // Act
        answersValidator.validate(throwsException, invocation);

        // Assert
        verify(throwsException, times(1)).getThrowable();
        verify(invocation, never()).isValidException(any(Throwable.class));
        verify(invocation, never()).isVoid();

    }

    @Test
    public void testValidate_ThrowsException_NullThrowable() {
        // Arrange
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        doThrow(new IllegalArgumentException()).when(reporter).cannotStubWithNullThrowable();

        // Act
        answersValidator.validate(throwsException, invocation);

        // Assert
        verify(throwsException, times(1)).getThrowable();
        verify(reporter, times(1)).cannotStubWithNullThrowable();
        verify(invocation, never()).isValidException(any(Throwable.class));

    }

    @Test
    public void testValidate_ThrowsException_CheckedExceptionInvalid() {
        // Arrange
        ThrowsException throwsException = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        doThrow(new IllegalArgumentException()).when(reporter).checkedExceptionInvalid(any(Throwable.class));

        // Act
        answersValidator.validate(throwsException, invocation);

        // Assert
        verify(throwsException, times(1)).getThrowable();
        verify(reporter, times(1)).checkedExceptionInvalid(any(Throwable.class));
        verify(invocation, never()).isValidException(any(Throwable.class));

    }

    @Test
    public void testValidate_Returns_ValidInvocation() {
        // Arrange
        Returns returns = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = new AnswersValidator();

        // Act
        answersValidator.validate(returns, invocation);

        // Assert
        verify(returns, times(1)).returnsNull();
        verify(returns, times(1)).getReturnType();
        verify(invocation, never()).isValidReturnType(any(Class.class));
        verify(invocation, never()).returnsPrimitive();
        verify(invocation, never()).isVoid();

    }

    @Test
    public void testValidate_Returns_VoidMethodWithReturnValue() {
        // Arrange
        Returns returns = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        doThrow(new IllegalArgumentException()).when(reporter).cannotStubVoidMethodWithAReturnValue();

        // Act
        answersValidator.validate(returns, invocation);

        // Assert
        verify(returns, times(1)).returnsNull();
        verify(returns, times(1)).getReturnType();
        verify(invocation, times(1)).isVoid();
        verify(reporter, times(1)).cannotStubVoidMethodWithAReturnValue();
        verify(invocation, never()).isValidReturnType(any(Class.class));
        verify(invocation, never()).returnsPrimitive();

    }

    @Test
    public void testValidate_Returns_WrongTypeOfReturnValue() {
        // Arrange
        Returns returns = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        doThrow(new IllegalArgumentException()).when(reporter)
                .wrongTypeOfReturnValue(any(String.class), any(String.class), anyString());

        // Act
        answersValidator.validate(returns, invocation);

        // Assert
        verify(returns, times(1)).returnsNull();
        verify(returns, times(1)).getReturnType();
        verify(invocation, never()).isVoid();
        verify(reporter, times(1)).wrongTypeOfReturnValue(any(String.class), any(String.class), anyString());
        verify(invocation, times(1)).printMethodReturnType();
        verify(invocation, times(1)).getMethodNam();
        verify(invocation, never()).isValidReturnType(any(Class.class));
        verify(invocation, never()).returnsPrimitive();

    }

    @Test
    public void testValidate_DoesNothing_ValidInvocation() {
        // Arrange
        DoesNothing doesNothing = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        AnswersValidator answersValidator = new AnswersValidator();

        // Act
        answersValidator.validate(doesNothing, invocation);

        // Assert
        verify(invocation, never()).isValidReturnType(any(Class.class));
        verify(invocation, never()).returnsPrimitive();
        verify(invocation, times(1)).isVoid();

    }

    @Test
    public void testValidate_DoesNothing_OnlyVoidMethodsCanBeSetToDoNothing() {
        // Arrange
        DoesNothing doesNothing = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator();
        doThrow(new IllegalArgumentException()).when(reporter).onlyVoidMethodsCanBeSetToDoNothing();

        // Act
        answersValidator.validate(doesNothing, invocation);

        // Assert
        verify(invocation, never()).isValidReturnType(any(Class.class));
        verify(invocation, never()).returnsPrimitive();
        verify(invocation, times(1)).isVoid();
        verify(reporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();

    }
}