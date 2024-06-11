import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

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
    }

    @Test
    public void testValidate_ThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(answer instanceof ThrowsException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);

        answersValidator.validate(answer, invocation);

        // Verify that the corresponding validateException method is called
        verify(answersValidator).validateException(throwsExceptionAnswer, invocation);
    }

    @Test
    public void testValidate_Returns() {
        Returns returnsAnswer = mock(Returns.class);
        when(answer instanceof Returns).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);

        answersValidator.validate(answer, invocation);

        // Verify that the corresponding validateReturnValue method is called
        verify(answersValidator).validateReturnValue(returnsAnswer, invocation);
    }

    @Test
    public void testValidate_DoesNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(answer instanceof DoesNothing).thenReturn(true);
        when(answer).thenReturn(doesNothingAnswer);

        answersValidator.validate(answer, invocation);

        // Verify that the corresponding validateDoNothing method is called
        verify(answersValidator).validateDoNothing(doesNothingAnswer, invocation);
    }

    @Test
    public void testValidate_InvalidAnswer() {
        when(answer instanceof ThrowsException).thenReturn(false);
        when(answer instanceof Returns).thenReturn(false);
        when(answer instanceof DoesNothing).thenReturn(false);

        answersValidator.validate(answer, invocation);

        // Verify that no specific validate method is called
        verify(answersValidator, never()).validateException(any(), any());
        verify(answersValidator, never()).validateReturnValue(any(), any());
        verify(answersValidator, never()).validateDoNothing(any(), any());
    }

    @Test
    public void testValidateDoNothing_NonVoidMethod() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer).thenReturn(doesNothingAnswer);

        answersValidator.validateDoNothing(doesNothingAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidateReturnValue_VoidMethod() {
        Returns returnsAnswer = mock(Returns.class);
        when(invocation.isVoid()).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);

        answersValidator.validateReturnValue(returnsAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidateReturnValue_ReturnsNull_ReturnsPrimitive() {
        Returns returnsAnswer = mock(Returns.class);
        when(returnsAnswer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer).thenReturn(returnsAnswer);

        answersValidator.validateReturnValue(returnsAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
    }

    @Test
    public void testValidateReturnValue_NotReturnsNull_InvalidReturnType() {
        Returns returnsAnswer = mock(Returns.class);
        when(returnsAnswer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(returnsAnswer.getReturnType())).thenReturn(false);
        when(answer).thenReturn(returnsAnswer);

        answersValidator.validateReturnValue(returnsAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), returnsAnswer.printReturnType(), invocation.getMethodName());
    }

    @Test
    public void testValidateException_NullThrowable() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(null);
        when(answer).thenReturn(throwsExceptionAnswer);

        answersValidator.validateException(throwsExceptionAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidateException_RuntimeExceptionOrError() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(true);
        when(answer).thenReturn(throwsExceptionAnswer);

        answersValidator.validateException(throwsExceptionAnswer, invocation);

        // Verify that reporter method is not called
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidateException_InvalidCheckedException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);
        Throwable throwable = mock(Throwable.class);
        when(throwsExceptionAnswer.getThrowable()).thenReturn(throwable);
        when(throwable instanceof RuntimeException).thenReturn(false);
        when(invocation.isValidException(throwable)).thenReturn(false);
        when(answer).thenReturn(throwsExceptionAnswer);

        answersValidator.validateException(throwsExceptionAnswer, invocation);

        // Verify that reporter method is called
        verify(reporter).checkedExceptionInvalid(throwable);
    }
}