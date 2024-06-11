package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.reporter = reporter;
 }

 @Test
 public void testValidateWithNullAnswer() {
 validateMethods(validator, null, new InvocationOnMock(null, null, null, null));
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateWithThrowsExceptionAnswer() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test Exception"));
 Invocation invocation = new InvocationOnMock(null, null, null, null);

 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateWithThrowsExceptionInvalidTypeAnswer() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new IllegalArgumentException("Test Exception"));
 Invocation invocation = new InvocationOnMock(null, null, null, null);

 validateMethods(validator, answer, invocation);
 verify(reporter).checkedExceptionInvalid(new IllegalArgumentException("Test Exception"));
 }

 @Test
 public void testValidateWithReturnsAnswer() {
 Returns answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn(1L);
 Invocation invocation = new InvocationOnMock(null, null, null, null);

 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateWithReturnsAnswerVoidMethod() {
 Returns answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn(null);
 Invocation invocation = new InvocationOnMock(null, null, null, "voidMethod()");

 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateWithReturnsAnswerInvalidReturnType() {
 Returns answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn("testString");
 Invocation invocation = new InvocationOnMock(null, null, null, "getIntMethod()");

 validateMethods(validator, answer, invocation);
 verify(reporter).wrongTypeOfReturnValue("int", "java.lang.String", "getIntMethod()");
 }

 @Test
 public void testValidateWithReturnsAnswerNullAndPrimitiveMethod() {
 Returns answer = mock(Returns.class);
 when(answer.getReturnValue()).thenReturn(null);
 Invocation invocation = new InvocationOnMock(null, null, null, "getBooleanMethod()");

 validateMethods(validator, answer, invocation);
 verify(reporter).wrongTypeOfReturnValue("boolean", "null", "getBooleanMethod()");
 }

 @Test
 public void testValidateWithDoesNothingAnswer() {
 DoesNothing answer = mock(DoesNothing.class);
 Invocation invocation = new InvocationOnMock(null, null, null, "voidMethod()");

 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateWithDoesNothingAnswerNonVoidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 Invocation invocation = new InvocationOnMock(null, null, null, "getIntMethod()");

 validateMethods(validator, answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 private void validateMethods(AnswersValidator validator, Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 }

}