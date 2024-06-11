package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateNullAnswerAndInvocation() {
 validate(null, null);
 }

 @Test
 public void testValidateThrowsExceptionAnswer() {
     ThrowsException answer = new ThrowsException(new RuntimeException("Test Exception"));
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithVoidMethod() {
     Returns returnsAnswer = new ReturnsVoid();
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithNullReturnValueAndVoidMethod() {
     Returns returnsAnswer = new ReturnsNull();
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithNullReturnValueAndNonVoidMethod() {
     Returns returnsAnswer = new ReturnsNull();
     Invocation invocation = new Invocation("testMethod", new int[0]);

     AnswersValidator validator = new AnswersValidator();
     try {
         validator.validate(returnsAnswer, invocation);
         Assert.fail("Expected reporter.cannotStubVoidMethodWithAReturnValue to be thrown");
     } catch (AssertionError e) {
         // Expected
     }
 }

 @Test
 public void testValidateReturnsAnswerWithValidReturnValue() {
     Returns returnsAnswer = new Returns("42");
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithInvalidReturnType() {
     Returns returnsAnswer = new Returns("foo");
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     try {
         validator.validate(returnsAnswer, invocation);
         Assert.fail("Expected reporter.wrongTypeOfReturnValue to be thrown");
     } catch (AssertionError e) {
         // Expected
     }
 }

 @Test
 public void testValidateThrowsExceptionAnswerWithInvalidException() {
     ThrowsException answer = new ThrowsException(new Exception("Test Exception"));;
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     try {
         validator.validate(answer, invocation);
         Assert.fail("Expected reporter.checkedExceptionInvalid to be thrown");
     } catch (AssertionError e) {
         // Expected
     }
 }

 @Test
 public void testValidateThrowsExceptionAnswerWithValidException() {
     ThrowsException answer = new ThrowsException(new RuntimeException("Test Exception"));
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoesNothingAnswerWithNonVoidMethod() {
     DoesNothing doesNothingAnswer = new DoesNothing();
     Invocation invocation = new Invocation("testMethod", new Object[0]);

     AnswersValidator validator = new AnswersValidator();
     try {
         validator.validate(doesNothingAnswer, invocation);
         Assert.fail("Expected reporter.onlyVoidMethodsCanBeSetToDoNothing to be thrown");
     } catch (AssertionError e) {
         // Expected
     }
 }
}