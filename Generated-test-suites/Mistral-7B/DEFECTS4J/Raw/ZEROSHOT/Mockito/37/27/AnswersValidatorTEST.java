package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private InvocationOnMock invocation;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 invocation = mock(InvocationOnMock.class);
 }

 @Test(expected = MockitoException.class)
 public void testValidateDoNothingVoidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoNothingNonVoidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 when(invocation.isVoid()).thenReturn(false);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsNullVoidMethod() {
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(true);
 when(invocation.isVoid()).thenReturn(true);
 validator.validate(answer, invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnsNullNonPrimitiveMethod() {
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(true);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(String.class)).thenReturn(false);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsNonNullNonPrimitiveMethod() {
 Returns answer = mock(Returns.class);
 String returnType = String.class.getName();
 when(answer.getReturnType()).thenReturn(returnType);
 when(invocation.isValidReturnType(returnType)).thenReturn(true);
 validator.validate(answer, invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnsNonPrimitiveNullValue() {
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(true);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(String.class)).thenReturn(true);
 when(answer.getReturnType()).thenReturn(String.class);
 when(answer.getAnswer()).thenReturn(null);
 validator.validate(answer, invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnsPrimitiveNullValue() {
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(false);
 when(answer.getReturnType()).thenReturn(int.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(int.class)).thenReturn(true);
 when(answer.getAnswer()).thenReturn(null);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsPrimitiveValue() {
 Returns answer = mock(Returns.class);
 when(answer.getReturnType()).thenReturn(int.class);
 when(invocation.isValidReturnType(int.class)).thenReturn(true);
 when(answer.getAnswer()).thenReturn(42);
 validator.validate(answer, invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateThrowsNullThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(null);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsException() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test exception"));
 validator.validate(answer, invocation);
 }

 @Test(expected = MockitoException.class)
 public void testValidateThrowsInvalidException() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new IllegalArgumentException("Test exception"));
 when(invocation.isValidException(IllegalArgumentException.class)).thenReturn(false);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsRuntimeExceptionOrError() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test exception"));
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsCheckedException() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new MockitoException("Test exception"));
 validator.validate(answer, invocation);
 }

 @Test
 public void testNoMoreInteractions() {
 AnswersValidator spyValidator = spy(validator);
 Answer<?> answer = mock(Answer.class);
 Invocation invocation = mock(InvocationOnMock.class);
 spyValidator.validate(answer, invocation);
 verifyNoMoreInteractions(answer);
 }

 @Test
 public void testZeroInteractions() {
 AnswersValidator spyValidator = spy(validator);
 Answer<?> answer = mock(Answer.class);
 Invocation invocation = mock(InvocationOnMock.class);
 spyValidator.validate(answer, invocation);
 verifyZeroInteractions(answer, invocation);
 }

 @Test
 public void testAnswerIsValid() {
 AnswersValidator spyValidator = spy(validator);
 Answer<?> answer = mock(Answer.class);
 Invocation invocation = mock(InvocationOnMock.class);
 spyValidator.validate(answer, invocation);
 verifyNoMoreInteractions(answer);
 verifyZeroInteractions(invocation);
 }
}