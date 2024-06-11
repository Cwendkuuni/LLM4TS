package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Exceptions;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 reporter = Mockito.spy(new Reporter());
 validator.reporter = reporter;
 }

 @Test
 public void testValidateNullAnswer() {
 doNothing().when(validator).validate(null, ArgumentMatchers.any(Invocation.class));
 // Basic test for validate method with null answer
 validator.validate(null, Mockito.mock(Invocation.class));
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidateThrowsExceptionAnswer() {
 Answer<Object> answer = Mockito.answer(new ThrowsException(new RuntimeException("Test Exception"))) ;
 doNothing().when(validator).validateException(ArgumentMatchers.eq(answer), ArgumentMatchers.any(Invocation.class));
 // Basic test for validateException method
 validator.validate(answer, Mockito.mock(Invocation.class));
 verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateThrowsExceptionInvalidException() {
 Answer<Object> answer = Mockito.answer(new ThrowsException(new Object()));
 doThrow(new AssertionError("Test Error")).when(reporter).checkedExceptionInvalid(ArgumentMatchers.any(Throwable.class));
 // Test for validateException method with invalid exception
 validator.validate(answer, Mockito.mock(Invocation.class));
 }

 @Test
 public void testValidateReturnsAnswerVoidMethod() {
 Answer<Void> answer = Mockito.answer(new DoesNothing());
 doNothing().when(validator).validateDoNothing(ArgumentMatchers.eq(answer), ArgumentMatchers.any(Invocation.class));
 // Basic test for validateDoNothing method
 validator.validate(answer, Mockito.mock(Invocation.class));
 verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsAnswerNonVoidMethod() {
 Answer<Integer> answer = Mockito.answer(new Returns(1));
 doThrow(new AssertionError("Test Error")).when(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 // Test for validateDoNothing method with non-void method
 validator.validate(answer, Mockito.mock(Invocation.class));
 }

 @Test
 public void testValidateReturnsAnswerNullReturnValuePrimitiveType() {
 Answer<Integer> answer = Mockito.answer(new Returns(null));
 doNothing().when(validator).validateReturnValue(ArgumentMatchers.eq(answer), ArgumentMatchers.any(Invocation.class));
 // Basic test for validateReturnValue method with null return value and primitive type
 validator.validate(answer, Mockito.mock(Invocation.class, new ExpectedException(NullPointerException.class)));
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsAnswerNullReturnValueWrongType() {
 Answer<String> answer = Mockito.answer(new Returns((Object) null));
 doThrow(new AssertionError("Test Error")).when(reporter).wrongTypeOfReturnValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq("null"), ArgumentMatchers.anyString());
 // Test for validateReturnValue method with null return value and wrong type
 validator.validate(answer, Mockito.mock(Invocation.class));
 }

 @Test
 public void testValidateReturnsAnswerWrongTypeReturnValue() {
 Answer<String> answer = Mockito.answer(new Returns("Test String"));
 doNothing().when(validator).validateReturnValue(ArgumentMatchers.eq(answer), ArgumentMatchers.any(Invocation.class));
 // Basic test for validateReturnValue method with wrong type of return value
 validator.validate(answer, Mockito.mock(Invocation.class));
 verifyNoMoreInteractions(reporter);
}
}