package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.stubning.Answers;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();

 @Test
 public void testValidate_whenAnswerIsThrowsException_thenExceptionIsValid() {
     Answer<String> answer = Answers.throwing(new RuntimeException("Test Exception"));
     Invocation invocation = Mockito.mock(Invocation.class);
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_whenAnswerIsReturns_thenReturnValueIsValid() {
     Answer<String> answer = Answers.returning("Test Value");
     Invocation invocation = Mockito.mock(Invocation.class);
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_whenAnswerIsDoesNothing_thenMethodIsVoid() {
     Answer<?> answer = Answers.doNothing();
     Invocation invocation = Mockito.mock(Invocation.class);
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_whenMethodIsNotVoid_thenErrorIsThrown() {
     Answer<Void> answer = Answers.doNothing();
     Invocation invocation = Mockito.mock(Invocation.class);
     Mockito.when(invocation.isVoid()).thenReturn(false);
     validator.validateDoNothing((DoesNothing) answer, invocation);
 }

 @Test
 public void testValidateReturnValue_whenInvocationReturnsVoid_thenErrorIsNotThrown() {
     Answer<String> answer = Answers.returning(null);
     Invocation invocation = Mockito.mock(Invocation.class);
     Mockito.when(invocation.isVoid()).thenReturn(true);
     validator.validateReturnValue((Returns) answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_whenAnswerReturnsNullAndInvocationReturnsPrimitive_thenErrorIsThrown() {
     Answer<String> answer = Answers.returning(null);
     Invocation invocation = Mockito.mock(Invocation.class);
     Mockito.when(invocation.isVoid()).thenReturn(false);
     Mockito.when(invocation.isValidReturnType(Mockito.any())).thenReturn(true);
     Mockito.when(invocation.returnsPrimitive()).thenReturn(true);
     validator.validateReturnValue((Returns) answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_whenAnswerDoesNotReturnNullAndInvocationReturnsInvalidType_thenErrorIsThrown() {
     Answer<String> answer = Answers.returning("Test Value");
     Invocation invocation = Mockito.mock(Invocation.class);
     Mockito.when(invocation.isVoid()).thenReturn(false);
     Mockito.when(invocation.isValidReturnType(Mockito.eq(String[].class))).thenReturn(false);
     validator.validateReturnValue((Returns) answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_whenAnswerHasNullThrowable_thenErrorIsThrown() {
     Answer<String> answer = Answers.throwing(null);
     Invocation invocation = Mockito.mock(Invocation.class);
     validator.validateException((ThrowsException) answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_whenInvocationDoesNotSupportGivenExceptionType_thenErrorIsThrown() {
     Answer<String> answer = Answers.throwing(new IllegalArgumentException("Test Exception"));
     Invocation invocation = Mockito.mock(Invocation.class);
     Mockito.when(invocation.isValidException(Mockito.eq(IllegalArgumentException.class))).thenReturn(false);
     validator.validateException((ThrowsException) answer, invocation);
 }
}