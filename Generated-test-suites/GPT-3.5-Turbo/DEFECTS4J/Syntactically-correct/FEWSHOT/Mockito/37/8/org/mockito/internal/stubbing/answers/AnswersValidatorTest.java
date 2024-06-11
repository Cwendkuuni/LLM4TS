package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {
    private AnswersValidator answersValidator;
    private Invocation invocation;
    private Reporter reporter;

    @Before
    public void setUp() {
        answersValidator = new AnswersValidator();
        invocation = mock(Invocation.class);
        reporter = mock(Reporter.class);
        answersValidator.reporter = reporter;
    }

    @Test
    public void testValidate_DoNothing_WithVoidInvocation() {
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(mock(DoesNothing.class), invocation);
        verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_DoNothing_WithNonVoidInvocation() {
        when(invocation.isVoid()).thenReturn(false);
        answersValidator.validate(mock(DoesNothing.class), invocation);
        verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
    }

    @Test
    public void testValidate_ReturnValue_WithVoidInvocation() {
        when(invocation.isVoid()).thenReturn(true);
        answersValidator.validate(mock(Returns.class), invocation);
        verify(reporter).cannotStubVoidMethodWithAReturnValue();
    }

    @Test
    public void testValidate_ReturnValue_WithNullReturnValueAndPrimitiveInvocation() {
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        Returns returnValueAnswer = mock(Returns.class);
        when(returnValueAnswer.returnsNull()).thenReturn(true);
        answersValidator.validate(returnValueAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue(any(), eq("null"), any());
    }

    @Test
    public void testValidate_ReturnValue_WithNonVoidInvocationAndValidReturnType() {
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(true);
        Returns returnValueAnswer = mock(Returns.class);
        when(returnValueAnswer.returnsNull()).thenReturn(false);
        when(returnValueAnswer.getReturnType()).thenReturn(Object.class);
        answersValidator.validate(returnValueAnswer, invocation);
        verify(reporter, never()).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void testValidate_ReturnValue_WithNonVoidInvocationAndInvalidReturnType() {
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(any())).thenReturn(false);
        Returns returnValueAnswer = mock(Returns.class);
        when(returnValueAnswer.returnsNull()).thenReturn(false);
        when(returnValueAnswer.getReturnType()).thenReturn(Object.class);
        answersValidator.validate(returnValueAnswer, invocation);
        verify(reporter).wrongTypeOfReturnValue(any(), any(), any());
    }

    @Test
    public void testValidate_Exception_WithNullThrowable() {
        ThrowsException exceptionAnswer = mock(ThrowsException.class);
        when(exceptionAnswer.getThrowable()).thenReturn(null);
        answersValidator.validate(exceptionAnswer, invocation);
        verify(reporter).cannotStubWithNullThrowable();
    }

    @Test
    public void testValidate_Exception_WithRuntimeException() {
        Throwable throwable = mock(RuntimeException.class);
        ThrowsException exceptionAnswer = mock(ThrowsException.class);
        when(exceptionAnswer.getThrowable()).thenReturn(throwable);
        answersValidator.validate(exceptionAnswer, invocation);
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_WithError() {
        Throwable throwable = mock(Error.class);
        ThrowsException exceptionAnswer = mock(ThrowsException.class);
        when(exceptionAnswer.getThrowable()).thenReturn(throwable);
        answersValidator.validate(exceptionAnswer, invocation);
        verify(reporter, never()).checkedExceptionInvalid(any());
    }

    @Test
    public void testValidate_Exception_WithCheckedException() {
        Throwable throwable = mock(Exception.class);
        ThrowsException exceptionAnswer = mock(ThrowsException.class);
        when(exceptionAnswer.getThrowable()).thenReturn(throwable);
        answersValidator.validate(exceptionAnswer, invocation);
        verify(reporter).checkedExceptionInvalid(any());
    }
}