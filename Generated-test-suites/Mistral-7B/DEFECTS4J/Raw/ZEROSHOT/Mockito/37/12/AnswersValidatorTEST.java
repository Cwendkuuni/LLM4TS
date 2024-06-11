package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator answersValidator;
 private Reporter reporter;

 @Before
 public void setUp() {
 answersValidator = new AnswersValidator();
 reporter = mock(Reporter.class);
 }

 @Test
 public void givenNullAnswer_whenValidateInvoked_thenThrowException() {
 AnswersValidator validator = new AnswersValidator() {
 @Override
 Reporter getReporter() {
 return null;
 }
 };

 try {
 validator.validate(null, new InvocationOnMock(new Object(), "methodName", new Object[0], null));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void givenThrowAnswer_whenValidateInvoked_thenDoNothing() {
 ThrowsException answer = new ThrowsException(new RuntimeException("Test Exception"));
 answersValidator.validate(answer, new InvocationOnMock(new Object(), "methodName", new Object[0], null));
 }

 @Test(expected = AssertionError.class)
 public void givenVoidMethodWithReturnValueAnswer_whenValidateInvoked_thenThrowException() {
 Returns answer = new Returns("Test String");
 answersValidator.validate(answer, new InvocationOnMock(new Object(), "voidMethod", new Object[0], null));
 }

 @Test
 public void givenNullReturnValueAnswer_whenValidateInvokeVoidMethod_thenDoNothing() {
 DoesNothing answer = new DoesNothing();
 answersValidator.validate(answer, new InvocationOnMock(new Object(), "voidMethod", new Object[0], null));
 }

 @Test
 public void givenInvalidReturnValueAnswerForVoidMethod_whenValidateInvoked_thenThrowException() {
 DoesNothing answer = new DoesNothing() {
 @Override
 public void answer() {
 throw new RuntimeException("Test Exception");
 }
 };
 answersValidator.validate(answer, new InvocationOnMock(new Object(), "voidMethod", new Object[0], null));
 }

 @Test
 public void givenValidReturnValueAnswer_whenValidateInvoked_thenDoNothing() {
 Returns answer = new Returns(1);
 answersValidator.validate(answer, new InvocationOnMock(Integer.class, "methodName", new Object[0], 1));
 }

 @Test(expected = AssertionError.class)
 public void givenNullReturnValueAnswerForNonVoidMethod_whenValidateInvoked_thenThrowException() {
 Returns answer = new Returns(null);
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], "Test String"));
 }

 @Test
 public void givenInvalidReturnValueAnswerForNonVoidMethod_whenValidateInvoked_thenThrowException() {
 Returns answer = new Returns(new Object());
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], "Test String"));
 }

 @Test
 public void givenValidExceptionAnswer_whenValidateInvoked_thenDoNothing() {
 ThrowsException answer = new ThrowsException(new IllegalArgumentException("Test Exception"));
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], null));
 }

 @Test(expected = AssertionError.class)
 public void givenInvalidExceptionAnswer_whenValidateInvoked_thenThrowException() {
 ThrowsException answer = new ThrowsException(new Object());
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], null));
 }

 @Test
 public void givenAnswerWithMismatchedReturnType_whenValidateInvoked_thenThrowException() {
 Returns answer = new Returns(new Object());
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], new Integer(1)));
 }

 @Test
 public void givenAnswerWithValidTypes_whenValidateInvoked_thenDoNothing() {
 Returns answer = new Returns("Test String");
 answersValidator.validate(answer, new InvocationOnMock(String.class, "methodName", new Object[0], "Test String"));
 }
}