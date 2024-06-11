package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private Reporter reporter = new Reporter();

 @Test
 public void testValidateNullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 Assert.assertNull(validator);
 }

 @Test
 public void testValidateThrowExceptionAnswerWithValidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Throwable throwable = new Throwable();
 ThrowsException throwsExceptionAnswer = new ThrowsException(throwable);
 Invocation invocation = new Invocation(null, null, null, null, null, null);
 validator.validate(throwsExceptionAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateThrowExceptionAnswerWithInvalidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Throwable throwable = new Throwable();
 ThrowsException throwsExceptionAnswer = new ThrowsException(throwable);
 Invocation invocation = new Invocation(null, null, null, null, null, "void method");
 validator.validate(throwsExceptionAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithValidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns("foo");
 Invocation invocation = new Invocation(null, null, null, null, null, null);
 validator.validate(returnsAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsAnswerWithVoidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns("foo");
 Invocation invocation = new Invocation(null, null, null, null, null, "void method");
 validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithNullReturnValueAndPrimitiveInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns(null);
 Invocation invocation = new Invocation(null, int.class, null, null, null, null);
 validator.validate(returnsAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsAnswerWithNullReturnValueAndNonPrimitiveInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns(null);
 Invocation invocation = new Invocation(null, String.class, null, null, null, null);
 validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateReturnsAnswerWithValidReturnValueAndInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns("foo");
 Invocation invocation = new Invocation(null, String.class, null, null, null, null);
 validator.validate(returnsAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsAnswerWithInvalidReturnTypeAndInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Returns returnsAnswer = new Returns(new Object());
 Invocation invocation = new Invocation(null, String.class, null, null, null, null);
 validator.validate(returnsAnswer, invocation);
 }

 @Test
 public void testValidateDoesNothingAnswerWithValidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 DoesNothing doesNothingAnswer = new DoesNothing();
 Invocation invocation = new Invocation(null, null, null, null, null, null);
 validator.validate(doesNothingAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoesNothingAnswerWithNonVoidInvocation() {
 AnswersValidator validator = new AnswersValidator();
 DoesNothing doesNothingAnswer = new DoesNothing();
 Invocation invocation = new Invocation(null, null, null, null, "returnValue", null);
 validator.validate(doesNothingAnswer, invocation);
 }
}