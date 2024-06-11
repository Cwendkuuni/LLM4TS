package org.mockito.internal.stubbing.answers;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Test
 public void testValidate_AnswerIsThrowsException_ValidException() {
     Invocation invocation = mock(Invocation.class);
     ThrowsException answer = mock(ThrowsException.class);
     answer.setThrowable(new RuntimeException("Test Exception"));

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsThrowsException_InvalidException() {
     Invocation invocation = mock(Invocation.class);
     ThrowsException answer = mock(ThrowsException.class);
     answer.setThrowable(new Exception("Test Exception"));

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_AnswerIsReturns_ValidReturnValue() {
     Invocation invocation = mock(Invocation.class);
     Returns answer = mock(Returns.class);
     answer.setReturnValue("Test Value");

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsReturns_InvalidReturnValue_VoidMethod() {
     Invocation invocation = mock(Invocation.class);
     Returns answer = mock(Returns.class);
     answer.setReturnValue("Test Value");

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);

     // The following line will throw an AssertionError since the method is void
     assertEquals("Test Value", invocation.getReturnValue());
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsReturns_InvalidReturnValue_InvalidType() {
     Invocation invocation = mock(Invocation.class);
     Returns answer = mock(Returns.class);
     answer.setReturnValue(new Object());

     when(invocation.getReturnType()).thenReturn(int.class);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_AnswerIsDoesNothing_ValidMethod() {
     Invocation invocation = mock(Invocation.class);
     DoesNothing answer = mock(DoesNothing.class);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsDoesNothing_InvalidMethod() {
     Invocation invocation = mock(Invocation.class);
     DoesNothing answer = mock(DoesNothing.class);

     when(invocation.isVoid()).thenReturn(false);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }
}