package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

public class AnswersValidatorTest {

 Reporter mockReporter = mock(Reporter.class);

 @Test
 public void testValidate_nullAnswer_throwsNullArgumentException() {
 Assert.Throws(NullPointerException.class, () -> new AnswersValidator(null, mockReporter).validate(mock(Answer.class), mock(Invocation.class)));
 }

 @Test
 public void testValidate_nullInvocation_throwsNullArgumentException() {
 Assert.Throws(NullPointerException.class, () -> new AnswersValidator(mock(Answer.class), null).validate(mock(Answer.class), mock(Invocation.class)));
 }

 @Test
 public void testValidate_answerInstancesThrowsException_callsValidateException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenReturn(new RuntimeException("Test Exception"));
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 verify(mockReporter).onlyRuntimeExceptionsCanBeThrown();
 }

 @Test
 public void testValidate_answerInstancesThrowsError_doesNotCallValidateException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenReturn(new IllegalStateException("Test Error"));
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 verify(mockReporter, never()).onlyRuntimeExceptionsCanBeThrown();
 }

 @Test
 public void testValidate_answerInstancesDoesNothing_callsValidateDoNothing() {
 DoesNothing doesNothingAnswer = mock(DoesNothing.class);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 new AnswersValidator(mockReporter).validate(doesNothingAnswer, invocation);
 verify(mockReporter, times(1)).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_answerInstancesDoesNothing_invocationIsNotVoid_doesNotCallValidateDoNothing() {
 DoesNothing doesNothingAnswer = mock(DoesNothing.class);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 new AnswersValidator(mockReporter).validate(doesNothingAnswer, invocation);
 verify(mockReporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_answerInstancesReturns_callsValidateReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn("Test Value");
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(eq(String.class))).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidate_answerInstancesReturnsNull_invocationReturnsPrimitive_callsValidateReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn(null);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(eq(int.class))).thenReturn(true);
 when(invocation.isValidReturnType(eq(String.class))).thenReturn(false);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter).wrongTypeOfReturnValue(eq("int"), eq("null"), anyString());
 }

 @Test
 public void testValidate_answerInstancesReturnsNull_invocationReturnsObject_callsValidateReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn(null);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(eq(String.class))).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidate_answerInstancesReturnsNotNull_invocationReturnsPrimitive_callsValidateReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn("Test Value");
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(eq(int.class))).thenReturn(false);
 when(invocation.isValidReturnType(eq(String.class))).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidate_answerInstancesReturnsNotNull_invocationReturnsObject_callsValidateReturnValue() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn(new Object());
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(eq(String.class))).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidate_answerInstancesReturnsNotNull_invocationReturnsVoid_throwsIllegalArgumentException() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn(new Object());
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 Assert.assertThrows(IllegalArgumentException.class, () -> new AnswersValidator(mockReporter).validate(returnsAnswer, invocation));
 }

 @Test
 public void testValidate_answerInstancesReturnsNull_invocationReturnsVoid_doesNothing() {
 Returns returnsAnswer = mock(Returns.class);
 when(returnsAnswer.getReturnValue()).thenReturn(null);
 Invocation invocation = mock(Invocation.class);
 when(invocation.isVoid()).thenReturn(true);
 new AnswersValidator(mockReporter).validate(returnsAnswer, invocation);
 verify(mockReporter, never()).cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidate_answerInstancesThrowsNullThrowable_throwsNullArgumentException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenReturn(null);
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 Assert.assertThrows(NullPointerException.class, () -> new AnswersValidator(mockReporter).validate(throwsAnswer, invocation));
 }

 @Test
 public void testValidate_answerInstancesThrowsInvokedException_doesNotCallValidateException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenThrow(new IllegalStateException("Test Expected Exception"));
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 verify(mockReporter, never()).checkedExceptionInvalid(any());
 }

 @Test
 public void testValidate_answerInstancesThrowsUnsupportedException_doesNotCallValidateException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenThrow(new UnsupportedOperationException("Test Unsupported Exception"));
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 verify(mockReporter, never()).checkedExceptionInvalid(any());
 }

 @Test
 public void testValidate_answerInstancesThrowsOtherException_callsValidateException() {
 ThrowsException throwsAnswer = mock(ThrowsException.class);
 when(throwsAnswer.getThrowable()).thenThrow(new RuntimeException("Test Other Exception"));
 Invocation invocation = mock(Invocation.class);
 new AnswersValidator(mockReporter).validate(throwsAnswer, invocation);
 verify(mockReporter).onlyRuntimeExceptionsCanBeThrown();
 }

}