package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test(expected = AssertionError.class)
 public void testValidate_nullAnswer() {
        validator.validate(null, mock(Invocation.class));
 }

 @Test
 public void testValidate_validDoesNothing() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_nonVoidMethod() {
        DoesNothing answer = mock(DoesNothing.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        validator.validateDoNothing(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_voidMethodWithReturnValue() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(true);
        validator.validateReturnValue(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_nullReturnValueWithPrimitiveMethod() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.returnsPrimitive()).thenReturn(true);
        validator.validateReturnValue(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_wrongReturnType() {
        Returns answer = mock(Returns.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.isVoid()).thenReturn(false);
        when(invocation.isValidReturnType(eq(String.class))).thenReturn(false);
        validator.validateReturnValue(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_nullThrowable() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        validator.validateException(answer, invocation);
 }

 @Test
 public void testValidateException_runtimeExceptionOrError() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        ThrowsException thrownException = mock(ThrowsException.class);
        when(answer.getThrowable()).thenReturn(thrownException);
        when(thrownException instanceof RuntimeException).thenReturn(true);
        validator.validateException(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalidCheckedException() {
        ThrowsException answer = mock(ThrowsException.class);
        Invocation invocation = mock(Invocation.class);
        Throwable throwable = mock(Throwable.class);
        when(answer.getThrowable()).thenReturn(throwable);
        when(invocation.isValidException(throwable)).thenReturn(false);
        validator.validateException(answer, invocation);
 }

}