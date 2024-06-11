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
 public void testValidateAnswerIsNullAndInvocationIsVoid() {
 Invocation invocation = new Invocation(null, null, null, null, null, null, null);
 validate(null, invocation);
 }

 @Test
 public void testValidateAnswerIsThrowsExceptionAndInvocationIsVoid() {
 validateException((ThrowsException) Mockito.mock(ThrowsException.class), new Invocation(null, null, null, null, null, null, null));
 }

 @Test
 public void testValidateAnswerIsThrowsExceptionAndInvocationIsNotVoid() {
 Invocation invocation = new Invocation(new Object[0].getClass(), "testMethod", new Class[0], new Object[0], new Object[0], null, new Object[0]);
 validateException((ThrowsException) Mockito.mock(ThrowsException.class), invocation);
 }

 @Test
 public void testValidateAnswerIsReturnsNullAndInvocationReturnsPrimitive() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(byte.class, "testMethod", byte[].class, null, null, null, new byte[0]));
 }

 @Test
 public void testValidateAnswerIsReturnsNullAndInvocationReturnsObject() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(String.class, "testMethod", String.class, null, null, null, "test"));
 }

 @Test
 public void testValidateAnswerIsReturnsNullAndInvocationIsVoidAndReturnsNull() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(void.class, "testMethod", null, null, null, null, null));
 }

 @Test
 public void testValidateAnswerIsReturnsNotNullAndInvocationReturnsPrimitive() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(int.class, "testMethod", int.class, null, null, null, 1));
 }

 @Test
 public void testValidateAnswerIsReturnsNotNullAndInvocationReturnsObject() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(String.class, "testMethod", String.class, null, null, null, "test"));
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(String[].class, "testMethod", String[].class, null, null, null, new String[]{"test"}));
 }

 @Test
 public void testValidateAnswerIsReturnsNotNullAndInvocationIsVoidAndReturnsNotNull() {
 validateReturnValue((Returns) Mockito.mock(Returns.class), new Invocation(void.class, "testMethod", null, null, null, null, null));
 }

 @Test
 public void testValidateAnswerIsDoesNothingAndInvocationIsVoid() {
 validateDoNothing((DoesNothing) Mockito.mock(DoesNothing.class), new Invocation(void.class, "testMethod", null, null, null, null, null));
 }

 @Test
 public void testValidateAnswerIsDoesNothingAndInvocationIsNotVoid() {
 Invocation invocation = new Invocation(new Object[0].getClass(), "testMethod", new Class[0], null, null, null, new Object[0]);
 validateDoNothing((DoesNothing) Mockito.mock(DoesNothing.class), invocation);
 }

 @Test
 public void testValidateExceptionWithNullThrowable() {
 validateException(null, new Invocation(null, null, null, null, null, null, null));
 }

 @Test
 public void testValidateExceptionWithInvalidThrowable() {
 Invocation invocation = new Invocation(int.class, "testMethod", int.class, null, null, null, 1);
 validateException(Mockito.mock(RuntimeException.class), invocation);
 }

 private void validate(Answer<?> answer, Invocation invocation) {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(answer, invocation);
 }

 private void validateDoNothing(DoesNothing answer, Invocation invocation) {
 AnswersValidator validator = new AnswersValidator();
 validator.validateDoNothing(answer, invocation);
 }

 private void validateException(ThrowsException answer, Invocation invocation) {
 AnswersValidator validator = new AnswersValidator();
 validator.validateException(answer, invocation);
 }

 private void validateReturnValue(Returns answer, Invocation invocation) {
 AnswersValidator validator = new AnswersValidator();
 validator.validateReturnValue(answer, invocation);
 }
}