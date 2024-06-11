import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AnswersValidatorTest {

    private AnswersValidator answersValidator;
    private Reporter reporter;
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_DoNothing_WithNonVoidInvocation_ShouldThrowException() {
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(new DoesNothing(), invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_WithVoidInvocation_ShouldThrowException() {
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(new Returns(), invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_WithNullValueAndPrimitiveReturnType_ShouldThrowException() {
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(new Returns(), invocation);

        verify(reporter).wrongTypeOfReturnValue("int", "null", "methodName");
    }

    @Test
    public void testValidate_ReturnValue_WithNonNullValueAndInvalidReturnType_ShouldThrowException() {
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType("String")).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(answer.printReturnType()).thenReturn("String");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(new Returns(), invocation);

        verify(reporter).wrongTypeOfReturnValue("int", "String", "methodName");
    }

    @Test
    public void testValidate_Exception_WithNullThrowable_ShouldThrowException() {
        when(answer.getThrowable()).thenReturn(null);

        answersValidator.validate(new ThrowsException(), invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_WithRuntimeExceptionOrError_ShouldNotThrowException() {
        when(answer.getThrowable()).thenReturn(new RuntimeException());

        answersValidator.validate(new ThrowsException(), invocation);

        // No exception should be thrown
    }

    @Test
    public void testValidate_Exception_WithCheckedException_ShouldThrowException() {
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        when(reporter.checkedExceptionInvalid(throwable)).thenThrow(new RuntimeException());

        answersValidator.validate(new ThrowsException(), invocation);

        verify(reporter).checkedExceptionInvalid(throwable);
    }
}