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
    private Answer<?> answer;
    private Invocation invocation;

    @Before
    public void setup() {
        answersValidator = new AnswersValidator();
        reporter = mock(Reporter.class);
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
    }

    @Test
    public void validate_DoNothingAnswerWithNonVoidMethod_ShouldInvokeReporter() {
        DoesNothing answer = mock(DoesNothing.class);
        invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void validate_ReturnsAnswerWithVoidMethod_ShouldInvokeReporter() {
        Returns answer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void validate_ReturnsAnswerWithNullReturnValueForPrimitiveMethod_ShouldInvokeReporter() {
        Returns answer = mock(Returns.class);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("int");
        when(invocation.getMethodName()).thenReturn("exampleMethod");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("int", "null", "exampleMethod");
    }

    @Test
    public void validate_ReturnsAnswerWithInvalidReturnType_ShouldInvokeReporter() {
        Returns answer = mock(Returns.class);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("String");
        when(answer.printReturnType()).thenReturn("Integer");
        when(invocation.getMethodName()).thenReturn("exampleMethod");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("String", "Integer", "exampleMethod");
    }

    @Test
    public void validate_ThrowsExceptionAnswerWithNullThrowable_ShouldInvokeReporter() {
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void validate_ThrowsExceptionAnswerWithRuntimeException_ShouldNotInvokeReporter() {
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new RuntimeException());

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_ThrowsExceptionAnswerWithError_ShouldNotInvokeReporter() {
        ThrowsException answer = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(new Error());

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_ThrowsExceptionAnswerWithValidCheckedException_ShouldNotInvokeReporter() {
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
    }

    @Test
    public void validate_ThrowsExceptionAnswerWithInvalidCheckedException_ShouldInvokeReporter() {
        ThrowsException answer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).checkedExceptionInvalid(throwable);
    }
}