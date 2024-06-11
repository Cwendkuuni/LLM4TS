package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.Runners;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(Runners.JUnit4.class)
public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test(expected = AssertionError.class)
 public void testValidateNullAnswer() {
  validator.validate(null, new InvocationOnMock(new Object(), new String[0], new Object[0], null));
 }

 @Test
 public void testValidateThrowsException() {
  Throwable throwable = new RuntimeException("Test Exception");
  ThrowsException answer = new ThrowsException(throwable);
  validator.validate(answer, new InvocationOnMock(new Object(), new String[0], new Object[0], null));
  verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateThrowsInvalidException() {
  Throwable throwable = new IllegalArgumentException("Test Exception");
  ThrowsException answer = new ThrowsException(throwable);
  validator.validate(answer, new InvocationOnMock(new Object(), new String[0], new Object[0], null));
 }

 @Test
 public void testValidateReturns() {
  Returns answer = Mockito.mock(Returns.class);
  when(answer.getReturnValue()).then(123);
  Invocation invocation = new InvocationOnMock(new Object(), new String[0], new Object[0], 123);
  validator.validate(answer, invocation);
  verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNullAndVoidInvocation() {
  Returns answer = Mockito.mock(Returns.class);
  validator.validate(answer, new InvocationOnMock(new Object(), new String[0], "void", null));
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNonValidTypeAndVoidInvocation() {
  Returns answer = Mockito.mock(Returns.class);
  when(answer.getReturnType()).then(String.class);
  validator.validate(answer, new InvocationOnMock(new Object(), new String[0], "void", null));
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNullAndNonVoidInvocation() {
  Returns answer = Mockito.mock(Returns.class);
  when(answer.getReturnValue()).then(null);
  Invocation invocation = new InvocationOnMock(new Object(), new String[0], new Object[0], new Object());
  validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNonValidTypeAndNonVoidInvocation() {
  Returns answer = Mockito.mock(Returns.class);
  when(answer.getReturnType()).then(int[].class);
  Invocation invocation = new InvocationOnMock(new Object(), new String[0], new Object[0], new Object());
  validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoesNothing() {
  DoesNothing answer = Mockito.mock(DoesNothing.class);
  Invocation invocation = new InvocationOnMock(new Object(), new String[0], new Object[0], null);
  validator.validate(answer, invocation);
  if (invocation.isVoid()) {
   verifyZeroInteractions(reporter);
  } else {
   verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
  }
 }

 @Test
 public void testValidateDoesNothingVoidInvocation() {
  DoesNothing answer = Mockito.mock(DoesNothing.class);
  Invocation invocation = new InvocationOnMock(new Object(), new String[0], "void", null);
  validator.validate(answer, invocation);
  verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoesNothingNonVoidInvocation() {
  DoesNothing answer = Mockito.mock(DoesNothing.class);
  validator.validate(answer, new InvocationOnMock(new Object(), new String[0], new Object[0], new Object()));
 }
}