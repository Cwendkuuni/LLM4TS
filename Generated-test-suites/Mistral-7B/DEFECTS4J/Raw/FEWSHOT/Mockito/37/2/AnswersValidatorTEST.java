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
 ValidateAnswers validator = new AnswersValidator();
 assertNull(validator.validate(null, null));
 }

 @Test
 public void testValidateNullAnswer() {
 AnswersValidator validator = new AnswersValidator();
 Invocation invocation = new Invocation(null, null, null, null, null);
 assertNull(validator.validate(null, invocation));
 }

 @Test
 public void testValidateNullInvocation() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new Returns<Object>(null);
 assertNull(validator.validate(answer, null));
 }

 @Test (expected = AssertionError.class)
 public void testValidateNullAnswerThrowsException() {
 AnswersValidator validator = new AnswersValidator();
 Invocation invocation = new Invocation(null, null, null, null, null);
 ThrowsException answer = new ThrowsException(new RuntimeException("test error"));
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoesNothing() {
 AnswersValidator validator = new AnswersValidator();
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(new DoesNothing(), invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateReturnsNullVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new Returns<Object>(null);
 Invocation invocation = new Invocation(null, null, null, null, null, new Class[]{}, new Object[]{});
 validator.validate(answer, invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateReturnsNotValidTypeNull() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new Returns<Object>(new Object());
 Invocation invocation = new Invocation(null, null, null, null, null, new Class[]{}, new Object[]{"int"});
 validator.validate(answer, invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateReturnsNotValidTypeWrongType() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new Returns<Object>("String");
 Invocation invocation = new Invocation(null, null, null, null, null, new Class[]{Integer.class}, null);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsValidType() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new Returns<Object>("test");
 Invocation invocation = new Invocation(null, null, null, null, null, new Class[]{String.class}, null);
 validator.validate(answer, invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateThrowsNull() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new ThrowsException(null);
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(answer, invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateThrowsRuntimeException() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new ThrowsException(new RuntimeException("test error"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsCheckedException() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new ThrowsException(new IllegalStateException("test error"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(answer, invocation);
 }

 @Test (expected = AssertionError.class)
 public void testValidateThrowsInvalidCheckedException() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new ThrowsException(new IOException("test error"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateThrowsValidException() {
 AnswersValidator validator = new AnswersValidator();
 Answer<Object> answer = new ThrowsException(new RuntimeException("test error"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 validator.validate(answer, invocation);
 }
}