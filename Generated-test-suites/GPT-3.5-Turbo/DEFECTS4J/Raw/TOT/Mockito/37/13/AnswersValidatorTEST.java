import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;

    @Mock
    private Reporter reporter;

    @Mock
    private Invocation invocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        answersValidator = new AnswersValidator();
    }

    @Test
    public void testValidate_DoNothing_VoidMethod_ValidInvocation() {
        // Arrange
        DoesNothing answer = mock(DoesNothing.class);
        doCallRealMethod().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter, times(0)).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_NonVoidMethod_ValidInvocation() {
        // Arrange
        DoesNothing answer = mock(DoesNothing.class);
        doCallRealMethod().when(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        when(invocation.isVoid()).thenReturn(false);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_Returns_VoidMethod_ValidInvocation() {
        // Arrange
        Returns answer = mock(Returns.class);
        doCallRealMethod().when(reporter).cannotStubVoidMethodWithAReturnValue();
        when(invocation.isVoid()).thenReturn(true);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_Returns_ReturnsNull_ReturnsPrimitive_ValidInvocation() {
        // Arrange
        Returns answer = mock(Returns.class);
        doCallRealMethod().when(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidate_Returns_ReturnsNonNull_InvalidReturnType_ValidInvocation() {
        // Arrange
        Returns answer = mock(Returns.class);
        doCallRealMethod().when(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(String.class)).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(answer.printReturnType()).thenReturn("String");
        when(invocation.getMethodName()).thenReturn("methodName");

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).wrongTypeOfReturnValue("int", "String", "methodName");
    }

    @Test
    public void testValidate_Returns_ReturnsNonNull_ValidReturnType_ValidInvocation() {
        // Arrange
        Returns answer = mock(Returns.class);
        doCallRealMethod().when(reporter).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(answer.getReturnType()).thenReturn(String.class);
        when(invocation.isValidReturnType(String.class)).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("String");
        when(invocation.getMethodName()).thenReturn("methodName");

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter, times(0)).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testValidate_ThrowsException_NullThrowable_ValidInvocation() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        doCallRealMethod().when(reporter).cannotStubWithNullThrowable();
        when(answer.getThrowable()).thenReturn(null);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_ThrowsException_RuntimeException_ValidInvocation() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new RuntimeException());

        // Act
        answersValidator.validate(answer, invocation);

        // Assert - No exception is thrown, so test passes
    }

    @Test
    public void testValidate_ThrowsException_Error_ValidInvocation() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new Error());

        // Act
        answersValidator.validate(answer, invocation);

        // Assert - No exception is thrown, so test passes
    }

    @Test
    public void testValidate_ThrowsException_CheckedException_InvalidInvocation() {
        // Arrange
        ThrowsException answer = mock(ThrowsException.class);
        doCallRealMethod().when(reporter).checkedExceptionInvalid(any(Throwable.class));
        Throwable throwable = mock(Throwable.class);
        when(invocation.isValidException(throwable)).thenReturn(false);
        when(answer.getThrowable()).thenReturn(throwable);

        // Act
        answersValidator.validate(answer, invocation);

        // Assert
        verify(reporter).checkedExceptionInvalid(throwable);
    }
}