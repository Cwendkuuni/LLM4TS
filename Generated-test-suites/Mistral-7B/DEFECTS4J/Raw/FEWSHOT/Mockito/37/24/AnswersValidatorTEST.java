package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test
 public void testValidateNullAnswerAndInvocation() {
 validateNullAnswerAndInvocation(null, null);
 }

 @Test
 public void testValidateEmptyAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.EmptyAnswer(), new Invocation("methodName", new Object[0]));
 }

 @Test
 public void testValidateVoidAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.DoNothing(), new Invocation("methodName", new Object[0], null));
 }

 @Test
 public void testValidateStringReturnValueAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.StringReturnValueAnswer("result"), new Invocation("methodName", new Object[0], String.class));
 }

 @Test
 public void testValidateWrongStringReturnValueAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.StringReturnValueAnswer("result"), new Invocation("methodName", new Object[0], Integer.class));
 }

 @Test
 public void testValidateVoidMethodWithReturnAnswer() {
 validateAnswerAndInvocation(new AnswersValidator.Returns("result"), new Invocation("methodName", new Object[0]));
 }

 @Test
 public void testValidatePrimitiveTypeWithNullReturnAnswer() {
 validateAnswerAndInvocation(new AnswersValidator.Returns(null), new Invocation("methodName", new Object[0], int.class));
 }

 @Test
 public void testValidateWrongTypeOfReturnValueAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.Returns("result"), new Invocation("methodName", new Object[0], String.class));
 }

 @Test
 public void testValidateThrowsExceptionAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.ThrowsException(new RuntimeException("message")), new Invocation("methodName", new Object[0]));
 }

 @Test
 public void testValidateWrongTypeOfThrowsExceptionAnswerAndInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.ThrowsException(new IllegalArgumentException("message")), new Invocation("methodName", new Object[0], String.class));
 }

 @Test
 public void testValidateDoesNothingAnswerAndVoidInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.DoesNothing(), new Invocation("methodName", new Object[0], null));
 }

 @Test
 public void testValidateDoesNothingAnswerAndNonVoidInvocation() {
 validateAnswerAndInvocation(new AnswersValidator.DoesNothing(), new Invocation("methodName", new Object[0], String.class));
 }

 private void validateAnswerAndInvocation(Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 private void validateNullAnswerAndInvocation(Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateExceptionWithNullAnswer() {
 validateExceptionWithNullAnswerAndInvocation(new Invocation("methodName", new Object[0]));
 }

 @Test
 public void testValidateExceptionWithInvalidAnswer() {
 validateExceptionWithNullAnswerAndInvocation(new Invocation("methodName", new Object[0], "String.class"));
 }

 @Test
 public void testValidateExceptionWithValidAnswer() {
 validateExceptionWithNullAnswerAndInvocation(new Invocation("methodName", new Object[0], Integer.class));
 }

 private void validateExceptionWithNullAnswerAndInvocation(Invocation invocation) {
 validator.validate(null, invocation);
 verify(reporter).cannotStubWithNullAnswer();
 }

 @Test
 public void testValidateExceptionWithInvalidThrowableTypeAnswer() {
 validateExceptionWithInvalidThrowableTypeAnswerAndInvocation(new AnswersValidator.ThrowsException(new IllegalStateException("message")), new Invocation("methodName", new Object[0], Integer.class));
 }

 @Test
 public void testValidateExceptionWithValidThrowableTypeAnswer() {
 validateExceptionWithInvalidThrowableTypeAnswerAndInvocation(new AnswersValidator.ThrowsException(new IllegalArgumentException("message")), new Invocation("methodName", new Object[0], Integer.class));
 }

 private void validateExceptionWithInvalidThrowableTypeAnswerAndInvocation(Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 verify(reporter).checkedExceptionInvalid(answer.getThrowable());
 }

 @Test
 public void testValidateExceptionWithValidThrowableTypeAnswerAndInvocation() {
 validateExceptionWithValidThrowableTypeAnswerAndInvocation(new AnswersValidator.ThrowsException(new IllegalArgumentException("message")), new Invocation("methodName", new Object[0], Integer.class));
 }

 private void validateExceptionWithValidThrowableTypeAnswerAndInvocation(Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }
}