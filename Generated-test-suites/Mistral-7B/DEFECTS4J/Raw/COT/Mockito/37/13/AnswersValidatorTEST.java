package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.reporter = reporter;
 }

 @Test
 public void testValidate_nullAnswer_throwsException() {
 Invocation invocation = mock(InvocationOnMock.class);
 Answer<?> nullAnswer = null;

 validate(validator, nullAnswer, invocation);

 verify(reporter).cannotStubWithNullAnswer();
 }

 @Test
 public void testValidate_throwsExceptionAnswer_noExceptionThrown() {
 Invocation invocation = mock(InvocationOnMock.class);
 String methodName = "testMethod";

 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable().printStackTrace(); // print stack trace to make it different every time

 validate(validator, answer, invocation);

 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidate_throwsExceptionAnswer_validExceptionThrown() {
 Invocation invocation = mock(InvocationOnMock.class);
 String methodName = "testMethod";

 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable().printStackTrace(); // print stack trace to make it different every time

 Invocation invocationWithValidException = mock(InvocationOnMock.class);
 Invocation invocationWithInvalidException = mock(InvocationOnMock.class);

 verify(reporter, times(1)).checkedExceptionValid(ArgumentMatchers.isA(RuntimeException.class));

 validate(validator, answer, invocation);
 validate(validator, answer, invocationWithValidException);
 validate(validator, answer, invocationWithInvalidException);

 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_returnNullAnswer_invalidPrimitiveTypeReturn_throwsException() {
 Invocation invocation = mock(InvocationOnMock.class);
 int expectedType = 5;

 Returns answer = mock(Returns.class);
 answer.returnsNull();

 validate(validator, answer, invocation);

 verify(reporter).wrongTypeOfReturnValue("int", "null", "testMethod");
 }

 @Test
 public void testValidate_returnNullAnswer_validTypeReturn_noExceptionThrown() {
 Invocation invocation = mock(InvocationOnMock.class);
 String expectedType = "String";

 Returns answer = mock(Returns.class);
 answer.returnsNull();

 validate(validator, answer, invocation);

 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidate_nonNullReturnAnswer_invalidTypeReturn_throwsException() {
 Invocation invocation = mock(InvocationOnMock.class);
 int expectedType = 5;
 String wrongType = "String";

 Returns answer = mock(Returns.class);
 answer.getReturnValue().getClass().setName("WrongReturnType");
 answer.getReturnValue().setClass(String.class);

 validate(validator, answer, invocation);

 verify(reporter).wrongTypeOfReturnValue("int", "WrongReturnType", "testMethod");
 }

 @Test
 public void testValidate_nonNullReturnAnswer_validTypeReturn_noExceptionThrown() {
 Invocation invocation = mock(InvocationOnMock.class);
 int expectedType = 5;

 Returns answer = mock(Returns.class);
 answer.getReturnValue().setIntValue(expectedType);

 validate(validator, answer, invocation);

 verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidate_doNothingAnswer_methodNotVoid_throwsException() {
 Invocation invocation = mock(InvocationOnMock.class);

 DoesNothing answer = mock(DoesNothing.class);

 validate(validator, answer, invocation);

 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_doNothingAnswer_methodVoid_noExceptionThrown() {
 Invocation invocation = mock(InvocationOnMock.class);

 DoesNothing answer = mock(DoesNothing.class);

 validate(validator, answer, invocation);

 verifyZeroInteractions(reporter);
}

 private void validate(AnswersValidator validator, Answer<?> answer, Invocation invocation) {
 validator.validate(answer, invocation);
 }

}