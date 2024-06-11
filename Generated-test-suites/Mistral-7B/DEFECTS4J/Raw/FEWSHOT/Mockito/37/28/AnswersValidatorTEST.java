package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test
 public void testValidate_nullAnswerAndInvocation() {
 validator.validate(null, null);
 // No exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_nullAnswer() {
 validator.validate(null, mock(Invocation.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_nullInvocation() {
 validator.validate(mock(Answer.class), null);
 }

 @Test
 public void testValidate_noAnswerOrInvocationTypeMismatch() {
 validator.validate(mock(Answer.class), mock(Object.class));
 // No exception should be thrown
 }

 @Test
 public void testValidate_returnsAnswer() {
 Answer<String> answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn("foo");
 Invocation invocation = mock(Invocation.class);
 when(invocation.getMethodName()).thenReturn("testMethod");
 when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(true);
 validator.validate(answer, invocation);
 // No exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_returnsAnswerWithInvalidReturnType() {
 Answer<String> answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn("foo");
 Invocation invocation = mock(Invocation.class);
 when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);
 validator.validate(answer, invocation);
 reporter.cannotStubWithInvalidReturnType(invocation.getMethodName(), answer.getReturnType().getClass().getName());
 verify(reporter).cannotStubWithInvalidReturnType(eq(invocation.getMethodName()), eq(answer.getReturnType().getClass().getName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_throwsAnswerWithNullThrowable() {
 Answer<String> answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(null);
 Invocation invocation = mock(Invocation.class);
 validator.validate(answer, invocation);
 reporter.cannotStubWithNullThrowable();
 verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidate_throwsAnswerWithRuntimeExceptionOrError() {
 Answer<String> answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenThrow(new RuntimeException("Test"));
 Invocation invocation = mock(Invocation.class);
 validator.validate(answer, invocation);
 // No exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_throwsAnswerWithInvalidExceptionType() {
 Answer<String> answer = mock(ThrowsException.class);
 Throwable throwable = new IllegalStateException("Test");
 when(answer.getThrowable()).thenThrow(throwable);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isValidException(throwable)).thenReturn(false);
 validator.validate(answer, invocation);
 reporter.checkedExceptionInvalid(throwable);
 verify(reporter).checkedExceptionInvalid(eq(throwable));
 }

 @Test
 public void testValidate_doesNothingAnswer() {
 Answer<Void> answer = mock(DoesNothing.class);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 validator.validate(answer, invocation);
 // No exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidate_doesNothingAnswerWithNonVoidInvocation() {
 Answer<Void> answer = mock(DoesNothing.class);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 validator.validate(answer, invocation);
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }
}