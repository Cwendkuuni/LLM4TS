package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyThrows;
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

 @Test(expected = AssertionError.class)
 public void testValidate_nullAnswer() {
 validator.validate(null, new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]));
 }

 @Test
 public void testValidate_doesNothing() {
 Answer<?> doesNothingAnswer = mock(DoesNothing.class);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(doesNothingAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_voidMethodWithReturnValue() {
 Returns returnAnswer = mock(Returns.class);
 when(returnAnswer.getReturnValue()).thenReturn(1);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(returnAnswer, invocation);
 }

 @Test
 public void testValidate_validReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn("testString");
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(returnsAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_nullReturnValueAndVoidMethod() {
 Returns nullReturnAnswer = mock(Returns.class);
 when(nullReturnAnswer.getReturnValue()).thenReturn(null);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(nullReturnAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidReturnType() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnType()).thenReturn(String[].class);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(returnsAnswer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_voidMethodWithDoNothing() {
 DoesNothing doNothingAnswer = mock(DoesNothing.class);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(doNothingAnswer, invocation);
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 verifyThrows(AssertionError.class, () -> reporter.getMessage());
 }

 @Test(expected = AssertionError.class)
 public void testValidate_nonVoidMethodWithDoNothing() {
 DoesNothing doNothingAnswer = mock(DoesNothing.class);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 invocation.setReturnValue(1);
 validator.validate(doNothingAnswer, invocation);
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 verifyThrows(AssertionError.class, () -> reporter.getMessage());
 }

 @Test(expected = AssertionError.class)
 public void testValidate_nullThrowable() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 validator.validate(throwsAnswer, new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]));
 reporter.cannotStubWithNullThrowable();
 verifyThrows(AssertionError.class, () -> reporter.getMessage());
 }

 @Test
 public void testValidate_validException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 Throwable throwable = new IllegalStateException("Test exception");
 when(throwsAnswer.getThrowable()).thenReturn(throwable);
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(throwsAnswer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidExceptionType() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenReturn(new Exception("Test exception"));
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(throwsAnswer, invocation);
 reporter.checkedExceptionInvalid(new Exception("Test exception"));
 verifyThrows(AssertionError.class, () -> reporter.getMessage());
 }

 @Test(expected = AssertionError.class)
 public void testValidate_throwsRuntimeExceptionOrError() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenThrow(new RuntimeException("Test exception"));
 Invocation invocation = new InvocationOnMock(new Object(), new String[]{"testMethod"}, new Object[0]);
 validator.validate(throwsAnswer, invocation);
 // No verification necessary as the method does not throw an exception in this case
 }
}