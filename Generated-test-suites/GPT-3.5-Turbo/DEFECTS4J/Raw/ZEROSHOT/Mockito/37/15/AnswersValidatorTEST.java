package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

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
        answersValidator.reporter = reporter;
        answer = mock(Answer.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testValidate_ThrowsException() {
        ThrowsException throwsExceptionAnswer = mock(ThrowsException.class);

        when(answer instanceof ThrowsException).thenReturn(true);
        when((ThrowsException) answer).thenReturn(throwsExceptionAnswer);

        answersValidator.validate(answer, invocation);

        verify(throwsExceptionAnswer).getThrowable();
        verify(invocation, never()).isValidException(any(Throwable.class));
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_Returns() {
        Returns returnsAnswer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(returnsAnswer.getReturnType())).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(answer).returnsNull();
        verify(invocation).isValidReturnType(returnsAnswer.getReturnType());
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testValidate_Returns_VoidMethod() {
        Returns returnsAnswer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);

        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        verifyZeroInteractions(returnsAnswer);
    }

    @Test
    public void testValidate_Returns_ReturnsNullAndReturnsPrimitive() {
        Returns returnsAnswer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(answer.returnsNull()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(answer).returnsNull();
        verify(invocation).printMethodReturnType();
        verify(reporter).wrongTypeOfReturnValue(any(), eq("null"), any());
        verifyZeroInteractions(returnsAnswer);
    }

    @Test
    public void testValidate_Returns_InvalidReturnType() {
        Returns returnsAnswer = mock(Returns.class);

        when(answer instanceof Returns).thenReturn(true);
        when((Returns) answer).thenReturn(returnsAnswer);

        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(returnsAnswer.getReturnType())).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(answer).returnsNull();
        verify(invocation).printMethodReturnType();
        verify(answer).printReturnType();
        verify(invocation).getMethodName();
        verify(reporter).wrongTypeOfReturnValue(any(), any(), any());
        verifyZeroInteractions(returnsAnswer);
    }

    @Test
    public void testValidate_DoesNothing() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);

        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);

        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verifyZeroInteractions(reporter, doesNothingAnswer);
    }

    @Test
    public void testValidate_DoesNothing_NonVoidMethod() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        
        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);

        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        verifyZeroInteractions(doesNothingAnswer);
    }

    @Test
    public void testValidate_DoesNothing_VerifyInteractions() {
        DoesNothing doesNothingAnswer = mock(DoesNothing.class);
        
        when(answer instanceof DoesNothing).thenReturn(true);
        when((DoesNothing) answer).thenReturn(doesNothingAnswer);

        when(invocation.isVoid()).thenReturn(true);

        answersValidator.validate(answer, invocation);

        verify(invocation).isVoid();
        verifyZeroInteractions(reporter);
    }
}