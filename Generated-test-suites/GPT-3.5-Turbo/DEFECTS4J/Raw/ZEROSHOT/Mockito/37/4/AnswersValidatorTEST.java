package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

    @Test
    public void testValidate_ThrowsException() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
        verifyNoMoreInteractions(reporter);
        verifyNoInteractions(invocation);
    }

    @Test
    public void testValidate_Returns() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(true);
        when(invocation.printMethodReturnType()).thenReturn("void");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubVoidMethodWithAReturnValue();
        verifyNoMoreInteractions(reporter);
        verify(invocation, times(2)).isVoid();
        verify(invocation).returnsPrimitive();
        verify(invocation).printMethodReturnType();
        verify(invocation, times(2)).getMethodName();
        verifyNoInteractions(answer);
    }

    @Test
    public void testValidate_DoesNothing() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
        verifyNoMoreInteractions(reporter);
        verify(invocation, times(2)).isVoid();
        verifyNoInteractions(answer);
    }

    @Test
    public void testValidate_ReturnValue_WithNullReturnType() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(true);
        when(invocation.returnsPrimitive()).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("null");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("null", "null", "methodName");
        verifyNoMoreInteractions(reporter);
        verify(invocation).isVoid();
        verify(invocation).returnsPrimitive();
        verify(invocation).printMethodReturnType();
        verify(invocation).getMethodName();
        verifyNoInteractions(answer);
    }

    @Test
    public void testValidate_ReturnValue_WithInvalidReturnType() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(answer.returnsNull()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        when(invocation.printMethodReturnType()).thenReturn("invalid");
        when(answer.printReturnType()).thenReturn("valid");
        when(invocation.getMethodName()).thenReturn("methodName");

        answersValidator.validate(answer, invocation);

        verify(reporter).wrongTypeOfReturnValue("invalid", "valid", "methodName");
        verifyNoMoreInteractions(reporter);
        verify(invocation).isVoid();
        verify(invocation).isValidReturnType(any());
        verify(invocation).printMethodReturnType();
        verify(invocation).getMethodName();
        verify(answer).printReturnType();
    }

    @Test
    public void testValidate_Exception_NullThrowable() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        when(((ThrowsException) answer).getThrowable()).thenReturn(null);

        answersValidator.validate(answer, invocation);

        verify(reporter).cannotStubWithNullThrowable();
        verifyNoMoreInteractions(reporter);
        verifyNoInteractions(invocation);
    }

    @Test
    public void testValidate_Exception_RuntimeExceptionOrError() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(RuntimeException.class);
        when(((ThrowsException) answer).getThrowable()).thenReturn(throwable);

        answersValidator.validate(answer, invocation);

        verifyZeroInteractions(reporter);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testValidate_Exception_InvalidCheckedException() {
        Reporter reporter = mock(Reporter.class);
        AnswersValidator answersValidator = new AnswersValidator(reporter);

        Answer<?> answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Exception.class);
        when(((ThrowsException) answer).getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(any())).thenReturn(false);

        answersValidator.validate(answer, invocation);

        verify(reporter).checkedExceptionInvalid(throwable);
        verifyNoMoreInteractions(reporter);
        verify(invocation).isValidException(any());
    }

}